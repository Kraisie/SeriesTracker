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

    private static Path pathSettings;
    private static Path pathSave;
    private static Path pathBackUp;
    private int backUpCycle;      //every x days BackUp

    public Settings(Path pathS, Path pathSav, Path pathB, int backUpCycle) {
        pathSettings = pathS;
        pathSave = pathSav;
        pathBackUp = pathB;
        this.backUpCycle = backUpCycle;
    }

    public Settings() {
    }

    public static Settings readData() {
        String json;
        if (pathSettings != null) {
            if (pathSettings.toFile().exists()) {
                try {
                    json = new String(Files.readAllBytes(pathSettings));
                } catch (IOException e) {
                    json = null;
                }
                Gson gson = new Gson();

                return gson.fromJson(json, Settings.class);
            }
        }

        return null;
    }

    public static void writeData(Settings settings) {
        Gson gson = new Gson();
        String json = gson.toJson(settings);
        try {
            Files.write(pathSettings, json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            PopUp.error("Trying to save data failed!");
        }
    }

    public Path getPathSettings() {
        return pathSettings;
    }

    public void setPathSettings(Path path) {
        pathSettings = path;
    }

    public Path getPathSave() {
        return pathSave;
    }

    public void setPathSave(Path path) {
        pathSave = path;
    }

    public Path getPathBackUp() {
        return pathBackUp;
    }

    public void setPathBackUp(Path path) {
        pathBackUp = path;
    }

    public int getBackUpCycle() {
        return backUpCycle;
    }

    public void setBackUpCycle(int cycle) {
        backUpCycle = cycle;
    }

    public void setStandardSettings() {
        pathSettings = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Settings.json");
        pathSave = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Series.json");
        pathBackUp = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/BackUp.json");
        backUpCycle = 1;
    }
}
