package Controller;

import Code.PopUp;
import Data.MySeries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteController {

    @FXML
    public ListView<String> listViewSeries;
    @FXML
    public Button buttonBack;

    public void initialize() {
        ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());
        for (MySeries listEntry : listEntries) {
            listViewSeries.getItems().add(listEntry.getName());
        }
    }

    public void delete() {
        String name = listViewSeries.getSelectionModel().getSelectedItem();
        ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());
        for (MySeries listEntry : listEntries) {
            if (listEntry.getName().equals(name)) {
                listEntries.remove(listEntry);
                break;
            }
        }

        MySeries.writeData(listEntries);
        PopUp.show("Deleted!");
        back();
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
