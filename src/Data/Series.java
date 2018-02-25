package Data;

import Controller.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Series {

    private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIENTRACKER/Series.json");

    private String name;
    private List<Episode> episodes;
    private int userState;      //0=not started; 1=watching; 2=wait for new episodes; 3=finished
    private String status;      //Continuing / Ended
    private int runtime;
    private String description;
    private double rating;
    private int currentSeason;
    private int currentEpisode;


    public Series(String name, List<Episode> episodes, int userStatetate, String status, int runtime, String description, double rating, int currentSeason, int currentEpisode){
        this.name = name;
        this.episodes = episodes;
        this.userState = userState;
        this.status = status;
        this.runtime = runtime;
        this.description = description;
        this.rating = rating;
        this.currentSeason = currentSeason;
        this.currentEpisode = currentEpisode;
    }

    public static List<Series> readData() {
        String json = "";
        try {
            json = new String(Files.readAllBytes(PATH));
        } catch (IOException e) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        Series[] entries = gson.fromJson(json, Series[].class);

        return new ArrayList<>(Arrays.asList(entries));
    }

    public static void writeData(List<Series> allEntries) {
        PopUp pop = new PopUp();
        Gson gson = new Gson();
        String json = gson.toJson(allEntries);
        try {
            Files.write(PATH, json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            pop.error("Trying to save data failed!");
        }
    }

    public static void addData(Series data) throws IOException {
        List<Series> list = readData();
        list.add(data);
        writeData(list);
    }

    public static boolean checkDuplicate(List<Series> allSeries, String name){
        boolean exists = false;
        for (Series series : allSeries) {
            if (name.equals(series.getName())) {
                PopUp pop = new PopUp();
                pop.error("This series already exists in your list!");
                exists = true;
                break;
            }
        }

        return !exists;
    }

    public int getSumEpisodes() {
        return episodes.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(int currentSeason) {
        this.currentSeason = currentSeason;
    }

    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(name, series.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
