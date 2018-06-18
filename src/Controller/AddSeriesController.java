package Controller;

import Code.PopUp;
import Data.MySeries;
import Data.TVDB.TVDB_Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AddSeriesController {

    @FXML
    public TextField nameTVDB;
    @FXML
    public Button buttonBack;

    public static List<MySeries> foundSeries;

    public void addTVDB() {
        List<MySeries> allSeries = MySeries.readData();
        if (!MySeries.checkDuplicate(allSeries, nameTVDB.getText())) {
            List<MySeries> possibleSeries = TVDB_Data.searchPossibleSeries(nameTVDB.getText(), 1, 1, 0);
            //max 5 series
            if (possibleSeries != null && possibleSeries.size() != 0) {
                if (possibleSeries.size() != 1) {
                    foundSeries = possibleSeries;

                    try {
                        Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/SelectFoundSeries.fxml")));
                        primaryStage.setTitle("Series Control Panel");
                        primaryStage.setScene(new Scene(root));
                        primaryStage.centerOnScreen();
                        primaryStage.setResizable(false);
                        primaryStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    allSeries.add(TVDB_Data.getUpdate(possibleSeries.get(0).getTvdbID(), 0, 1, 1));
                    MySeries.writeData(allSeries);
                    PopUp.show(possibleSeries.get(0).getName() + " added.");
                    back();
                }
            } else {
                PopUp.error("Could not find \"" + nameTVDB.getText() + "\"!");
            }
        } else {
            PopUp.error(nameTVDB.getText() + " already exists!");
        }
    }

    public void back() {
        try {
            Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/MainSeries.fxml")));
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
