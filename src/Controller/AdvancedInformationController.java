package Controller;

import Data.Episode;
import Data.MySeries;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AdvancedInformationController {

    @FXML
    ImageView imgSeriesBanner;
    @FXML
    Label labelSeriesName;

    @FXML
    Label labelStatusInfo;
    @FXML
    Label labelRuntimeInfo;
    @FXML
    Label labelWastedInfo;
    @FXML
    Label labelRatingInfo;
    @FXML
    Label labelCSeasonInfo;
    @FXML
    Label labelCEpisodeInfo;
    @FXML
    Label labelNrSeasonInfo;
    @FXML
    Label labelNrEpisodeInfo;
    @FXML
    Label labelCompletionInfo;

    @FXML
    TextArea overviewSeries;
    @FXML
    Label labelEpOfSeason;
    @FXML
    Label labelIsCurrent;
    @FXML
    TextArea overviewEpisode;

    @FXML
    Button backButton;
    @FXML
    Button backMenuButton;

    private String tmp;
    private int cS;
    private int cE;
    private MySeries series;
    private boolean first = true;

    public void initialize() {
        if (first) {
            tmp = AdvancedInformationSelectionController.toController.getCurrent().getSeason() + "." + AdvancedInformationSelectionController.toController.getCurrent().getEpNumberOfSeason();
            cS = AdvancedInformationSelectionController.toController.getCurrent().getSeason();
            cE = AdvancedInformationSelectionController.toController.getCurrent().getEpNumberOfSeason();
            series = AdvancedInformationSelectionController.toController;
            first = false;
        }

        //ToDo: Set banner (API Link)

        labelSeriesName.setText(series.getName());
        labelStatusInfo.setText(series.getStatus());
        labelRuntimeInfo.setText(String.valueOf(series.getRuntime()) + " minutes");
        labelWastedInfo.setText(AdvancedInformationSelectionController.toController.getWastedTime());
        labelRatingInfo.setText(String.valueOf(series.getRating()) + " / " + "10");
        labelCSeasonInfo.setText(String.valueOf(AdvancedInformationSelectionController.toController.getCurrent().getSeason()) + " / " + series.getNumberOfSeasons());
        labelCEpisodeInfo.setText(String.valueOf(AdvancedInformationSelectionController.toController.getCurrent().getEpNumberOfSeason()) + " / " + series.getSumEpisodes());
        labelNrSeasonInfo.setText(String.valueOf(series.getNumberOfSeasons()));
        labelNrEpisodeInfo.setText(String.valueOf(series.getSumEpisodes()));
        labelCompletionInfo.setText(String.format("%.2f", AdvancedInformationSelectionController.toController.getCompletionRate()) + "%");
        overviewSeries.setText(series.getDescription());

        labelEpOfSeason.setText(series.getCurrent().getSeason() + "." + series.getCurrent().getEpNumberOfSeason());
        overviewEpisode.setText(series.getCurrent().getOverview());
    }

    private void update() {
        labelEpOfSeason.setText(series.getCurrent().getSeason() + "." + series.getCurrent().getEpNumberOfSeason());

        if (tmp.equals(labelEpOfSeason.getText())) {
            labelIsCurrent.setVisible(true);
        } else {
            labelIsCurrent.setVisible(false);
        }

        overviewEpisode.setText(series.getCurrent().getOverview());
    }

    public void incEpisode() {
        List<Episode> episodes = series.getEpisodes();
        if (episodes.indexOf(series.getCurrent()) < episodes.size() - 1) {
            int pos = episodes.indexOf(series.getCurrent());
            series.getEpisodes().get(pos).setCurrent(false);
            series.getEpisodes().get(pos + 1).setCurrent(true);                  //Not saving since it is just to get up and down
        }

        update();
    }

    public void decEpisode() {
        if (series.getCurrent().getSeason() != 1 || series.getCurrent().getEpNumberOfSeason() != 1) {
            List<Episode> episodes = series.getEpisodes();
            int pos = episodes.indexOf(series.getCurrent());
            series.getEpisodes().get(pos).setCurrent(false);
            series.getEpisodes().get(pos - 1).setCurrent(true);                 //Not saving since it is just to get up and down

            update();
        }
    }

    public void selectCurrentEpisode() {
        List<Episode> allEpisodes = series.getEpisodes();
        for(Episode episode : allEpisodes) {
            if(episode.getSeason() == cS && episode.getEpNumberOfSeason() == cE) {
                int pos = allEpisodes.indexOf(series.getCurrent());
                series.getEpisodes().get(pos).setCurrent(false);
                episode.setCurrent(true);
            }
        }

        update();
    }

    public void back() {
        try {
            Stage primaryStage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/AdvancedInformationSelector.fxml"));
            primaryStage.setTitle("Series Control Panel");
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToMain() {
        try {
            Stage primaryStage = (Stage) backMenuButton.getScene().getWindow();
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
