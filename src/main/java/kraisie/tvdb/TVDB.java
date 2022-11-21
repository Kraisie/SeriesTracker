package kraisie.tvdb;

import com.google.gson.Gson;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import kraisie.data.APIKey;
import kraisie.data.Episode;
import kraisie.data.EpisodeList;
import kraisie.data.Settings;
import kraisie.data.definitions.UserState;
import kraisie.util.LogUtil;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TVDB {

	private final Token token;
	private final Settings settings;

	public TVDB() {
		APIKey key = APIKey.readKey();
		this.token = new Token(key.getFormattedKey());
		LogUtil.logDebug("Token: " + token);
		this.settings = Settings.readData();
	}

	public static Image getBannerImage(String banner) {
		try {
			String bannerUrl = "https://thetvdb.com/banners/" + banner;
			URL url = new URL(bannerUrl);
			BufferedImage bufImg = ImageIO.read(url);
			return SwingFXUtils.toFXImage(bufImg, null);
		} catch (IOException e) {
			return getFallbackImage();
		}
	}

	public static Image getFallbackImage() {
		try {
			URL noImage = TVDB.class.getResource("/Pics/NoImageAvailable.png");
			if (noImage == null) {
				throw new IllegalStateException("Resource Picture not available!");
			}

			BufferedImage bufImg = ImageIO.read(noImage);
			return SwingFXUtils.toFXImage(bufImg, null);
		} catch (IOException e) {
			return null;
		}
	}

	public boolean isTokenExpired() {
		return token.isExpired();
	}

	public List<SearchData> searchSeries(String name) {
		String urlSeries = getEncodedName(name);
		if (urlSeries.isBlank()) {
			return new ArrayList<>();
		}

		final String apiSearchSeriesUrl = "https://api.thetvdb.com/search/series?name=" + urlSeries;
		HttpGet request = generateRequest(apiSearchSeriesUrl);
		String json = receiveResponse(request);
		if (json == null) {
			return new ArrayList<>();
		}

		if (json.startsWith("{\"Error\":") || json.isEmpty()) {
			LogUtil.logWarning("API replied with error on search request!\nRequest: " + apiSearchSeriesUrl + "\nResponse: " + json);
			return new ArrayList<>();
		}

		SeriesSearchData searchData = extractSeriesSearchData(json);
		return Arrays.asList(searchData.getData());
	}

	private String getEncodedName(String name) {
		return URLEncoder.encode(name, StandardCharsets.UTF_8);
	}

	private HttpGet generateRequest(String url) {
		HttpGet request = new HttpGet(url);
		request.setHeader("Authorization", "Bearer " + token.getToken());
		request.addHeader("Accept", "application/json");
		request.addHeader("Accept-Language", settings.getLangIso());
		return request;
	}

	private String receiveResponse(HttpGet request) {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			return httpClient.execute(request, response -> getEntityContent(response.getEntity()));
		} catch (IOException e) {
			LogUtil.logError("HTTP Client could not execute request!", e);
		}

		return null;
	}

	private String getEntityContent(HttpEntity entity) {
		String json = "";
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8))) {
			json = reader.readLine();
		} catch (IOException e) {
			LogUtil.logError("Could not extract json from HTTP entity!", e);
		}

		return json;
	}

	private SeriesSearchData extractSeriesSearchData(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SeriesSearchData.class);
	}

	public SeriesPosters getSeriesPosters(int id) {
		final String apiQueryImagesUrl = "https://api.thetvdb.com/series/" + id + "/images/query?keyType=poster";
		String json = extractResponseEntity(apiQueryImagesUrl);
		if (json == null) {
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(json, SeriesPosters.class);
	}

	private String extractResponseEntity(final String apiGetEpisodesUrl) {
		HttpGet request = generateRequest(apiGetEpisodesUrl);
		String json = receiveResponse(request);
		if (json == null) {
			LogUtil.logWarning("Received no response on " + apiGetEpisodesUrl);
			return null;
		}

		if (json.startsWith("{\"Error\":") || json.isBlank()) {
			LogUtil.logWarning("API replied with error on episode request!\nRequest: " + apiGetEpisodesUrl + "\nResponse: " + json);
			return null;
		}
		return json;
	}

	public kraisie.data.Series getSeries(int tvdbID) {
		return getSeriesById(tvdbID);
	}

	private kraisie.data.Series getSeriesById(int id) {
		final String apiGetSeriesUrl = "https://api.thetvdb.com/series/" + id;
		String json = extractResponseEntity(apiGetSeriesUrl);
		if (json == null) {
			return null;
		}

		Gson gson = new Gson();
		SeriesData tvdbData = gson.fromJson(json, SeriesData.class);
		Series tvdbSeries = tvdbData.getData();
		List<Episode> episodes = getEpisodes(id);
		EpisodeList episodeList = new EpisodeList(episodes);
		return new kraisie.data.Series(
				tvdbSeries.getSeriesName(),
				tvdbSeries.getId(),
				episodeList,
				UserState.NOT_STARTED,
				tvdbSeries.getStatus(),
				Integer.parseInt(tvdbSeries.getRuntime().isBlank() ? "0" : tvdbSeries.getRuntime()),
				tvdbSeries.getOverview(),
				Double.parseDouble(tvdbSeries.getSiteRating().isBlank() ? "0.0" : tvdbSeries.getSiteRating()),
				tvdbSeries.getBanner(),
				tvdbSeries.getNetwork()
		);
	}

	private List<Episode> getEpisodes(int id) {
		SeriesEpisodes episodePage = getFirstPage(id);
		if (episodePage == null) {
			return new ArrayList<>();
		}

		int pages = episodePage.getLinks().getLast();
		List<SeriesEpisodes> episodes = new ArrayList<>();
		List<SeriesEpisodes> remainingEpisodes = getOtherPages(id, pages);

		episodes.add(episodePage);
		episodes.addAll(remainingEpisodes);
		return convertEpisodes(episodes);
	}

	private SeriesEpisodes getFirstPage(int id) {
		return getEpisodePage(id, 1);
	}

	private SeriesEpisodes getEpisodePage(int id, int page) {
		final String apiGetEpisodesUrl = "https://api.thetvdb.com/series/" + id + "/episodes?page=" + page;
		String json = extractResponseEntity(apiGetEpisodesUrl);
		if (json == null) {
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(json, SeriesEpisodes.class);
	}

	private List<SeriesEpisodes> getOtherPages(int id, int pages) {
		List<SeriesEpisodes> otherEpisodes = new ArrayList<>();
		for (int page = 2; page <= pages; page++) {
			SeriesEpisodes episodes = getEpisodePage(id, page);
			if (episodes == null) {
				LogUtil.logWarning("Episode page (" + page + "/" + pages + ") is null! Ignoring that page.");
				return new ArrayList<>();
			}

			otherEpisodes.add(episodes);
		}

		return otherEpisodes;
	}

	private List<Episode> convertEpisodes(List<SeriesEpisodes> episodes) {
		List<Episode> convertedEpisodes = new ArrayList<>();
		for (SeriesEpisodes episode : episodes) {
			BasicEpisode[] basicEpisodes = episode.getData();
			for (BasicEpisode basicEpisode : basicEpisodes) {
				if (isSpecialEpisode(basicEpisode)) {
					continue;
				}

				Episode convertedEpisode = convertEpisode(basicEpisode);
				convertedEpisodes.add(convertedEpisode);
			}
		}

		convertedEpisodes.sort((e1, e2) -> {
			int value1 = e1.getSeason() - e2.getSeason();
			if (value1 == 0) {
				return e1.getEpNumberOfSeason() - e2.getEpNumberOfSeason();
			}
			return value1;
		});

		return convertedEpisodes;
	}

	private boolean isSpecialEpisode(BasicEpisode episode) {
		int seasonNumber = Integer.parseInt(episode.getAiredSeason());
		int episodeNumber = Integer.parseInt(episode.getAiredEpisodeNumber());

		return (seasonNumber == 0 || episodeNumber == 0);
	}

	private Episode convertEpisode(BasicEpisode episode) {
		int seasonNumber = Integer.parseInt(episode.getAiredSeason());
		int episodeNumber = Integer.parseInt(episode.getAiredEpisodeNumber());

		String name = episode.getEpisodeName() == null ? "No name given" : episode.getEpisodeName();
		String overview = episode.getOverview() == null ? "No overview given" : episode.getOverview();
		String firstAired = episode.getFirstAired() == null ? "No air date given" : episode.getFirstAired();

		return new Episode(episodeNumber, seasonNumber, name, overview, firstAired);
	}

	public kraisie.data.Series updateSeries(kraisie.data.Series series) {
		int id = Integer.parseInt(series.getTvdbID());
		kraisie.data.Series updatedSeries = getSeries(id);
		List<Episode> episodes = getEpisodes(id);
		EpisodeList episodeList = new EpisodeList(episodes);
		kraisie.data.Series newSeries = new kraisie.data.Series(
				updatedSeries.getName(),
				series.getTvdbID(),
				episodeList,
				series.getUserStatus(),
				updatedSeries.getStatus(),
				updatedSeries.getRuntime(),
				updatedSeries.getDescription(),
				updatedSeries.getRating(),
				updatedSeries.getBanner(),
				updatedSeries.getNetwork()
		);

		skipToCurrent(newSeries, series);
		updateUserState(newSeries);
		return newSeries;
	}

	private void skipToCurrent(kraisie.data.Series newSeries, kraisie.data.Series oldSeries) {
		if (newSeries.getUserStatus() == UserState.NOT_STARTED) {
			return;
		}

		int index = 0;
		Episode oldCurrent = oldSeries.getEpisodeList().getCurrent();
		Episode newCurrent = newSeries.getEpisodeList().getEpisodes().get(index);
		while (!newCurrent.equals(oldCurrent) && newSeries.getEpisodeList().isIncrementable()) {
			newCurrent = newSeries.getEpisodeList().getEpisodes().get(++index);
		}

		if (newCurrent.equals(oldCurrent)) {
			newSeries.getEpisodeList().get(index).setCurrent(true);
		}
	}

	private void updateUserState(kraisie.data.Series newSeries) {
		if (newSeries.getUserStatus() == UserState.WAITING && newSeries.getStatus().equalsIgnoreCase("Ended") &&
				!newSeries.getEpisodeList().isIncrementable()) {
			newSeries.setUserStatus(UserState.FINISHED);
			return;
		}

		if ((newSeries.getUserStatus() == UserState.FINISHED || newSeries.getUserStatus() == UserState.WAITING) &&
				newSeries.getEpisodeList().isIncrementable()) {
			newSeries.setUserStatus(UserState.WATCHING);
			newSeries.increaseWatchProgress();
		}
	}
}
