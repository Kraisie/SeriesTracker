package Data.TVDB;

import Data.Episode;
import Data.MySeries;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TVDB_Data {

    //-1=no predefined Status (-1 add); 0=not started; 1=watching; 2=wait for new episodes; 3=finished (0-3 update)
    public static MySeries searchFindAndGetSeries(String seriesName, int currentSeason, int currentEpisode, int userState) {
        //LOGIN
        String token = logIn();

        //SEARCH possible Data.Series
        SeriesSearchData suggestions = searchPossibleSeries(seriesName, token, false);

        if (suggestions == null) {
            System.out.println("\n\tCould not find \"" + seriesName + "\". Searching on german...");
            suggestions = searchPossibleSeries(seriesName, token, true);
        }

        if (suggestions == null) {
            System.out.println("\tNothing found on german!");
            System.out.println("\tThe series did not get added to your list. Please add it manually or check the name on TVDB.com!");
            System.out.println("\tThe series might exists, but may have wrong values in the Database.");

            return null;
        }

        //SELECT from possible series
        int id = selectSeries(suggestions, seriesName);

        //Should never happen, but just to be safe
        if (id == 0) {
            System.out.println("\nID not found!");
            return null;
        }

        //GET series with ID
        SeriesData series = getSeries(token, id);

        //GET Episodes
        List<Episode> episodes = getEpisodes(token, id, currentSeason, currentEpisode);

        //If userState is not given (-1) set it on 0 (not started)
        if (userState == -1) {
            userState = 0;
        }

        String overview;
        if(series.getData().getOverview() == null || series.getData().getOverview().equals("")) {
            overview = "Not given!";
        }else {
            overview = series.getData().getOverview();
        }

        String banner;
        if(series.getData().getBanner() == null || series.getData().getBanner().equals("")) {
            banner = "Not given!";
        }else {
            banner = series.getData().getOverview();
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
        if(suggestions.getData().length < max) {
            max = suggestions.getData().length;
        }

        List<MySeries> suggestedSeries = new ArrayList<>();
        for(int i = 0; i < max; i++) {
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

    public static MySeries getUpdate (String providedID, int userState, int currentSeason, int currentEpisode) {

        //LOGIN
        String token = logIn();

        //GET Series
        SeriesData series = getSeries(token, Integer.valueOf(providedID));

        //GET Episodes
        List<Episode> episodes = getEpisodes(token, Integer.valueOf(providedID), currentSeason, currentEpisode);

        //If userState is not given (-1) set it on 0 (not started)
        if(userState == -1){
            userState = 0;
        }

        String overview;
        if(series.getData().getOverview() == null || series.getData().getOverview().equals("")) {
            overview = "Not given!";
        }else {
            overview = series.getData().getOverview();
        }

        String banner;
        if(series.getData().getBanner() == null || series.getData().getBanner().equals("")) {
            banner = "Not given!";
        }else {
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

        if(foundJSON.contains("\"Error\":")) {
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

    private static int selectSeries(SeriesSearchData suggestions, String seriesName) {
        if (suggestions.getData().length > 1) {             // > because 4 Strings define 1 series info package
            System.out.println("\nThere are more than one series by the name \"" + seriesName + "\". Please type in the number of the following series you want to track:");
            //Maximum of 5 series to display
            int max = 5;
            if(suggestions.getData().length < max) {
                max = suggestions.getData().length;
            }

            for (int i = 0; i < max; i++) {
                System.out.println("\nNumber " + i + ":");
                System.out.println("\tFirst aired: " + suggestions.getData()[i].getFirstAired());
                System.out.println("\tID: " + suggestions.getData()[i].getId());
                System.out.println("\tPublisher: " + suggestions.getData()[i].getNetwork());
                System.out.println("\tDescription: " + suggestions.getData()[i].getOverview());
                System.out.println("\tStatus: " + suggestions.getData()[i].getStatus());
            }

            Scanner scanner = new Scanner(System.in);

            while(true){
                System.out.print("\n\n\tPlease select a number: ");
                try{
                    int pick = Integer.valueOf(scanner.nextLine());

                    if(pick >= 0 && pick < max){
                        return suggestions.getData()[pick].getId();
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please pick a number between 0 and " + (max - 1) + "!");
                }
            }
        } else {
            return suggestions.getData()[0].getId();
        }
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

        List<Episode> allEpisodes = new ArrayList<>();
        for(int i = 0; i < episodes.getData().length; i++) {
            if(episodes.getData()[i].getEpisodeName() != null && !episodes.getData()[i].getEpisodeName().equals("")) {
                allEpisodes.add(new Episode(
                        Integer.valueOf(episodes.getData()[i].getAiredEpisodeNumber()),
                        Integer.valueOf(episodes.getData()[i].getAiredSeason()),
                        episodes.getData()[i].getEpisodeName(),
                        episodes.getData()[i].getOverview()
                ));
            }else {
                if(episodes.getData()[i].getOverview() != null && !episodes.getData()[i].getOverview().equals("")) {
                    allEpisodes.add(new Episode(
                            Integer.valueOf(episodes.getData()[i].getAiredEpisodeNumber()),
                            Integer.valueOf(episodes.getData()[i].getAiredSeason()),
                            episodes.getData()[i].getEpisodeName(),
                            episodes.getData()[i].getOverview()
                    ));
                } else {
                    allEpisodes.add(new Episode(
                            Integer.valueOf(episodes.getData()[i].getAiredEpisodeNumber()),
                            Integer.valueOf(episodes.getData()[i].getAiredSeason()),
                            "Not given",
                            "Not given!"
                    ));
                }
            }
        }

        for(int i = 2; i <= episodes.getLinks().getLast(); i++) {
            episodesJSON = requestToString("getEpisodes", token, String.valueOf(id), false, i);
            episodes = gson.fromJson(episodesJSON, SeriesEpisodes.class);

            for(int j = 0; j < episodes.getData().length; j++) {
                if(episodes.getData()[i].getEpisodeName() == null || episodes.getData()[i].getEpisodeName().equals("") || episodes.getData()[i].getOverview() == null || episodes.getData()[i].getOverview().equals("")) {
                    allEpisodes.add(new Episode(
                            Integer.valueOf(episodes.getData()[i].getAiredEpisodeNumber()),
                            Integer.valueOf(episodes.getData()[i].getAiredSeason()),
                            episodes.getData()[i].getEpisodeName(),
                            episodes.getData()[i].getOverview()
                    ));
                }else {
                    allEpisodes.add(new Episode(
                            Integer.valueOf(episodes.getData()[i].getAiredEpisodeNumber()),
                            Integer.valueOf(episodes.getData()[i].getAiredSeason()),
                            "Not given",
                            "Not given!"
                    ));
                }
            }
        }

        for (Episode epi : allEpisodes) {
            if (epi.getSeason() == currentSeason && epi.getEpNumberOfSeason() == currentEpisode) {
                epi.setCurrent(true);
            }
        }

        return allEpisodes;
    }
}
