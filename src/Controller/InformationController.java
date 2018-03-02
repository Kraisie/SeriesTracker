package Controller;

import Data.Episode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class InformationController {

    @FXML
    public Label labelNameSeries;
    @FXML
    public Label labelNumberSeasons;
    @FXML
    public Label labelNumberEpisodes;
    @FXML
    public Label labelCurrentSeason;
    @FXML
    public Label labelCurrentEpisode;
    @FXML
    public Label labelPercentageCompletition;
    @FXML
    public Label labelWastedTime;
    @FXML
    public TextArea descriptionTextArea;

    public void initialize(){
        labelNameSeries.setText(MainMenuController.toController.getName());
        labelNumberSeasons.setText(String.valueOf(MainMenuController.toController.getNumberOfSeasons()));

        List<Episode> episodes = MainMenuController.toController.getEpisodes();
        int sumEpisodes = 0;
        for(Episode epi : episodes){
            sumEpisodes++;
        }
        labelNumberEpisodes.setText(String.valueOf(sumEpisodes));

        labelCurrentSeason.setText(String.valueOf(MainMenuController.toController.getCurrent().getSeason()) + " / " + MainMenuController.toController.getNumberOfSeasons());
        labelCurrentEpisode.setText(String.valueOf(MainMenuController.toController.getCurrent().getEpNumberOfSeason()) + " / " + MainMenuController.toController.getSumEpisodes());
        labelPercentageCompletition.setText(String.format("%.2f", MainMenuController.toController.getCompletionRate()) + "%");

        labelWastedTime.setText(MainMenuController.toController.getWastedTime());
        descriptionTextArea.setText(MainMenuController.toController.getDescription());
    }

    public void back(){
        try {
            Stage primaryStage = (Stage) labelNameSeries.getScene().getWindow();
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
