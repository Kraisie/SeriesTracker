package Data;

import Code.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class MyMovie {

    private String name;
    private int userState;      //0=to watch; 1=watched

    public MyMovie(String name, int userState) {
        this.name = name;
        this.userState = userState;
    }

    public static List<MyMovie> readData() {
        Settings settings = Settings.readData();
        String json;
        try {
            json = new String(Files.readAllBytes(settings.getPathMovies()));
        } catch (IOException e) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        MyMovie[] entries = gson.fromJson(json, MyMovie[].class);

        return new ArrayList<>(Arrays.asList(entries));
    }

    public static void writeData(List<MyMovie> allEntries) {
        //sort movies by name
        allEntries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        Settings settings = Settings.readData();

        Gson gson = new Gson();
        String json = gson.toJson(allEntries);
        try {
            Files.write(settings.getPathMovies(), json.getBytes(), TRUNCATE_EXISTING, CREATE);
        } catch (IOException e) {
            PopUp.error("Trying to save data failed!");
        }
    }

    public static void addData(MyMovie data) {
        List<MyMovie> list = readData();
        list.add(data);
        writeData(list);
    }

    public static boolean checkDuplicate(List<MyMovie> allEntries, String name) {
        if (!allEntries.isEmpty()) {
            for (MyMovie movie : allEntries) {
                if (name.equals(movie.getName())) {
                    return true;
                }
            }
        }

        return false;
    }

    //as Movies are just Strings atm no method needs to get the name
    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyMovie series = (MyMovie) o;
        return Objects.equals(name, series.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
