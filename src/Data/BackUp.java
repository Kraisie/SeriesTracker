package Data;

import Code.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class BackUp {

    private final long lastSave;
    private final List<MySeries> series;

    public BackUp() {
        this.lastSave = System.currentTimeMillis();
        this.series = MySeries.readData();
    }

    public static BackUp readBackUp() {
        Settings settings = Settings.readData();
        if (settings.getPathBackUp().toFile().exists()) {
            String json;
            try {
                json = new String(Files.readAllBytes(settings.getPathBackUp()));
            } catch (IOException e) {
                return new BackUp();
            }
            Gson gson = new Gson();

            return gson.fromJson(json, BackUp.class);
        } else {
            PopUp.error("You do not have a BackUp to import!");
        }

        return null;
    }

    public static void writeBackUp(BackUp backUp) {
        Gson gson = new Gson();
        String json = gson.toJson(backUp);
        Settings settings = Settings.readData();
        try {
            Files.write(settings.getPathBackUp(), json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            PopUp.error("BackUp failed, check the validity of your path!");
        }
    }

    public static boolean checkOldBackUp() {
        Settings settings = Settings.readData();

        if(settings == null) {
            //shouldn't get here since Settings get created on start, but maybe the cheeky user deletes his settings
            settings = new Settings();
            settings.setStandardSettings();
            Settings.writeData(settings);
        }

        BackUp backUp = readBackUp();

        //check if backUp exists
        if(backUp == null) {
            backUp = new BackUp();
            BackUp.writeBackUp(backUp);
        }

        //return true if backUp is older than backUpCycle * 24 hours (ms) = x days
        return (System.currentTimeMillis() - backUp.lastSave) >= settings.getBackUpCycle() * 86400000L;
    }

    public List<MySeries> getSeries() {
        return series;
    }
}
