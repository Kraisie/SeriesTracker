package Data;

import Code.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class BackUp {

    private long lastSave;
    private List<MySeries> series;

    private static Path PATH;

    public BackUp() {
        this.lastSave = System.currentTimeMillis();
        this.series = MySeries.readData();
    }

    public static BackUp readBackUp() {
        setPath();

        String json;
        try {
            json = new String(Files.readAllBytes(PATH));
        } catch (IOException e) {
            return new BackUp();
        }
        Gson gson = new Gson();

        return gson.fromJson(json, BackUp.class);
    }

    public static void writeBackUp(BackUp backUp) {
        setPath();
        Gson gson = new Gson();
        String json = gson.toJson(backUp);
        try {
            Files.write(PATH, json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            PopUp.error("BackUp failed, check the validity of your path!");
        }
    }

    public static boolean checkOldBackUp() {
        setPath();
        if (Files.exists(PATH)) {
            BackUp backUp = readBackUp();
            return (System.currentTimeMillis() - backUp.lastSave) >= 86400000L;
        } else {
            writeBackUp(new BackUp());
            return false;
        }
    }

    private static void setPath() {
        //Detect Windows or Linux and fuck Mac
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            PATH = Paths.get("F:\\BACKUP-SERIEN", "/Series-backup.json");
        } else if (System.getProperty("os.name").toLowerCase().contains("nux") || System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("aix")) {
            String buildPath = "/media/" + System.getProperty("user.name") + "/LEON-FP/BACKUP-SERIEN/Series-backup.json";
            PATH = Paths.get(buildPath);
        } else {
            PopUp.error("Could not identify your OS. Too bad.");
        }
    }

    public List<MySeries> getSeries() {
        return series;
    }
}
