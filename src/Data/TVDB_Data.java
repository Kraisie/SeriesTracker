package Data;

import Controller.PopUp;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TVDB_Data {

    //Serie
    private static String name;
    private static List<Episode> episodes;
    private static int userState = 0;       //0=not started; 1=watching; 2=wait for new episodes; 3=finished
    private static String status;           //Continuing / Ended
    private static int runtime;
    private static String description;
    private static double rating;

    public static Series searchFindAndGetSeries(String seriesName) {
        //LOGIN
        String token = logIn();

        //SEARCH Series
        String id = searchSeries(seriesName, token);

        //GET Series
        getSeries(token, id);

        //GET Episodes
        getEpisodes(token, id);

        return new Series(name, episodes, userState, status, runtime, description, rating);
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

    private static String searchSeries(String search, String token) {
        search = search.replaceAll(" ", "%20");
        search = search.replaceAll("%", "%25");
        search = search.replaceAll("&", "%26");
        search = search.replaceAll("ä", "%C3%A4");
        search = search.replaceAll("Ä", "%C3%84");
        search = search.replaceAll("ö", "%C3%B6");
        search = search.replaceAll("Ö", "%C3%96");
        search = search.replaceAll("ü", "%C3%BC");
        search = search.replaceAll("Ü", "%C3%9C");
        String foundJSON = requestToString("search", token, search);

        if (!foundJSON.contains("Resource not found")) {
            try {
                return findValues(foundJSON, "\"id\":", "[^\\d]");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            PopUp popUp = new PopUp();
            popUp.error("Could not find \"" + search + "\". Please make sure the name is correct.");
        }

        return "";
    }

    private static String requestToString(String mode, String token, String seriesIndication) {
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
                    request = new HttpGet("https://api.thetvdb.com/series/" + seriesIndication + "/episodes");
            }
            if (request != null) {
                request.setHeader("Authorization", "Bearer " + token);
                //GERMAN: request.addHeader("Accept-Language", "de");
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

        return "";
    }

    private static void getSeries(String token, String id) {
        String seriesJSON = requestToString("getSeries", token, id);

        try {
            name = findValues(seriesJSON, "\"seriesName\":", ".*?:\\s\"");
            status = findValues(seriesJSON, "\"status\":", ".*?:\\s\"");
            runtime = Integer.valueOf(findValues(seriesJSON, "\"runtime\":", "[^\\d]"));      //minutes
            description = findValues(seriesJSON, "\"overview\":", ".*?:\\s\"");
            rating = Double.valueOf(findValues(seriesJSON, "\"siteRating\":", "[^\\d*.\\d]"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getEpisodes(String token, String id) {
        String episodesJSON = requestToString("getEpisodes", token, id);
        System.out.println(episodesJSON);

        //List of all episodes
        episodes = searchEpisodes(episodesJSON);
    }

    private static List<Episode> searchEpisodes(String episodesJSON){
        List<Episode> episodes = new ArrayList<>();
        BufferedReader br = new BufferedReader(new StringReader(episodesJSON));
        String line;
        String valueNumberEpisodes = "\"airedEpisodeNumber\":";
        String valueNumberSeason = "\"airedEpisodeNumber\":";
        String valueNameEpisodes = "\"airedEpisodeNumber\":";
        String valueOverviewEpisodes = "\"airedEpisodeNumber\":";

        int numberEpisode = 0;
        int numberSeason = 0;
        String nameEpisode = "";
        String overviewEpisode = "";

        try{
            while((line = br.readLine()) != null){
                if(line.contains(valueNumberEpisodes)){
                    line = line.replaceAll("[^\\d]", "");
                    line = removeEndAndNewLine(line);
                    numberEpisode = Integer.valueOf(line);
                }else if(line.contains(valueNumberSeason)){
                    line = line.replaceAll("[^\\d]", "");
                    line = removeEndAndNewLine(line);
                    numberSeason = Integer.valueOf(line);
                }else if(line.contains(valueNameEpisodes)){
                    line = line.replaceAll(".*?:\\s\"", "");
                    line = removeEndAndNewLine(line);
                    nameEpisode = line;
                }else if(line.contains(valueOverviewEpisodes)){
                    line = line.replaceAll(".*?:\\s\"", "");
                    line = removeEndAndNewLine(line);
                    overviewEpisode = line;

                    episodes.add(new Episode(numberEpisode, numberSeason, nameEpisode, overviewEpisode));
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return episodes;
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

}
