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

public class Series {

    private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIENTRACKER/Series.json");

    private String name;
    private List<Episode> episodes;
    private int userState;      //0=not started; 1=watching; 2=wait for new episodes; 3=finished
    private String status;      //Continuing / Ended
    private int runtime;
    private String description;
    private double rating;


    public Series(String name, List<Episode> episodes, int userState, String status, int runtime, String description, double rating) {
        this.name = name;
        this.episodes = episodes;
        this.userState = userState;
        this.status = status;
        this.runtime = runtime;
        this.description = description;
        this.rating = rating;
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
        //Delete all 0/null episodes (if season or episode = null/0)
        //and Sort episodes
        for (Series series : allEntries) {
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

    public static void addData(Series data) throws IOException {
        List<Series> list = readData();
        list.add(data);
        writeData(list);
    }

    public static boolean checkDuplicate(List<Series> allSeries, String name) {
        boolean exists = false;
        for (Series series : allSeries) {
            if (name.equals(series.getName())) {
                PopUp.error("This series already exists in your list!");
                exists = true;
                break;
            }
        }

        return !exists;
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
        for (Episode ep : episodes) {
            if (ep.equals(newCurrent)) {
                ep.setCurrent(true);
                break;
            }
        }
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
        if ((pos - 1) < 0 || (pos + 1) >= episodes.size()) {
            //Does not work, somehow inform user
        } else {
            if (direction) {
                setCurrent(episodes.get(pos + 1));
            } else {
                setCurrent(episodes.get(pos - 1));
            }

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

    public String getWastedTime() {
        int time = episodes.size() * runtime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return Objects.equals(name, series.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
