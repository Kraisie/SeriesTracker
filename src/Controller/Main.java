package Controller;

import Data.BackUp;
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
    public void start(Stage primaryStage) throws Exception{
        //Create BackUp if last BackUp is older than 24 hours
        if(checkOldBackUp()){
            writeBackUp(new BackUp());
        }

        URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
        Image img = new Image(resource.toString());

        Parent root = FXMLLoader.load(getClass().getResource("/resources/FXML/MainMenu.fxml"));
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
