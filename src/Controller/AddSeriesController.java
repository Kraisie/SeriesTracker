package Controller;

import Code.PopUp;
import Data.Series;
import Data.TVDB_Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AddSeriesController {

    @FXML
    public TextField nameTVDB;
    @FXML
    public Button buttonBack;
    @FXML
    public Button manualAddButton;

    public static List<Series> foundSeries;

    public void manualAdd(){
        try {
            URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) manualAddButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/ManualAddSeries.fxml"));
            primaryStage.setTitle("Add a series manually");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTVDB(){
        List<Series> allSeries = Series.readData();
        if(!Series.checkDuplicate(allSeries, nameTVDB.getText())){
            List<Series> possibleSeries = TVDB_Data.searchSeries(nameTVDB.getText());
            //max 3 series
            if(possibleSeries != null && possibleSeries.size() != 0){
                if(possibleSeries.size() != 1) {
                    foundSeries = possibleSeries;

                    try {
                        Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/SelectFoundSeries.fxml"));
                        primaryStage.setTitle("Series Control Panel");
                        primaryStage.setScene(new Scene(root));
                        primaryStage.centerOnScreen();
                        primaryStage.setResizable(false);
                        primaryStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Series newSeries = TVDB_Data.getUpdate(String.valueOf(possibleSeries.get(1)), 0);
                    if(newSeries != null) {
                        allSeries.add(newSeries);
                        Series.writeData(allSeries);
                        PopUp.show(nameTVDB.getText() + " added.");
                    } else {
                        PopUp.error("Could not find \"" + nameTVDB.getText() + "\"!");
                    }
                }
            }
        }
        PopUp.error(nameTVDB.getText() + " already exists!");

        back();
    }

    public void back(){
        try {
            Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/MainMenu.fxml"));
            primaryStage.setTitle("Series Control Panel");
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
