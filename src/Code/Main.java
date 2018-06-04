package Code;

import Controller.MainSeriesController;
import Data.BackUp;
import Data.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;

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

        //Create BackUp if last BackUp is older than 24 hours
        if (checkOldBackUp()) {
            writeBackUp(new BackUp());
        }

        URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
        Image img = new Image(resource.toString());

        Parent root = FXMLLoader.load(getClass().getResource("/resources/FXML/MainSeries.fxml"));
        primaryStage.setTitle("Series Control Panel");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(img);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
