package Code;

import Controller.MainSeriesController;
import Data.BackUp;
import Data.MySeries;
import Data.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static Data.BackUp.checkOldBackUp;
import static Data.BackUp.writeBackUp;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Check if there are Settings
        if (Settings.readData() == null) {
            Settings settings = new Settings();
            settings.setStandardSettings();
            Settings.writeData(settings);
        }

        //Create BackUp if last BackUp is older than 24 hours/back up cycle in settings
        if (checkOldBackUp()) {
            writeBackUp(new BackUp());
        }

        //check for newly aired episodes
        checkAirDates();

        //open stage
        URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
        Image img = new Image(resource.toString());

        Parent root = FXMLLoader.load(getClass().getResource("/resources/FXML/MainSeries.fxml"));
        primaryStage.setTitle("Series Control Panel");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(img);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void checkAirDates() {
        List<MySeries> allEntries = MySeries.readData();
        boolean updated = false;

        for (MySeries series : allEntries) {
            if (series.getUserState() == 3) {
                //check if the TVDB state changed to 'Continuing'
                if (series.getStatus().equals("Continuing")) {
                    series.setUserState(2);
                    updated = true;
                }
            }

            //if there are episodes after current in a series with userState 2 and a date before today or today set UserState to 1
            if (series.getUserState() == 2) {
                int index = series.getEpisodes().indexOf(series.getCurrent());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                if (series.hasNext() && !series.getEpisodes().get(index + 1).getFirstAired().equals("Not given!")) {
                    LocalDate date = LocalDate.parse(series.getEpisodes().get(index + 1).getFirstAired(), formatter);
                    if (date.isBefore(LocalDate.now())) {
                        series.setUserState(1);
                        series.setNewCurrent(series.getCurrent(), true);    //add 1 episode as we are changing from last episode of a season to the first episode of a new season
                        updated = true;
                        break;
                    }
                }
            }
        }

        //save changes if necessary
        if (updated) {
            MySeries.writeData(allEntries);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
