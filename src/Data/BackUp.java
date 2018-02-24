package Data;

import Controller.PopUp;
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
    private List<Series> series;

    private static final Path PATH = Paths.get("F:\\BACKUP-SERIEN", "/Series-backup.json");

    public BackUp(){
        this.lastSave = System.currentTimeMillis();
        series = Series.readData();
    }

    private static BackUp readBackUp() {
        String json = "";
        try {
            json = new String(Files.readAllBytes(PATH));
        } catch (IOException e) {
            return new BackUp();
        }
        Gson gson = new Gson();

        return gson.fromJson(json, BackUp.class);
    }

    public static void writeBackUp(BackUp backUp) {
        PopUp pop = new PopUp();
        Gson gson = new Gson();
        String json = gson.toJson(backUp);
        try {
            Files.write(PATH, json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            pop.error("Trying to save data failed!");
        }
    }

    public static boolean checkOldBackUp(){
        if(Files.exists(PATH)){
            BackUp backUp = readBackUp();
            return (System.currentTimeMillis() - backUp.lastSave) >= 86400000L;
        }else{
            writeBackUp(new BackUp());
            return false;
        }
    }


}
