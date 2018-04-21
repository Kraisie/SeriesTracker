package Controller;

import Data.Episode;
import Data.MySeries;
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
        labelNameSeries.setText(MainSeriesController.toController.getName());
        labelNumberSeasons.setText(String.valueOf(MainSeriesController.toController.getNumberOfSeasons()));

        List<Episode> episodes = MainSeriesController.toController.getEpisodes();
        int sumEpisodes = episodes.size();
        labelNumberEpisodes.setText(String.valueOf(sumEpisodes));

        labelCurrentSeason.setText(String.valueOf(MainSeriesController.toController.getCurrent().getSeason()) + " / " + MainSeriesController.toController.getNumberOfSeasons());
        labelCurrentEpisode.setText(String.valueOf(MainSeriesController.toController.getCurrent().getEpNumberOfSeason()) + " / " + MainSeriesController.toController.getSumEpisodesOfSeason(MainSeriesController.toController.getCurrent()));
        labelPercentageCompletition.setText(String.format("%.2f", MainSeriesController.toController.getCompletionRate()) + "%");

        labelWastedTime.setText(MySeries.wastedMinutesToString(MainSeriesController.toController.getWastedTime()));
        descriptionTextArea.setText(MainSeriesController.toController.getDescription());
    }

    public void back(){
        try {
            Stage primaryStage = (Stage) labelNameSeries.getScene().getWindow();
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
