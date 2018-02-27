package Data;

import Code.PopUp;
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
    private static List<Episode> episodes = new ArrayList<>();
    private static String status;           //Continuing / Ended
    private static int runtime;
    private static String description;
    private static double rating;

    public static Series searchFindAndGetSeries(String seriesName, int userState) {     //-1=no predefined Status (-1 add); 0=not started; 1=watching; 2=wait for new episodes; 3=finished (0-3 update)
        //LOGIN
        String token = logIn();

        //SEARCH Series
        String id = searchSeries(seriesName, token, false);

        if (id.equals("")) {
            //Did not find english series
            System.out.println("\n\tCould not find \"" + seriesName + "\". Searching on german...");
            id = searchSeries(seriesName, token, true);
        }

        if (id.equals("")) {
            System.out.println("\tNothing found on german!");
            System.out.println("\tThe series did not get added to your list. Please add it manually or check the name on TVDB.com!");
            System.out.println("\tSome series are wrongfully saved in the Database (e.g Big Mouth, Disjointed, The end of the fucking world)");
            System.out.println("\tThey exist, but can not be found by the API. Only solution atm is to search them yourself and add infos manually\n");

            return null;
        }

        //GET Series
        getSeries(token, id);

        //GET Episodes
        getEpisodes(token, id); //get "last" of first page and iterate through every page until that page

        System.out.println(seriesName + " added.");

        //If userState is not given (-1) set it on 0 (not started)
        if(userState == -1){
            userState = 0;
        }
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

    private static String searchSeries(String search, String token, boolean german) {
        search = search.replaceAll("%", "%25");         //First, or it will change all other %
        search = search.replaceAll(" ", "%20");
        search = search.replaceAll("&", "%26");
        search = search.replaceAll("ä", "%C3%A4");
        search = search.replaceAll("Ä", "%C3%84");
        search = search.replaceAll("ö", "%C3%B6");
        search = search.replaceAll("Ö", "%C3%96");
        search = search.replaceAll("ü", "%C3%BC");
        search = search.replaceAll("Ü", "%C3%9C");

        String foundJSON;
        if (!german) {
            foundJSON = requestToString("search", token, search, !german, 0);
        } else {
            foundJSON = requestToString("search", token, search, german, 0);
        }

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
        String seriesJSON = requestToString("getSeries", token, id, false, 0);

        try {
            name = findValues(seriesJSON, "\"seriesName\":", ".*?:\\s\"");
            status = findValues(seriesJSON, "\"status\":", ".*?:\\s\"");

            String runtimeValue = findValues(seriesJSON, "\"runtime\":", "[^\\d]");
            if (!runtimeValue.isEmpty()) {
                runtime = Integer.valueOf(runtimeValue);            //minutes
            }

            description = findValues(seriesJSON, "\"overview\":", ".*?:\\s\"");

            String ratingValue = findValues(seriesJSON, "\"siteRating\":", "[^\\d*.\\d]");
            if (!ratingValue.isEmpty()) {
                rating = Double.valueOf(ratingValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getEpisodes(String token, String id) {
        String episodesJSON = requestToString("getEpisodes", token, id, false, 1);

        //request for number of pages
        int lastPage = 0;
        try {
            String lastPageString = findValues(episodesJSON, "\"last\":", "[^\\d]");
            if (lastPageString != null) {
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
                        nameEpisode = line;
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
