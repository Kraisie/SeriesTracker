package Controller;

import Data.MySeries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;


public class FinishedController {

    @FXML
    public TableView<MySeries> tableFinishedSeries;
    @FXML
    public TableColumn<MySeries, String> columnName;
    @FXML
    public TableColumn<MySeries, Integer> columnSeasons;
    @FXML
    public TableColumn<MySeries, Integer> columnEpisodes;
    @FXML
    public Label labelWasted;
    @FXML
    public Button buttonBack;

    public void initialize() {
        ObservableList<MySeries> finishedSeries = FXCollections.observableArrayList();
        ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());
        if (!listEntries.isEmpty()) {
            for (MySeries listEntry : listEntries) {
                if (listEntry.getUserState() == 3) {
                    finishedSeries.add(listEntry);
                }
            }
        }

        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnSeasons.setCellValueFactory(new PropertyValueFactory<>("numberOfSeasons"));
        columnEpisodes.setCellValueFactory(new PropertyValueFactory<>("sumEpisodes"));
        tableFinishedSeries.setItems(finishedSeries);

        int sum = 0;
        for (MySeries series : finishedSeries) {
            sum += series.getWastedTime();
        }
        labelWasted.setText(MySeries.wastedMinutesToString(sum));

    }

    public void back() {
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
