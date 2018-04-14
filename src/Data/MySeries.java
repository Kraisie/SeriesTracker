package Data;

import Code.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class MySeries {

    private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIENTRACKER/Series.json");

    private String name;
    private String tvdbID;
    private List<Episode> episodes;
    private int userState;      //0=not started; 1=watching; 2=wait for new episodes; 3=finished
    private String status;      //Continuing / Ended
    private int runtime;
    private String description;
    private double rating;
    private String banner;


    public MySeries(String name, String tvdbID, List<Episode> episodes, int userState, String status, int runtime, String description, double rating, String banner) {
        this.name = name;
        this.tvdbID = tvdbID;
        this.episodes = episodes;
        this.userState = userState;
        this.status = status;
        this.runtime = runtime;
        this.description = description;
        this.rating = rating;
        this.banner = banner;
    }

    public static List<MySeries> readData() {
        String json = "";
        try {
            json = new String(Files.readAllBytes(PATH));
        } catch (IOException e) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        MySeries[] entries = gson.fromJson(json, MySeries[].class);

        return new ArrayList<>(Arrays.asList(entries));
    }

    public static void writeData(List<MySeries> allEntries) {
        //Delete all 0/null episodes (if season or episode = null/0)
        //and Sort episodes
        for (MySeries series : allEntries) {
            Episode.deleteNull(series.episodes);
            Episode.sort(series.episodes);
        }

        //sort series by name
        allEntries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        Gson gson = new Gson();
        String json = gson.toJson(allEntries);
        try {
            Files.write(PATH, json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            PopUp.error("Trying to save data failed!");
        }
    }

    public static void addData(MySeries data) {
        List<MySeries> list = readData();
        list.add(data);
        writeData(list);
    }

    public static boolean checkDuplicate(List<MySeries> allSeries, String name) {
        for (MySeries series : allSeries) {
            if (name.equals(series.getName())) {
                return true;
            }
        }

        return false;
    }

    public Episode getCurrent() {
        for (Episode ep : episodes) {
            if (ep.isCurrent()) {
                return ep;
            }
        }

        return null;
    }

    public void setCurrent(Episode newCurrent) {
        //just sets watched right for newCurrent after current, if it is before it won't change the ones behind
        for (Episode ep : episodes) {
            if (ep.equals(newCurrent)) {
                ep.setCurrent(true);
                break;
            }
            ep.setWatched(true);
        }
    }

    public boolean hasNext() {
        Episode current = getCurrent();
        int index = episodes.indexOf(current);
        return index < episodes.size() - 1;
    }

    //For CellValue
    public int getCurrentSeason() {
        if (getCurrent() == null) {
            return -1;
        } else {
            return getCurrent().getSeason();
        }
    }

    public int getCurrentEpisode() {
        if (getCurrent() == null) {
            return -1;
        } else {
            return getCurrent().getEpNumberOfSeason();
        }
    }

    public int getNumberOfSeasons() {
        Episode episode = episodes.get(episodes.size() - 1);
        return episode.getSeason();
    }
    //End CellValue

    public void setNewCurrent(Episode current, boolean direction) {         //true = ++ ; false = --
        int pos = episodes.indexOf(current);
        if (pos  < 0 || (pos + 1) > episodes.size()) {
            //Does not work, somehow inform user
        } else {
            if (direction) {
                setCurrent(episodes.get(pos + 1));
            } else {
                setCurrent(episodes.get(pos - 1));
            }
            episodes.get(pos).setCurrent(false);
        }
    }

    public Double getCompletionRate() {
        double sum = 0;
        for (Episode epi : episodes) {
            if (epi.isWatched()) {
                sum++;
            }
        }

        return (sum / episodes.size()) * 100;
    }

    public int getWastedTime() {
        int sum = 0;
        for (Episode epi : episodes) {
            if (epi.isWatched()) {
                sum++;
            }
        }

        return sum * runtime;
    }

    public static String wastedMinutesToString (int time) {
        //if lower than 2 hours print minutes, lower than 7 days print hours, elsewise days
        if (time < 120) {
            return time + " minutes wasted";
        } else if (time < (7 * 24 * 60)) {
            String wastedTime = String.format("%.2f", ((double)time / 60));
            return wastedTime + " hours wasted";
        } else {
            String wastedTime = String.format("%.2f", (((double)time / 60) / 24));
            return wastedTime + " days wasted";
        }
    }

    public int getSumEpisodesOfSeason (Episode current) {
        int sum = 0;
        List<Episode> allEpisodes = this.getEpisodes();
        for(Episode episode : allEpisodes) {
            if(episode.getSeason() == current.getSeason()) {
                sum++;
            }
        }

        return sum;
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

    public String getTvdbID() {
        return tvdbID;
    }

    public void setTvdbID(String tvdbID) {
        this.tvdbID = tvdbID;
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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MySeries series = (MySeries) o;
        return Objects.equals(name, series.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
