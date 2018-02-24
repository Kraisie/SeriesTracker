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
    private int seasons;
    private int[] episodes;
    private int currentSeason;
    private int currentEpisode;
    private int state;      //0=not started; 1=watching; 2=wait for new episodes; 3=finished

    public Series(String name, int seasons, int[] episodes, int currentSeason, int currentEpisode, int state){
        this.name = name;
        this.seasons = seasons;
        this.episodes = new int[seasons];
        for(int i = 0; i < episodes.length; i++){
            this.episodes[i] = episodes[i];
        }
        this.currentSeason = currentSeason;
        this.currentEpisode = currentEpisode;
        this.state = state;
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

    public int getSumEpisodes() {
        int sum = 0;
        for(int epi : episodes){
            sum += epi;
        }

        return sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
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

    public int[] getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int[] episodes) {
        this.episodes = episodes;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
