package Kraisie.SceneController;

import Kraisie.Data.BackUp;
import Kraisie.Data.Episode;
import Kraisie.Data.MySeries;
import Kraisie.Data.Settings;
import Kraisie.Dialog.PopUp;
import Kraisie.TVDB.APIKey;
import Kraisie.TVDB.TVDB_Data;
import Kraisie.UI.BackgroundManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static Kraisie.Dialog.BrowserControl.openBrowser;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class MainSeriesController extends Controller {

	@FXML
	private BorderPane borderPane;
	@FXML
	private MenuBar menuBar;
	@FXML
	private TableView<MySeries> tableContinueWatching;
	@FXML
	private TableView<MySeries> tableStartWatching;
	@FXML
	private TableColumn<MySeries, String> columnContinueName;
	@FXML
	private TableColumn<MySeries, Integer> columnContinueSeason;
	@FXML
	private TableColumn<MySeries, Integer> columnContinueEpisode;
	@FXML
	private TableColumn<MySeries, String> columnStartName;
	@FXML
	private TableColumn<MySeries, Integer> columnStartSeasons;
	@FXML
	private Button waitButton;
	@FXML
	private Button buttonIncEpisode;
	@FXML
	private Button buttonDecEpisode;
	@FXML
	private Button buttonFinishedSeries;
	@FXML
	private Button buttonStartedSeries;
	@FXML
	private Label labelWatching;
	@FXML
	private Label labelStarting;
	@FXML
	private ProgressBar progressIndicator;

	private boolean updating = false;
	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		if (updating) {
			return;
		}

		populateTables();
	}

	/**
	 * initializes the background of the main scene
	 */
	public void initBackground() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		int width = (int) borderPane.getWidth();
		int height = (int) borderPane.getHeight();
		setBackground(width, height);

		if (borderPane.getBackground().getImages().size() > 0) {
			setHeaderContrastColor(labelWatching);
			setHeaderContrastColor(labelStarting);
		}

		// event on resize to update background pictures
		stage.widthProperty().addListener((obs, oldVal, newVal) -> updateBackgroundSize());
		stage.heightProperty().addListener((obs, oldVal, newVal) -> updateBackgroundSize());
	}

	/**
	 * sets the background picture of the scene. Images have to be larger than 1280x720 or equal and should be 16:9 for perfect fit.
	 *
	 * @param w width of the borderPane
	 * @param h height of the borderPane
	 */
	private void setBackground(int w, int h) {
		BufferedImage bufImg = BackgroundManager.getRandomImage();
		if (bufImg == null) {
			popUp.showWarning("No background picture found!", "Please make sure that local pictures are in the correct folder or that you are connected to the internet to see a background picture.", (Stage) borderPane.getScene().getWindow());
			return;
		}

		BackgroundManager bm = new BackgroundManager(bufImg);
		Background background = bm.getFittingBackground(w, h);
		borderPane.setBackground(background);

	}

	/**
	 * updates the image on resize of the scene
	 */
	private void updateBackgroundSize() {
		Image img = borderPane.getBackground().getImages().get(0).getImage();
		BackgroundManager bm = new BackgroundManager(SwingFXUtils.fromFXImage(img, null));

		int w = (int) borderPane.getWidth();
		int h = (int) borderPane.getHeight();
		Background background = bm.getFittingBackground(w, h);
		borderPane.setBackground(background);
	}

	/**
	 * sets header text color to one with a high contrast to the background of the label
	 *
	 * @param label label that needs a high contrast text color to its background
	 */
	private void setHeaderContrastColor(Label label) {
		Image img = borderPane.getBackground().getImages().get(0).getImage();
		BufferedImage bufImg = SwingFXUtils.fromFXImage(img, null);

		BackgroundManager bm = new BackgroundManager(bufImg);
		String hexContrastColor = bm.getContrastColor(label);
		label.setTextFill(javafx.scene.paint.Color.web(hexContrastColor));
	}

	/**
	 * populates the tables with data
	 */
	private void populateTables() {
		ObservableList<MySeries> unstartedSeries = FXCollections.observableArrayList();
		ObservableList<MySeries> watchingSeries = FXCollections.observableArrayList();
		List<MySeries> listEntries = MySeries.readData();

		if (listEntries.isEmpty()) {
			return;
		}

		setCellValueFactories();
		setColumnWidth();

		// populate the observable lists for the tables according to the userState
		for (MySeries listEntry : listEntries) {
			switch (listEntry.getUserState()) {
				case 0:
					unstartedSeries.add(listEntry);
					break;
				case 1:
					watchingSeries.add(listEntry);
					break;
				default:
					// waiting and finished series get ignored
			}
		}

		insertTableData(unstartedSeries, watchingSeries);

		/*
		Force table update as increasing or decreasing the episodes
		does not seem to trigger an UI update even when calling initialize.
		Thus we need to refresh the table manually.
		 */
		tableContinueWatching.refresh();
	}

	/**
	 * sets cell values factories for every column
	 */
	private void setCellValueFactories() {
		columnContinueName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnContinueSeason.setCellValueFactory(new PropertyValueFactory<>("currentSeason"));
		columnContinueEpisode.setCellValueFactory(new PropertyValueFactory<>("currentEpisode"));

		columnStartName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnStartSeasons.setCellValueFactory(new PropertyValueFactory<>("numberOfSeasons"));
	}

	/**
	 * sets the column width for every column
	 */
	private void setColumnWidth() {
		columnContinueName.prefWidthProperty().bind(tableContinueWatching.widthProperty().divide(2));
		columnContinueSeason.prefWidthProperty().bind(tableContinueWatching.widthProperty().divide(4));
		columnContinueEpisode.prefWidthProperty().bind(tableContinueWatching.widthProperty().divide(4));

		columnStartName.prefWidthProperty().bind(tableStartWatching.widthProperty().divide(2));
		columnStartSeasons.prefWidthProperty().bind(tableStartWatching.widthProperty().divide(2));
	}

	/**
	 * inserts the series data into the cells of the table
	 *
	 * @param unstarted ObservableList of all series which the user did not start yet
	 * @param watching  ObservableList of all series which the user is currently watching
	 */
	private void insertTableData(ObservableList<MySeries> unstarted, ObservableList<MySeries> watching) {
		Settings settings = Settings.readData();
		if (settings.isSortByCompletion()) {
			// only watching as the others are all 100% or 0%
			watching.sort(Comparator.comparing(MySeries::getCompletionRate).reversed());
		}

		if (settings.isSortByTime()) {
			watching.sort(Comparator.comparing(MySeries::getTimeToEnd));
			unstarted.sort(Comparator.comparing(MySeries::getTimeToEnd));
		}

		tableStartWatching.setItems(unstarted);
		tableContinueWatching.setItems(watching);
	}

	/**
	 * reacts to a button click to increase the episode counter of a series
	 */
	@FXML
	private void incEpisodeButton() {
		if (tableContinueWatching.getSelectionModel().getSelectedItem() == null) {
			popUp.showWarning("No series selected!", "Please select a series to increase the episode of.", (Stage) buttonIncEpisode.getScene().getWindow());
			return;
		}

		List<MySeries> allSeries = MySeries.readData();
		for (MySeries series : allSeries) {
			if (!series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
				continue;
			}

			series.getCurrent().setWatched(true);

			if (series.hasNoNextEp()) {
				if (series.getStatus().equals("Ended")) {
					series.setUserState(3);
					break;
				} else {
					series.setUserState(2);
					break;
				}
			}

			incEpisode(series);
			break;
		}

		try {
			MySeries.writeData(allSeries);
		} catch (IOException e) {
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) buttonIncEpisode.getScene().getWindow());
		}
		initialize();
	}

	/**
	 * increases the episode counter of a series
	 */
	private void incEpisode(MySeries series) {
		int index = series.getEpisodes().indexOf(series.getCurrent());
		// if next episodes isn't aired yet set the userstate to 'waiting'
		if (series.getEpisodes().get(index + 1).getFirstAired().equals("Not given!")) {
			series.setUserState(2);
			return;
		}

		// check if episode already aired
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(series.getEpisodes().get(index + 1).getFirstAired(), formatter);

		if (date.isAfter(LocalDate.now())) {
			series.setUserState(2);
			return;
		}

		// next episode has already aired
		series.setNewCurrent(series.getCurrent(), true);            //true = ++ ; false = --
	}

	/**
	 * reacts to a button click to decrease the episode counter of a series
	 */
	@FXML
	private void decEpisodeButton() {
		if (tableContinueWatching.getSelectionModel().getSelectedItem() == null) {
			popUp.showWarning("No series selected!", "Please select a series to decrease the episode of.", (Stage) buttonDecEpisode.getScene().getWindow());
			return;
		}

		List<MySeries> allSeries = MySeries.readData();
		for (MySeries series : allSeries) {
			if (!series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
				continue;
			}

			// prohibit to decrease below 1.1
			if (series.getCurrent().getSeason() == 1 && series.getCurrent().getEpNumberOfSeason() == 1) {
				break;
			}

			decEpisode(series);
			break;
		}

		try {
			MySeries.writeData(allSeries);
		} catch (IOException e) {
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) buttonDecEpisode.getScene().getWindow());
		}
		initialize();
	}

	/**
	 * decreases the episode counter of a series
	 *
	 * @param series the selected series to decrease the Episode counter of
	 * @see Episode
	 */
	private void decEpisode(MySeries series) {
		series.setNewCurrent(series.getCurrent(), false);                      //true = ++ ; false = --
		series.getCurrent().setWatched(false);
	}

	/**
	 * changes user state of a selected series from not started to watching
	 */
	@FXML
	private void startSeries() {
		if (tableStartWatching.getSelectionModel().getSelectedItem() == null) {
			popUp.showWarning("No series selected!", "Please select a series you want to start watching.", (Stage) buttonStartedSeries.getScene().getWindow());
			return;
		}

		List<MySeries> allSeries = MySeries.readData();
		for (MySeries series : allSeries) {
			if (series.equals(tableStartWatching.getSelectionModel().getSelectedItem())) {
				series.setUserState(1);
				break;
			}
		}

		try {
			MySeries.writeData(allSeries);
		} catch (IOException e) {
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) buttonStartedSeries.getScene().getWindow());
		}
		initialize();
	}

	/**
	 * scrolls to a series with the same first char as the button pressed for all tables
	 */
	@FXML
	private void scrollToKeyWatching(KeyEvent key) {
		scrollToSeries(tableContinueWatching, key);
	}

	/**
	 * scrolls to a series with the same first char as the button pressed for all tables
	 */
	@FXML
	private void scrollToKeyNotStarted(KeyEvent key) {
		scrollToSeries(tableStartWatching, key);
	}

	/**
	 * changes the sorting behaviour to "by Name"
	 */
	@FXML
	private void sortByName() {
		setSortingBehaviour("name");
	}

	/**
	 * changes the sorting behaviour to "by Completion"
	 */
	@FXML
	private void sortByCompletion() {
		setSortingBehaviour("comp");
	}

	/**
	 * changes the sorting behaviour to "by less needed time"
	 */
	@FXML
	private void sortByTime() {
		setSortingBehaviour("time");
	}

	/**
	 * changes the sorting behaviour to 'by name', 'by completion rate' or by 'less needed time'
	 *
	 * @param mode the mode to sort by
	 */
	private void setSortingBehaviour(String mode) {
		Settings settings = Settings.readData();

		switch (mode) {
			case "comp":
				settings.setSortByCompletion(true);
				settings.setSortByTime(false);
				break;
			case "time":
				settings.setSortByCompletion(false);
				settings.setSortByTime(true);
				break;
			case "name":
			default:
				settings.setSortByCompletion(false);
				settings.setSortByTime(false);
		}

		try {
			Settings.writeData(settings);
		} catch (IOException e) {
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) menuBar.getScene().getWindow());
		}
		initialize();
	}

	/**
	 * gets called when a series gets selected in tableContinueWatching, clears selections on other tables
	 */
	@FXML
	private void clickOnTableWatching() {
		tableStartWatching.getSelectionModel().clearSelection();
	}

	/**
	 * gets called when a series gets selected in tableStartWatching, clears selections on other tables
	 */
	@FXML
	private void clickOnTableUnstarted() {
		tableContinueWatching.getSelectionModel().clearSelection();
	}

	/**
	 * imports BackUp from the drive
	 *
	 * @see BackUp
	 */
	@FXML
	private void importBackUp() {
		boolean choice = popUp.showChoice("Import BackUp", "Are you sure you want to import the BackUp?", (Stage) menuBar.getScene().getWindow());
		if (!choice) {
			return;
		}

		BackUp backUp = BackUp.readBackUp();
		if (backUp == null) {
			popUp.showError("Failed to read BackUp!", "Please check the validity of your path.", false, (Stage) menuBar.getScene().getWindow());
			return;
		}

		try {
			List<MySeries> series = backUp.getSeries();
			MySeries.writeData(series);
		} catch (IOException e) {
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) menuBar.getScene().getWindow());
		}
		initialize();
	}

	/**
	 * creates a new BackUp on the drive
	 *
	 * @see BackUp
	 */
	@FXML
	private void createBackUp() {
		boolean choice = popUp.showChoice("Create BackUp", "Are you sure you want to create a new BackUp?", (Stage) menuBar.getScene().getWindow());
		if (!choice) {
			return;
		}

		try {
			BackUp backUp = new BackUp();
			BackUp.writeBackUp(backUp);
		} catch (IOException e) {
			popUp.showError("BackUp failed!", "The BackUp failed. Please check the validity of your Path.", false, (Stage) menuBar.getScene().getWindow());
			return;
		}
		popUp.showAlert("BackUp created", "Created a new BackUp on your drive.", false, (Stage) menuBar.getScene().getWindow());

	}

	/**
	 * opens HowTo in a browser
	 */
	@FXML
	private void showHowTo() {
		try {
			String url = "https://github.com/Kraisie/SeriesTracker/blob/master/README.md";
			openBrowser(url);
		} catch (Exception e) {
			popUp.showWarning("Can not open browser!", "There is no supported browser installed on your machine.", (Stage) menuBar.getScene().getWindow());
		}
	}

	/**
	 * restores the default resolution of 1280x720
	 */
	@FXML
	private void restoreDefaultResolution() {
		Stage stage = (Stage) borderPane.getScene().getWindow();

		// calculate difference between BorderPane w/h to Window w/h to get the size of the window border
		double borderWidth = stage.getWidth() - borderPane.getWidth();
		double borderHeight = stage.getHeight() - borderPane.getHeight();

		stage.setWidth(1280d + borderWidth);
		stage.setHeight(720d + borderHeight);
		borderPane.resize(1280d, 720d);
		updateBackgroundSize();
	}

	/**
	 * closes the stage and thus the program itself
	 */
	@FXML
	private void close() {
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.close();
	}

	/**
	 * opens information scene for a selected series
	 *
	 * @see Controller
	 * @see AdvancedInformationController
	 */
	@FXML
	private void displayInformation() {
		MySeries selectedSeries = tableContinueWatching.getSelectionModel().getSelectedItem();

		if (selectedSeries == null) {
			selectedSeries = tableStartWatching.getSelectionModel().getSelectedItem();
		}

		if (selectedSeries == null) {
			popUp.showWarning("No series selected!", "Please select a series to get the fitting information.", (Stage) tableStartWatching.getScene().getWindow());
			return;
		}

		try {
			String title = "Information about " + selectedSeries.getName();
			openSceneWithOneParameter((Stage) tableStartWatching.getScene().getWindow(), "/FXML/AdvancedInformation.fxml", title, selectedSeries);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) tableStartWatching.getScene().getWindow());
		}
	}

	/**
	 * deletes a selected series after verification in PopUp
	 */
	@FXML
	private void directDeleteSeries() {
		MySeries selectedSeries = tableContinueWatching.getSelectionModel().getSelectedItem();

		if (selectedSeries == null) {
			selectedSeries = tableStartWatching.getSelectionModel().getSelectedItem();
		}

		if (selectedSeries == null) {
			popUp.showWarning("No series selected!", "Please select a series to get the fitting information.", (Stage) tableStartWatching.getScene().getWindow());
			return;
		}

		boolean verify = popUp.showChoice(
				"Do you want to delete the series?",
				"This will delete the series from your list, all progress will be lost and can not be recovered!",
				(Stage) tableStartWatching.getScene().getWindow()
		);

		if (verify) {
			List<MySeries> allSeries = MySeries.readData();
			allSeries.remove(selectedSeries);
			try {
				MySeries.writeData(allSeries);
			} catch (IOException e) {
				popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) tableStartWatching.getScene().getWindow());
			}
			initialize();
		}
	}

	@FXML
	private void displayWaiting() {
		try {
			openScene((Stage) waitButton.getScene().getWindow(), "/FXML/WaitingSeries.fxml", "Series with not aired episodes");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) waitButton.getScene().getWindow());
		}
	}

	/**
	 * opens search scene
	 *
	 * @see Controller
	 * @see SearchController
	 */
	@FXML
	private void searchSeries() {
		try {
			String title = "Search one of your series by attributes";
			openSearch((Stage) menuBar.getScene().getWindow(), "/FXML/SearchSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) menuBar.getScene().getWindow());
		}
	}

	/**
	 * opens scene with a list of all series the user finished
	 *
	 * @see Controller
	 * @see FinishedController
	 */
	@FXML
	private void displayFinishedSeries() {
		try {
			String title = "All finished Series";
			openScene((Stage) buttonFinishedSeries.getScene().getWindow(), "/FXML/FinishedSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) buttonFinishedSeries.getScene().getWindow());
		}
	}

	/**
	 * opens scene to add series
	 *
	 * @see Controller
	 * @see AddSeriesController
	 */
	@FXML
	private void addSeries() {
		try {
			String title = "Add a new series";
			openScene((Stage) menuBar.getScene().getWindow(), "/FXML/AddSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) menuBar.getScene().getWindow());
		}
	}

	/**
	 * opens PopUp with information about the program
	 *
	 * @see PopUp
	 */
	@FXML
	public void showAbout() {
		popUp.showAbout((Stage) menuBar.getScene().getWindow());
	}

	/**
	 * opens settings scene
	 *
	 * @see Controller
	 * @see SettingsController
	 */
	@FXML
	private void showSettings() {
		try {
			String title = "Settings";
			openScene((Stage) menuBar.getScene().getWindow(), "/FXML/Settings.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) menuBar.getScene().getWindow());
		}
	}

	/**
	 * updates all series
	 *
	 * @see MySeries
	 */
	@FXML
	private void menuUpdateAll() {
		uiOnUpdate(true);
		update("Continuing");
		initialize();
	}

	/**
	 * only updates the series with status 'Ended'
	 *
	 * @see MySeries
	 */
	@FXML
	private void menuUpdateEnded() {
		uiOnUpdate(true);
		update("Ended");
		initialize();
	}

	/**
	 * enables and disables UI features while updating to prevent data loss
	 *
	 * @param updateRunning true to disable
	 */
	private void uiOnUpdate(boolean updateRunning) {
		progressIndicator.setVisible(updateRunning);
		buttonIncEpisode.setDisable(updateRunning);
		buttonDecEpisode.setDisable(updateRunning);
		buttonStartedSeries.setDisable(updateRunning);
		waitButton.setDisable(updateRunning);
		buttonFinishedSeries.setDisable(updateRunning);
		menuBar.setDisable(updateRunning);
	}

	/**
	 * updates the locally saved series with data from theTVDB API in a second thread
	 *
	 * @param mode only updates series that have that status
	 * @see MySeries
	 */
	private void update(String mode) {
		List<MySeries> allSeries = MySeries.readData();
		List<MySeries> updatedAllSeries = new ArrayList<>();
		updating = true;

		final Task<Void> task = new Task<>() {
			@Override
			protected Void call() {
				for (MySeries series : allSeries) {
					if (!series.getStatus().equals(mode)) {
						updatedAllSeries.add(series);
						continue;
					}

					TVDB_Data data = new TVDB_Data(APIKey.readKey());
					MySeries updatedSeries = data.getUpdate(series.getTvdbID(), series.getUserState(), series.getCurrentSeason(), series.getCurrentEpisode());
					if (updatedSeries == null) {
						updatedAllSeries.add(series);
						continue;
					}

					Episode.sort(updatedSeries.getEpisodes());
					updatedSeries.setCurrent(series.getCurrent());
					updatedAllSeries.add(updatedSeries);
					updateProgress(updatedAllSeries.size(), allSeries.size());
				}

				try {
					MySeries.writeData(updatedAllSeries);
				} catch (IOException e) {
					popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) menuBar.getScene().getWindow());
				}

				//TODO: this thread can't access the JavaFX/PopUp stuff so any alerts won't get shown to the user!

				// recheck dates as they may updated
				MySeries.checkAirDates();
				// update tables
				initialize();

				uiOnUpdate(false);
				updating = false;
				return null;
			}
		};

		progressIndicator.progressProperty().unbind();
		progressIndicator.progressProperty().bind(task.progressProperty());

		final Thread thread = new Thread(task, "task-thread");
		thread.setDaemon(true);
		thread.start();
	}
}
