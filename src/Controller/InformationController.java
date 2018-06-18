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
import java.util.Objects;

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
    public Label labelPercentageCompletion;
    @FXML
    public Label labelWastedTime;
    @FXML
    public TextArea descriptionTextArea;

    private boolean main;
    public static List<MySeries> tmpMatches;

    public void initialize() {
        MySeries tmp = null;
        if (MainSeriesController.toController != null) {
            tmp = MainSeriesController.toController;
            main = true;
        } else if (SearchController.toController != null) {
            tmp = SearchController.toController;
            tmpMatches = SearchController.tmpMatches;
            main = false;
        }

        if (tmp != null) {
            labelNameSeries.setText(tmp.getName());
            labelNumberSeasons.setText(String.valueOf(tmp.getNumberOfSeasons()));

            List<Episode> episodes = tmp.getEpisodes();
            int sumEpisodes = episodes.size();
            labelNumberEpisodes.setText(String.valueOf(sumEpisodes));

            labelCurrentSeason.setText(String.valueOf(tmp.getCurrent().getSeason()) + " / " + tmp.getNumberOfSeasons());
            labelCurrentEpisode.setText(String.valueOf(tmp.getCurrent().getEpNumberOfSeason()) + " / " + tmp.getSumEpisodesOfSeason(tmp.getCurrent()));
            labelPercentageCompletion.setText(String.format("%.2f", tmp.getCompletionRate()) + "%");

            labelWastedTime.setText(MySeries.wastedMinutesToString(tmp.getWastedTime()));
            descriptionTextArea.setText(tmp.getDescription());
        }
    }

    public void back() {
        try {
            if (main) {
                Stage primaryStage = (Stage) labelNameSeries.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/MainSeries.fxml")));
                primaryStage.setTitle("Series Control Panel");
                primaryStage.setScene(new Scene(root));
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.show();
            } else {
                Stage primaryStage = (Stage) labelNameSeries.getScene().getWindow();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/SearchSeries.fxml")));
                primaryStage.setTitle("Search one of your series by attributes");
                primaryStage.setScene(new Scene(root));
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
