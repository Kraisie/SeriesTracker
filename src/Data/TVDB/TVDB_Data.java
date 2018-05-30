package Data.TVDB;

import Data.Episode;
import Data.MySeries;
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
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TVDB_Data {

    //-1=no predefined Status (-1 add); 0=not started; 1=watching; 2=wait for new episodes; 3=finished (0-3 update)

    public static Image getBannerImage(String banner) {
        try {
            URL urlImage1 = new URL("https://thetvdb.com/banners/" + banner);
            HttpURLConnection conn1 = (HttpURLConnection) urlImage1.openConnection();
            conn1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

            InputStream img1Input = conn1.getInputStream();
            BufferedImage img1 = ImageIO.read(img1Input);
            img1Input.close();
            conn1.disconnect();


            return SwingFXUtils.toFXImage(img1, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<MySeries> searchPossibleSeries(String seriesName, int currentSeason, int currentEpisode, int userState) {
        //LOGIN
        String token = logIn();

        //SEARCH possible Data.Series
        SeriesSearchData suggestions = searchPossibleSeries(seriesName, token, false);

        if (suggestions == null) {
            suggestions = searchPossibleSeries(seriesName, token, true);
        }
        if (suggestions == null) {
            return null;
        }

        //max 5 series
        int max = 5;
        if(suggestions.getData() != null) {
            if (suggestions.getData().length < max) {
                max = suggestions.getData().length;
            }
        } else {
            return null;
        }


        List<MySeries> suggestedSeries = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            //Needs banner, name and status
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

    public static MySeries getUpdate(String providedID, int userState, int currentSeason, int currentEpisode) {

        //LOGIN
        String token = logIn();

        //GET Series
        SeriesData series = getSeries(token, Integer.valueOf(providedID));

        //GET Episodes
        List<Episode> episodes = getEpisodes(token, Integer.valueOf(providedID), currentSeason, currentEpisode);

        //If userState is not given (-1) set it to 0 (not started)
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
                Integer.valueOf(series.getData().getRuntime()),
                overview,
                Double.valueOf(series.getData().getSiteRating()),
                banner                        //banner = 758x140
        );
    }

    private static String logIn() {
        String tokenJSON = "";
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://api.thetvdb.com/login");
            StringEntity params = new StringEntity("{\"apikey\": \"7769528BA49FC84C\",\"userkey\": \"C5C1089300EF9A15\", \"username\": \"Frolen307\"}");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                tokenJSON = convertStreamToString(instream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getToken(tokenJSON);
    }

    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private static String getToken(String tokenJSON) {
        tokenJSON = tokenJSON.replaceAll("\\{", "");
        tokenJSON = tokenJSON.replaceAll("}", "");
        tokenJSON = tokenJSON.replaceAll("\"", "");
        tokenJSON = tokenJSON.replaceAll("token:", "");
        tokenJSON = tokenJSON.replaceAll(" ", "");
        tokenJSON = tokenJSON.replaceAll("\n", "");

        return tokenJSON;
    }

    private static SeriesSearchData searchPossibleSeries(String seriesName, String token, boolean german) {
        //search Data.Series
        String urlEncodedSeries = null;
        try {
            urlEncodedSeries = URLEncoder.encode(seriesName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String foundJSON = requestToString("search", token, urlEncodedSeries, german, 0);

        if (foundJSON.contains("\"Alert\":")) {
            return null;
        }

        return extractSeries(foundJSON);
    }

    private static String requestToString(String mode, String token, String seriesIndication, boolean german, int page) {
        String stringJSON = "";
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet request = null;
            switch (mode) {
                //SEARCH
                case "search":
                    request = new HttpGet("https://api.thetvdb.com/search/series?name=" + seriesIndication);
                    break;
                //GET Series
                case "getSeries":
                    request = new HttpGet("https://api.thetvdb.com/series/" + seriesIndication);
                    break;
                //GET Episodes
                case "getEpisodes":
                    request = new HttpGet("https://api.thetvdb.com/series/" + seriesIndication + "/episodes?page=" + page);
            }
            if (request != null) {
                request.setHeader("Authorization", "Bearer " + token);
                if (german) {
                    request.addHeader("Accept-Language", "de");
                } else {
                    request.addHeader("Accept-Language", "en");
                }

                HttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    stringJSON = convertStreamToString(instream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringJSON;
    }

    private static SeriesSearchData extractSeries(String foundJSON) {
        Gson gson = new Gson();
        return gson.fromJson(foundJSON, SeriesSearchData.class);
    }

    private static SeriesData getSeries(String token, int id) {
        String seriesJSON = requestToString("getSeries", token, String.valueOf(id), false, 0);

        Gson gson = new Gson();
        return gson.fromJson(seriesJSON, SeriesData.class);
    }

    private static List<Episode> getEpisodes(String token, int id, int currentSeason, int currentEpisode) {
        String episodesJSON = requestToString("getEpisodes", token, String.valueOf(id), false, 1);

        Gson gson = new Gson();
        SeriesEpisodes episodes = gson.fromJson(episodesJSON, SeriesEpisodes.class);
        List<Episode> allEpisodes = setFields(episodes);

        for (int i = 2; i <= episodes.getLinks().getLast(); i++) {
            episodesJSON = requestToString("getEpisodes", token, String.valueOf(id), false, i);
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

    private static List<Episode> setFields(SeriesEpisodes episodes) {
        List<Episode> allEpisodes = new ArrayList<>();

        for (int i = 0; i < episodes.getData().length; i++) {
            //if sth not given set the String as not given
            Episode e = new Episode(0, 0, null, null, null);
            e.setEpNumberOfSeason(Integer.valueOf(episodes.getData()[i].getAiredEpisodeNumber()));
            e.setSeason(Integer.valueOf(episodes.getData()[i].getAiredSeason()));


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
