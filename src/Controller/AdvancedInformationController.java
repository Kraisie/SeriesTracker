package Controller;

import Data.Episode;
import Data.MySeries;
import Data.TVDB.TVDB_Data;
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
import java.util.Objects;

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
    private boolean main;
    public static List<MySeries> tmpMatches;

    public void initialize() {
        MySeries tmpSeries = null;
        if (MainSeriesController.toController != null) {
            tmpSeries = MainSeriesController.toController;
            main = true;
        } else if (SearchController.toController != null) {
            tmpSeries = SearchController.toController;
            tmpMatches = SearchController.tmpMatches;
            main = false;
        }

        if (first && tmpSeries != null) {
            tmp = tmpSeries.getCurrent().getSeason() + "." + tmpSeries.getCurrent().getEpNumberOfSeason();
            cS = tmpSeries.getCurrent().getSeason();
            cE = tmpSeries.getCurrent().getEpNumberOfSeason();
            series = tmpSeries;
            first = false;
        }

        if (series.getUserState() == 3) {
            labelIsCurrent.setText("\tSeries finished");
        }

        imgSeriesBanner.setImage(TVDB_Data.getBannerImage(series.getBanner()));

        labelSeriesName.setText(series.getName());
        labelStatusInfo.setText(series.getStatus());
        labelRuntimeInfo.setText(String.valueOf(series.getRuntime()) + " minutes");
        labelWastedInfo.setText(MySeries.wastedMinutesToString(tmpSeries.getWastedTime()));
        labelRatingInfo.setText(String.valueOf(series.getRating()) + " / " + "10");
        labelCSeasonInfo.setText(String.valueOf(tmpSeries.getCurrent().getSeason()) + " / " + series.getNumberOfSeasons());
        labelCEpisodeInfo.setText(String.valueOf(tmpSeries.getCurrent().getEpNumberOfSeason()) + " / " + series.getSumEpisodesOfSeason(tmpSeries.getCurrent()));
        labelNrSeasonInfo.setText(String.valueOf(series.getNumberOfSeasons()));
        labelNrEpisodeInfo.setText(String.valueOf(series.getSumEpisodes()));
        labelCompletionInfo.setText(String.format("%.2f", tmpSeries.getCompletionRate()) + "%");
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
        for (Episode episode : allEpisodes) {
            if (episode.getSeason() == cS && episode.getEpNumberOfSeason() == cE) {
                int pos = allEpisodes.indexOf(series.getCurrent());
                series.getEpisodes().get(pos).setCurrent(false);
                episode.setCurrent(true);
            }
        }

        update();
    }

    public void back() {
        try {
            if (main) {
                backToMain();
            } else {
                Stage primaryStage = (Stage) backButton.getScene().getWindow();
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

    public void backToMain() {
        try {
            tmpMatches = null;
            Stage primaryStage = (Stage) backMenuButton.getScene().getWindow();
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
