package Controller;

import Data.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;


public class FinishedController {

    @FXML
    public TableView<Series> tableFinishedSeries;
    @FXML
    public TableColumn<Series, String> columnName;
    @FXML
    public TableColumn<Series, Integer> columnSeasons;
    @FXML
    public TableColumn<Series, Integer> columnEpisodes;
    @FXML
    public Button buttonBack;

    public void initialize() {
        ObservableList<Series> finishedSeries = FXCollections.observableArrayList();
        ObservableList<Series> listEntries = FXCollections.observableArrayList(Series.readData());
        if (!listEntries.isEmpty()) {
            for (Series listEntry : listEntries) {
                if(listEntry.getUserState() == 3){
                    finishedSeries.add(listEntry);
                }
            }
        }

        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSeasons.setCellValueFactory(new PropertyValueFactory<>("seasons"));
        columnEpisodes.setCellValueFactory(new PropertyValueFactory<>("sumEpisodes"));

        tableFinishedSeries.setItems(finishedSeries);
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
