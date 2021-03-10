package kraisie.tvdb;

import com.google.gson.Gson;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import kraisie.data.APIKey;
import kraisie.data.Episode;
import kraisie.data.EpisodeList;
import kraisie.data.Settings;
import kraisie.data.definitions.UserState;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TVDB {

	private Token token;
	private Settings settings;

	public TVDB() {
		APIKey key = APIKey.readKey();
		this.token = new Token(key.getFormattedKey());
		System.out.println(this.token.getToken());
		this.settings = Settings.readData();
	}

	public static Image getBannerImage(String banner) {
		try {
			String banenrUrl = "https://thetvdb.com/banners/" + banner;
			URL url = new URL(banenrUrl);
			BufferedImage bufImg = ImageIO.read(url);

			return SwingFXUtils.toFXImage(bufImg, null);
		} catch (IOException e) {
			return getFallbackImage();
		}
	}

	public static Image getFallbackImage() {
		try {
			URL noImage = TVDB.class.getResource("/Pics/Alert/NoImageAvailable.png");
			BufferedImage bufImg = ImageIO.read(noImage);

			return SwingFXUtils.toFXImage(bufImg, null);
		} catch (IOException e) {
			return null;
		}
	}

	public List<SearchResult> searchSeries(String name) {
		String urlSeries = getEncodedName(name);
		if (urlSeries.isBlank()) {
			return new ArrayList<>();
		}

		final String apiSearchSeriesUrl = "https://api.thetvdb.com/search/series?name=" + urlSeries;
		HttpGet request = generateRequest(apiSearchSeriesUrl);
		HttpResponse response = receiveResponse(request);
		if (response == null) {
			return new ArrayList<>();
		}

		String json = getResponseContent(response);
		if (json.startsWith("{\"Error\":") || json.isEmpty()) {
			// TODO: log error
			return new ArrayList<>();
		}

		SeriesSearchData searchData = extractSeriesSearchData(json);
		SearchData[] seriesData = searchData.getData();
		List<SearchResult> results = new ArrayList<>();
		for (SearchData data : seriesData) {
			SeriesPosters posters = getSeriesPosters(data.getId());
			results.add(new SearchResult(data, posters));
		}

		return results;
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

	private HttpResponse receiveResponse(HttpGet request) {
		try {
			HttpClient httpClient = HttpClients.createDefault();
			return httpClient.execute(request);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: log error
		}

		return null;
	}

	private String getResponseContent(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		return getEntityContent(entity);
	}

	private String getEntityContent(HttpEntity entity) {
		String json = "";
		try {
			InputStream entityContent = entity.getContent();
			InputStreamReader isReader = new InputStreamReader(entityContent, StandardCharsets.UTF_8);
			BufferedReader reader = new BufferedReader(isReader);
			json = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: log error
		}

		return json;
	}

	private SeriesSearchData extractSeriesSearchData(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SeriesSearchData.class);
	}

	private SeriesPosters getSeriesPosters(int id) {
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
		HttpResponse response = receiveResponse(request);
		if (response == null) {
			// TODO: log error
			return null;
		}

		String json = getResponseContent(response);
		if (json.startsWith("{\"Error\":") || json.isBlank()) {
			// TODO: log error
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
				Integer.parseInt(tvdbSeries.getRuntime()),
				tvdbSeries.getOverview(),
				Double.parseDouble(tvdbSeries.getSiteRating()),
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
				// TODO: log error
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
}
