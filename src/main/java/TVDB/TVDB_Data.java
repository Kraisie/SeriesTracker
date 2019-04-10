package TVDB;

import Data.Episode;
import Data.MySeries;
import Data.Settings;
import Dialog.PopUp;
import SceneController.MainSeriesController;
import com.google.gson.Gson;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TVDB_Data {

	private String token;

	// -1=no predefined Status (-1 add); 0=not started; 1=watching; 2=wait for new episodes; 3=finished (0-3 update)

	public TVDB_Data() {
		this.token = logIn();
	}

	/*
	 *	pull a banner of the series
	 */
	public static Image getBannerImage(String banner) {
		try {
			URL url = new URL("https://thetvdb.com/banners/" + banner);
			BufferedImage bufImg = ImageIO.read(url);

			return SwingFXUtils.toFXImage(bufImg, null);
		} catch (IOException e) {
			return getFallbackBanner();

		}
	}

	private static Image getFallbackBanner() {
		try {
			URL noImage = MainSeriesController.class.getResource("/Pics/Alert/NoImageAvailable.png");
			BufferedImage bufImg = ImageIO.read(noImage);

			return SwingFXUtils.toFXImage(bufImg, null);
		} catch (IOException e) {
			return null;
		}
	}


	/*
	 *	search possibly fitting series to a search term
	 */
	public List<MySeries> searchSeries(String seriesName) {
		// SEARCH possible Series
		Settings settings = Settings.readData();
		SeriesSearchData suggestions = searchPossibleSeries(seriesName, token, settings.getLangIso());

		if (suggestions == null) {
			suggestions = searchPossibleSeries(seriesName, token, "en");
		}

		if (suggestions == null) {
			return null;
		}

		// max 5 series
		int max = 5;
		if (suggestions.getData() == null) {
			return null;
		}
		if (suggestions.getData().length < max) {
			max = suggestions.getData().length;
		}

		List<MySeries> suggestedSeries = new ArrayList<>();
		for (int i = 0; i < max; i++) {
			// Needs banner, name and status
			suggestedSeries.add(new MySeries(
					suggestions.getData()[i].getSeriesName(),
					String.valueOf(suggestions.getData()[i].getId()),
					new ArrayList<>(),
					0,
					suggestions.getData()[i].getStatus(),
					0,
					suggestions.getData()[i].getOverview(),
					0.0,
					suggestions.getData()[i].getBanner()
			));
		}

		return suggestedSeries;
	}

	/*
	 *	updated series that are already saved locally
	 */
	public MySeries getUpdate(String providedID, int userState, int currentSeason, int currentEpisode) {
		// GET Series
		Settings settings = Settings.readData();
		String langIso = settings.getLangIso();
		SeriesData series = getSeries(token, Integer.parseInt(providedID), langIso);

		if (series == null) {
			return null;
		}

		if (series.getData().getSeriesName() == null) {
			// series not available on that language
			series = getSeries(token, Integer.parseInt(providedID), "en");
		}

		if (series == null) {
			// use local copy
			return null;
		}

		// GET Episodes
		List<Episode> episodes = getEpisodes(token, Integer.parseInt(providedID), currentSeason, currentEpisode, langIso);

		// If userState is not given (-1) set it to 0 (not started)
		if (userState == -1) {
			userState = 0;
		}

		String overview;
		if (series.getData().getOverview() == null || series.getData().getOverview().equals("")) {
			overview = "Not given!";
		} else {
			overview = series.getData().getOverview();
		}

		String banner;
		if (series.getData().getBanner() == null || series.getData().getBanner().equals("")) {
			banner = "Not given!";
		} else {
			banner = series.getData().getBanner();
		}

		return new MySeries(
				series.getData().getSeriesName(),
				String.valueOf(series.getData().getId()),
				episodes,
				userState,
				series.getData().getStatus(),
				Integer.parseInt(series.getData().getRuntime()),
				overview,
				Double.valueOf(series.getData().getSiteRating()),
				banner                        //banner = 758x140
		);


	}

	/*
	 *	get access to the API
	 */
	private String logIn() {
		String tokenJSON = "";
		HttpClient httpClient = HttpClientBuilder.create().build();
		APIKey key = APIKey.readKey();
		if (key == null) {
			PopUp popUp = new PopUp();
			popUp.showWarning("Missing API Key!", "Please set an API-Key in the settings to use the program.");
			return null;
		}

		try {
			HttpPost request = new HttpPost("https://api.thetvdb.com/login");
			StringEntity params = new StringEntity(key.getFormattedKey());
			request.addHeader("content-type", "application/json");
			request.setEntity(params);

			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
				tokenJSON = reader.readLine();
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getToken(tokenJSON);
	}

	/*
	 *	extract token
	 */
	private static String getToken(String tokenJSON) {
		Gson gson = new Gson();
		Token token = gson.fromJson(tokenJSON, Token.class);

		return token.getToken();
	}

	/*
	 *	prepare search request to API
	 */
	private static SeriesSearchData searchPossibleSeries(String seriesName, String token, String language) {
		// search Data.Series
		String urlEncodedSeries = null;
		try {
			urlEncodedSeries = URLEncoder.encode(seriesName, java.nio.charset.StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String foundJSON = requestToString("search", token, urlEncodedSeries, 0, language);

		if (foundJSON == null) {
			return null;
		}

		if (foundJSON.contains("\"Alert\":") || foundJSON.isEmpty()) {
			return null;
		}

		return extractSeries(foundJSON);
	}

	/*
	 *	send requests to the API
	 */
	private static String requestToString(String mode, String token, String seriesIndication, int page, String languageIsoCode) {
		String stringJSON = "";
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpGet request = null;
			switch (mode) {
				// SEARCH
				case "search":
					request = new HttpGet("https://api.thetvdb.com/search/series?name=" + seriesIndication);
					break;
				// GET Series
				case "getSeries":
					request = new HttpGet("https://api.thetvdb.com/series/" + seriesIndication);
					break;
				// GET Episodes
				case "getEpisodes":
					request = new HttpGet("https://api.thetvdb.com/series/" + seriesIndication + "/episodes?page=" + page);
					break;
				default:
					// shouldn't happen
			}

			if (request == null) {
				return null;
			}

			request.setHeader("Authorization", "Bearer " + token);
			request.addHeader("Accept-Language", languageIsoCode);

			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				return null;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
			if ((stringJSON = reader.readLine()) == null) {
				reader.close();
				return null;
			}
			reader.close();

			if (stringJSON.startsWith("{\"Error\":\"ID: ") && stringJSON.endsWith(" not found\"}")) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return stringJSON;
	}

	/*
	 *	extract series
	 */
	private static SeriesSearchData extractSeries(String foundJSON) {
		Gson gson = new Gson();
		return gson.fromJson(foundJSON, SeriesSearchData.class);
	}

	/*
	 *	 getting a series from the API
	 */
	private static SeriesData getSeries(String token, int id, String language) {
		String seriesJSON = requestToString("getSeries", token, String.valueOf(id), 0, language);

		if (seriesJSON != null) {
			Gson gson = new Gson();
			return gson.fromJson(seriesJSON, SeriesData.class);
		}

		return null;
	}

	/*
	 *	getting a list of all episodes a series got
	 */
	private static List<Episode> getEpisodes(String token, int id, int currentSeason, int currentEpisode, String language) {
		String episodesJSON = requestToString("getEpisodes", token, String.valueOf(id), 1, language);

		Gson gson;
		SeriesEpisodes episodes;
		List<Episode> allEpisodes;

		if (episodesJSON == null) {
			return new ArrayList<>();
		}

		// {"Error":"No results for your query: map[...]} = No episodes listed in database
		if (episodesJSON.startsWith("{\"Error\":\"No results for your query:")) {
			return new ArrayList<>();
		}

		gson = new Gson();
		episodes = gson.fromJson(episodesJSON, SeriesEpisodes.class);
		allEpisodes = setFields(episodes);

		for (int i = 2; i <= episodes.getLinks().getLast(); i++) {
			episodesJSON = requestToString("getEpisodes", token, String.valueOf(id), i, language);
			episodes = gson.fromJson(episodesJSON, SeriesEpisodes.class);
			allEpisodes.addAll(setFields(episodes));
		}

		for (Episode epi : allEpisodes) {
			if (epi.getSeason() == currentSeason && epi.getEpNumberOfSeason() == currentEpisode) {
				epi.setCurrent(true);
			}
		}

		return allEpisodes;
	}

	/*
	 *	 change missing data to a general information about missing data
	 */
	private static List<Episode> setFields(SeriesEpisodes episodes) {
		List<Episode> allEpisodes = new ArrayList<>();

		for (int i = 0; i < episodes.getData().length; i++) {
			// if sth not given set the String as not given
			Episode e = new Episode(0, 0, null, null, null);
			e.setEpNumberOfSeason(Integer.parseInt(episodes.getData()[i].getAiredEpisodeNumber()));
			e.setSeason(Integer.parseInt(episodes.getData()[i].getAiredSeason()));


			if (episodes.getData()[i].getEpisodeName() != null && !episodes.getData()[i].getEpisodeName().equals("")) {
				e.setName(episodes.getData()[i].getEpisodeName());
			} else {
				e.setName("Not given!");
			}

			if (episodes.getData()[i].getOverview() != null && !episodes.getData()[i].getOverview().equals("")) {
				e.setOverview(episodes.getData()[i].getOverview());
			} else {
				e.setOverview("Not given!");
			}

			if (episodes.getData()[i].getFirstAired() != null && !episodes.getData()[i].getFirstAired().equals("")) {
				e.setFirstAired(episodes.getData()[i].getFirstAired());
			} else {
				e.setFirstAired("Not given!");
			}

			allEpisodes.add(e);
		}

		return allEpisodes;
	}
}
