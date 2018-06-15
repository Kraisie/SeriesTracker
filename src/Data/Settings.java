package Data;

import Code.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Settings {

    private String pathMovies;
    private String pathSeries;
    private String pathBackUp;
    private int backUpCycle;      //every x days BackUp

    private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Settings.json");

    /*
    * GSON CAN NOT SAVE A PATH AS IT HAS CYCLIC REFERENCE IN IT!
    * fs -> WindowsFileSystem -> provider -> WindowsFileSystem -> provider -> ...
    * THAT'S WHY WE SAVE THE PATH AS STRING AND RETURN THEM AS PATH IN THE GETTER
    * AND SAVE THEM AS PATH IN THE SETTER
    */

    public Settings(Path pathM, Path pathS, Path pathB, int backUpCycle) {
        pathMovies = pathM.toString();
        pathSeries = pathS.toString();
        pathBackUp = pathB.toString();
        this.backUpCycle = backUpCycle;
    }

    public Settings() {
    }

    public static Settings readData() {
        String json;
        if (!PATH.toFile().exists()) {
            try {
                Files.createDirectories(PATH.getParent());
                Files.createFile(PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            json = new String(Files.readAllBytes(PATH));
        } catch (IOException e) {
            json = null;
        }
        Gson gson = new Gson();

        return gson.fromJson(json, Settings.class);
    }

    public static void writeData(Settings settings) {
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        try {
            Files.write(PATH, json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            PopUp.error("Trying to save data failed!");
        }
    }

    public Path getPathMovies() {
        return Paths.get(pathMovies);
    }

    public void setPathMovies(Path path) {
        pathMovies = path.toString();
    }

    public Path getPathSeries() {
        return Paths.get(pathSeries);
    }

    public void setPathSeries(Path path) {
        pathSeries = path.toString();
    }

    public Path getPathBackUp() {
        return Paths.get(pathBackUp);
    }

    public void setPathBackUp(Path path) {
        pathBackUp = path.toString();
    }

    public int getBackUpCycle() {
        return backUpCycle;
    }

    public void setBackUpCycle(int cycle) {
        backUpCycle = cycle;
    }

    public void setStandardSettings() {
        pathMovies = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Settings.json").toString();
        pathSeries = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Series.json").toString();
        pathBackUp = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/BackUp.json").toString();
        backUpCycle = 1;
    }
}
