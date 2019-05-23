package com.Kraisie.SceneController;

import com.Kraisie.Data.Episode;
import com.Kraisie.Data.MySeries;
import com.Kraisie.Dialog.PopUp;
import com.Kraisie.TVDB.TVDB_Data;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class AdvancedInformationController extends Controller {

	@FXML
	private ImageView imgSeriesBanner;
	@FXML
	private Label labelSeriesName;
	@FXML
	private Label labelStatusInfo;
	@FXML
	private Label labelRuntimeInfo;
	@FXML
	private Label labelWastedInfo;
	@FXML
	private Label labelRatingInfo;
	@FXML
	private Label labelCurrentSeasonInfo;
	@FXML
	private Label labelCurrentEpisodeInfo;
	@FXML
	private Label labelNrSeasonInfo;
	@FXML
	private Label labelNrEpisodeInfo;
	@FXML
	private Label labelCompletionInfo;
	@FXML
	private TextArea overviewSeries;
	@FXML
	private Label labelEpOfSeason;
	@FXML
	private Label labelIsCurrent;
	@FXML
	private TextArea overviewEpisode;
	@FXML
	private Button backButton;
	@FXML
	private Button backMenuButton;

	private String tmpSeasonAndEpisodeSign;
	private int currentSeason;
	private int currentEpisode;
	private boolean first = true;
	private boolean main = false;
	private PopUp popUp = new PopUp();
	private MySeries series;
	private List<MySeries> tmpMatches;

	/**
	 * used to receive data from another controller via Dependency Injection
	 *
	 * @param series the series to show information about
	 */
	void initData(MySeries series) {
		this.series = series;
		tmpMatches = null;
		ownInitialize();
	}

	/**
	 * used to receive data from another controller via Dependency Injection
	 *
	 * @param series     the series to show information about
	 * @param tmpMatches a list of all matches that a search has given the user
	 */
	void initData(MySeries series, List<MySeries> tmpMatches) {
		this.series = series;
		this.tmpMatches = tmpMatches;
		ownInitialize();
	}

	/**
	 * This method is not needed as it would run as soon as the FXMLLoader loads the fxml file
	 * as such the parameters didn't already get passed as the initData can only be called after
	 * we initialized the Controller. Thus we later have to call an own InitializeFunction, but can
	 * also not just remove this function as it initializes all scene content.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * own function to initialize the scene due to a non-usable initialize function
	 */
	private void ownInitialize() {
		MySeries tmpSeries;
		tmpSeries = series;

		if (tmpMatches == null) {
			main = true;
			backButton.setDisable(true);
			backButton.setVisible(false);
		}

		// on first initialization
		if (first) {
			tmpSeasonAndEpisodeSign = tmpSeries.getCurrent().getSeason() + "." + tmpSeries.getCurrent().getEpNumberOfSeason();
			currentSeason = tmpSeries.getCurrentSeason();
			currentEpisode = tmpSeries.getCurrent().getEpNumberOfSeason();
			series = tmpSeries;
			first = false;
		}

		fillInformation(tmpSeries);
	}

	/**
	 * fill all fields with the fitting information about the series
	 *
	 * @param tmpSeries the series to show information about
	 */
	private void fillInformation(MySeries tmpSeries) {
		if (series.getUserState() == 3) {
			labelIsCurrent.setText("\tSeries finished");
		}

		Image banner = TVDB_Data.getBannerImage(series.getBanner());
		imgSeriesBanner.setImage(banner);
		centerImage(imgSeriesBanner);

		labelSeriesName.setText(series.getName());
		labelStatusInfo.setText(series.getStatus());
		labelRuntimeInfo.setText(series.getRuntime() + " minutes");
		labelWastedInfo.setText(MySeries.wastedMinutesToString(tmpSeries.getWastedTime()));
		labelRatingInfo.setText(series.getRating() + " / " + "10");
		labelCurrentSeasonInfo.setText(tmpSeries.getCurrent().getSeason() + " / " + series.getNumberOfSeasons());
		labelCurrentEpisodeInfo.setText(tmpSeries.getCurrent().getEpNumberOfSeason() + " / " + series.getSumEpisodesOfSeason(tmpSeries.getCurrent()));
		labelNrSeasonInfo.setText(String.valueOf(series.getNumberOfSeasons()));
		labelNrEpisodeInfo.setText(String.valueOf(series.getSumEpisodes()));
		overviewSeries.setText(series.getDescription());

		if (!tmpSeries.getCompletionRate().isNaN()) {
			labelCompletionInfo.setText(String.format("%.2f", tmpSeries.getCompletionRate()) + "%");
		} else {
			labelCompletionInfo.setText("Not calculable");
		}

		labelEpOfSeason.setText(series.getCurrent().getSeason() + "." + series.getCurrent().getEpNumberOfSeason());
		overviewEpisode.setText(series.getCurrent().getOverview());
	}

	/**
	 * increases the Episode which should get shown in the overview TextArea
	 *
	 * @see Episode
	 */
	@FXML
	private void incEpisode() {
		List<Episode> episodes = series.getEpisodes();
		if (episodes.indexOf(series.getCurrent()) < episodes.size() - 1) {
			int pos = episodes.indexOf(series.getCurrent());
			series.getEpisodes().get(pos).setCurrent(false);
			series.getEpisodes().get(pos + 1).setCurrent(true);                  //Not saving since it is just to get up and down

			update();
		}
	}

	/**
	 * decreases the Episode which should get shown in the overview TextArea
	 *
	 * @see Episode
	 */
	@FXML
	private void decEpisode() {
		List<Episode> episodes = series.getEpisodes();
		if (series.getCurrent().getSeason() != 1 || series.getCurrent().getEpNumberOfSeason() != 1) {
			int pos = episodes.indexOf(series.getCurrent());
			series.getEpisodes().get(pos).setCurrent(false);
			series.getEpisodes().get(pos - 1).setCurrent(true);                 //Not saving since it is just to get up and down

			update();
		}
	}

	/**
	 * updates the season and episode sign (x.y) and show if the episode is the current one to watch
	 *
	 * @see Episode
	 */
	private void update() {
		labelEpOfSeason.setText(series.getCurrent().getSeason() + "." + series.getCurrent().getEpNumberOfSeason());

		if (tmpSeasonAndEpisodeSign.equals(labelEpOfSeason.getText())) {
			labelIsCurrent.setVisible(true);
		} else {
			labelIsCurrent.setVisible(false);
		}

		overviewEpisode.setText(series.getCurrent().getOverview());
	}

	/**
	 * jumps back to the current episode and shows its content again
	 */
	@FXML
	private void selectCurrentEpisode() {
		List<Episode> allEpisodes = series.getEpisodes();
		for (Episode episode : allEpisodes) {
			if (episode.getSeason() == currentSeason && episode.getEpNumberOfSeason() == currentEpisode) {
				int pos = allEpisodes.indexOf(series.getCurrent());
				series.getEpisodes().get(pos).setCurrent(false);
				episode.setCurrent(true);
			}
		}

		update();
	}

	/**
	 * opens search scene again
	 *
	 * @see Controller
	 * @see SearchController
	 */
	@FXML
	private void back() {
		if (main) {
			backToMain();
		}

		try {
			openSearchFromAdvancedInformation((Stage) backButton.getScene().getWindow(), "/FXML/SearchSeries.fxml", "Search one of your series by attributes", tmpMatches);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) backButton.getScene().getWindow());
		}
	}

	/**
	 * opens main menu scene
	 *
	 * @see Controller
	 * @see MainSeriesController
	 */
	@FXML
	private void backToMain() {
		try {
			openScene((Stage) backMenuButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) backMenuButton.getScene().getWindow());
		}
	}

}
