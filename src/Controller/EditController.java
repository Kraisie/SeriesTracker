package Controller;

import Data.MySeries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EditController {

    @FXML
    public ListView<String> listViewSeries;
    @FXML
    public Button buttonBack;

    public static MySeries toController;

    public void initialize(){
        toController = null;
        ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());
        for (MySeries listEntry : listEntries) {
            listViewSeries.getItems().add(listEntry.getName());
        }
    }

    public void edit(){
        //edit series like number of seasons, number of episodes in a season, the name and the state
        String name = listViewSeries.getSelectionModel().getSelectedItem();
        ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());
        for (MySeries listEntry : listEntries) {
            if(listEntry.getName().equals(name)){
                toController = listEntry;
                break;
            }
        }

        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/EditASeries.fxml"));
            primaryStage.setTitle("Edit the series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(){
        try {
            Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/MainSeries.fxml"));
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
