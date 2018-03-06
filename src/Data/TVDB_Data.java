package Data;

import Code.PopUp;
import javafx.scene.image.Image;
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

    //Serie
    private static String name;
    private static String id;
    private static List<Episode> episodes;
    private static String status;           //Continuing / Ended
    private static int runtime;
    private static String description;
    private static double rating;
    private static String banner;           //https://api.thetvdb.com/<banner>      758x140

    public static void main(String[] args) {
        getSeriesBanner("262980" ,"graphical/262980-g5.jpg");
    }

    public static Image getSeriesBanner(String id, String banner) {
        String token = logIn();

        HttpClient httpClient = HttpClientBuilder.create().build();
        //HttpGet request = new HttpGet("https://api.thetvdb.com/series/" + id + "/images/query?keyType=series");
        HttpGet request = new HttpGet("https://api.thetvdb.com/series/" + id + "/_cache/graphical/262980-g.jpg");

        if (request != null) {
            request.setHeader("Authorization", "Bearer " + token);
        }

        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                System.out.println(convertStreamToString(instream));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Series> searchSeries(String seriesName) {
        String token = logIn();
        List<Series> possibleSeries = searchPossibleSeries(seriesName, token, false);

        if(possibleSeries.size() == 0) {
            PopUp.error("Could not find \"" + seriesName + "\"!");
            return null;
        }

        return possibleSeries;
    }

    public static Series getUpdate (String providedID, int userState) {
        //RESET VALUES
        name = null;
        episodes = null;
        status = null;
        runtime = 0;
        description = null;
        rating = 0;
        banner = null;

        //LOGIN
        String token = logIn();

        //GET Series
        getSeries(token, providedID);

        //GET Episodes
        getEpisodes(token, providedID); //get "last" of first page and iterate through every page until that page

        //If userState is not given (-1) set it on 0 (not started)
        if(userState == -1){
            userState = 0;
        }

        return new Series(name, providedID, episodes, userState, status, runtime, description, rating, banner);
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

    private static List<Series> searchPossibleSeries(String seriesName, String token, boolean german) {
        //search Series
        String urlEncodedSeries = null;
        try {
            urlEncodedSeries = URLEncoder.encode(seriesName, java.nio.charset.StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String foundJSON = requestToString("search", token, urlEncodedSeries, german, 0);

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

    private static List<Series> extractSeries(String foundJSON) {
        BufferedReader br = new BufferedReader(new StringReader(foundJSON));
        List<Series> foundSeries = new ArrayList<>();
        String line;

        try {
            while ((line = br.readLine()) != null) {
                //MAX 5 SERIES!!!
                if(foundSeries.size() == 5){
                    break;
                }
                //id, has to be set, so no check
                if (line.contains("\"id\":")) {
                    line = line.replaceAll("[^\\d]", "");
                    foundSeries.add(getUpdate(line, 0));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foundSeries;
    }

    private static String removeEndAndNewLine(String line) {
        if (line.charAt(line.length() - 1) == ',') {
            line = line.substring(0, line.length() - 1);
        }
        if (line.charAt(line.length() - 1) == '\"') {
            line = line.substring(0, line.length() - 1);
        }

        line = line.replaceAll("\r", "");
        line = line.replaceAll("\n", "");

        return line;
    }

    private static void getSeries(String token, String id) {
        String seriesJSON = requestToString("getSeries", token, id, false, 0);

        try {
            name = findValues(seriesJSON, "\"seriesName\":", ".*?:\\s\"");
            status = findValues(seriesJSON, "\"status\":", ".*?:\\s\"");

            String runtimeValue = findValues(seriesJSON, "\"runtime\":", "[^\\d]");
            if (!runtimeValue.equals("Not given!") && !runtimeValue.isEmpty()) {
                runtime = Integer.valueOf(runtimeValue);            //minutes
            }

            description = findValues(seriesJSON, "\"overview\":", ".*?:\\s\"");
            description = description.replaceAll("\n", "");
            description = description.replaceAll("\r", "");
            description = description.replaceAll("\\\"", "\"");
            if (description.contains("\"overview\": null")) {
                description = "Not given!";
            }

            String ratingValue = findValues(seriesJSON, "\"siteRating\":", "[^\\d*.\\d]");
            if (!ratingValue.equals("Not given!") && !ratingValue.isEmpty()) {
                rating = Double.valueOf(ratingValue);
            }

            String bannerValue = findValues(seriesJSON, "\"banner\":", ".*?:\\s\"");
            if (!bannerValue.equals("Not given!") && !bannerValue.isEmpty()) {
                banner = bannerValue;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findValues(String foundJSON, String value, String regex) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(foundJSON));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains(value)) {
                //Delete everything besides value
                line = line.replaceAll(regex, "");
                if (line.charAt(line.length() - 1) == ',') {
                    line = line.substring(0, line.length() - 1);
                }
                if (line.charAt(line.length() - 1) == '\"') {
                    line = line.substring(0, line.length() - 1);
                }

                line = line.replaceAll("\r", "");
                line = line.replaceAll("\n", "");
                return line;
            }
        }

        return "Not given!";
    }

    private static void getEpisodes(String token, String id) {
        episodes = new ArrayList<>();
        String episodesJSON = requestToString("getEpisodes", token, id, false, 1);

        //request for number of pages
        int lastPage = 0;
        try {
            String lastPageString = findValues(episodesJSON, "\"last\":", "[^\\d]");
            if (!lastPageString.equals("Not given!")) {
                lastPage = Integer.valueOf(lastPageString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //request for every page 1 to last
        if (lastPage != 0) {
            for (int i = 1; i <= lastPage; i++) {             //first to last site (number one to = last)
                String episodesPageJSON = requestToString("getEpisodes", token, id, false, i);
                List<Episode> siteEpisodes = searchEpisodes(episodesPageJSON);
                episodes.addAll(siteEpisodes);
            }
        }
    }

    private static List<Episode> searchEpisodes(String episodesJSON) {
        List<Episode> episodes = new ArrayList<>();
        BufferedReader br = new BufferedReader(new StringReader(episodesJSON));
        String line;
        String valueNumberEpisodes = "\"airedEpisodeNumber\":";
        String valueNumberSeason = "\"airedSeason\":";
        String valueNameEpisodes = "\"episodeName\":";
        String valueOverviewEpisodes = "\"overview\":";

        int numberEpisode = 0;
        int numberSeason = 0;
        String nameEpisode = "";
        String overviewEpisode = "";

        try {
            int foundName = 0;
            int foundOverview = 0;
            while ((line = br.readLine()) != null) {
                if (line.contains(valueNumberEpisodes)) {
                    line = line.replaceAll("[^\\d]", "");
                    line = removeEndAndNewLine(line);
                    numberEpisode = Integer.valueOf(line);
                } else if (line.contains(valueNumberSeason)) {
                    line = line.replaceAll("[^\\d]", "");
                    line = removeEndAndNewLine(line);
                    numberSeason = Integer.valueOf(line);
                } else if (line.contains(valueNameEpisodes)) {
                    //First episodeName: is the episodeName, second is the language of the name
                    foundName++;
                    if ((foundName % 2) == 1) {
                        line = line.replaceAll(".*?:\\s\"", "");
                        line = removeEndAndNewLine(line);

                        if (!line.contains("null")) {
                            nameEpisode = line;
                        } else {
                            nameEpisode = "Not given!";
                        }
                    }
                } else if (line.contains(valueOverviewEpisodes)) {
                    //First is language of overview, second is overview
                    foundOverview++;
                    if ((foundOverview % 2) == 0) {
                        line = line.replaceAll(".*?:\\s\"", "");
                        line = removeEndAndNewLine(line);

                        if (!line.contains("null")) {
                            overviewEpisode = line;
                        } else {
                            overviewEpisode = "Not given!";
                        }
                        episodes.add(new Episode(numberEpisode, numberSeason, nameEpisode, overviewEpisode));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return episodes;
    }
}

