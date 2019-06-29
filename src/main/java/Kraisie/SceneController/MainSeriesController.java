package Kraisie.SceneController;

import Kraisie.Data.BackUp;
import Kraisie.Data.Episode;
import Kraisie.Data.MySeries;
import Kraisie.Data.Settings;
import Kraisie.Dialog.PopUp;
import Kraisie.TVDB.APIKey;
import Kraisie.TVDB.TVDB_Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

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
	private Button infoButton;
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
		int width = (int) borderPane.getScene().getWidth();
		int height = (int) borderPane.getScene().getHeight();
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
		File backgroundFolder;
		File[] files;

		// get all png/jpg files of background folder
		try {
			// create folder if it does not exist
			backgroundFolder = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/");
			boolean createdDir = backgroundFolder.mkdir();

			// check if there are fitting files
			if (createdDir) {
				throw new Exception();
			}

			files = backgroundFolder.listFiles((dir, name) -> (
					name.toLowerCase().endsWith(".png") ||
							name.toLowerCase().endsWith(".jpg") ||
							name.toLowerCase().endsWith(".jpeg")
			));

			if (files == null || files.length == 0) {
				throw new Exception();
			}

			// select a random image
			Random random = new Random();
			InputStream is = new FileInputStream(files[random.nextInt(files.length)]);
			BufferedImage bufImg = ImageIO.read(is);

			setBorderPaneBackground(SwingFXUtils.toFXImage(bufImg, null), w, h);
		} catch (Exception e) {
			setFallbackImage(w, h);
		}
	}

	/**
	 * uses a non local picture as background if there is no local available
	 *
	 * @param w width of the borderPane
	 * @param h height of the borderPane
	 */
	private void setFallbackImage(int w, int h) {
		try {
			URL url = new URL("https://i.imgur.com/iJYsAF4.jpg");
			BufferedImage bufImg = ImageIO.read(url);
			File file = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/fallback.jpg");
			ImageIO.write(bufImg, "jpg", file);
			setBorderPaneBackground(SwingFXUtils.toFXImage(bufImg, null), w, h);
		} catch (IOException e) {
			popUp.showWarning("No background picture found!", "Please make sure that local pictures are in the correct folder or that you are connected to the internet to see a background picture.", (Stage) borderPane.getScene().getWindow());
		}
	}

	/**
	 * sets an image as background of the borderPane
	 *
	 * @param img the image that shall get set as background
	 * @param w   width of the borderPane
	 * @param h   height of the borderPane
	 */
	private void setBorderPaneBackground(Image img, int w, int h) {

		BackgroundSize bSize = new BackgroundSize(w, h, false, false, false, false);
		Background background = new Background(
				new BackgroundImage(
						img,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						bSize
				)
		);

		borderPane.setBackground(background);
	}

	/**
	 * updates the image on resize of the scene
	 */
	private void updateBackgroundSize() {
		Image img = borderPane.getBackground().getImages().get(0).getImage();
		int w = (int) borderPane.getScene().getWidth();
		int h = (int) borderPane.getScene().getHeight();
		setBorderPaneBackground(img, w, h);
	}

	/**
	 * sets header text color to one with a high contrast to the background of the label
	 *
	 * @param label label that needs a high contrast text color to its background
	 */
	private void setHeaderContrastColor(Label label) {
		// get far most left (x) and far most up point (y)
		Image img = borderPane.getBackground().getImages().get(0).getImage();
		BufferedImage bufImg = SwingFXUtils.fromFXImage(img, null);
		List<Color> pixels = new ArrayList<>();

		for (int x = (int) label.getLayoutX(); x < (int) (label.getLayoutX() + label.getWidth()); x++) {
			for (int y = (int) label.getLayoutY(); y < (int) (label.getLayoutY() + label.getHeight()); y++) {
				pixels.add(new Color(bufImg.getRGB(x, y)));
			}
		}

		// get average RGB of the label background
		int avgRed = 0, avgGreen = 0, avgBlue = 0;
		for (Color pixel : pixels) {
			avgRed += pixel.getRed();
			avgGreen += pixel.getGreen();
			avgBlue += pixel.getBlue();
		}

		Color avgColor = new Color(avgRed / pixels.size(), avgGreen / pixels.size(), avgBlue / pixels.size());
		String hexContrastColor = getContrastColor(avgColor);
		label.setTextFill(javafx.scene.paint.Color.web(hexContrastColor));
	}

	/**
	 * @param color background color
	 * @return a high contrast color for background color
	 */
	private String getContrastColor(Color color) {
		ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		float[] rgb = {color.getRed(), color.getGreen(), color.getBlue()};
		float[] sRGB = space.fromRGB(rgb);

		// get min and max of sRGB
		List<Float> tempRGBList = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(sRGB)));
		float min = Collections.min(tempRGBList);
		float max = Collections.max(tempRGBList);

		// use hue formula according to max value
		double hue;
		if (max == sRGB[0]) {
			hue = (sRGB[1] - sRGB[2]) / (max - min);
		} else if (max == sRGB[1]) {
			hue = 2 + (sRGB[2] - sRGB[0]) / (max - min);
		} else {
			hue = 4 + (sRGB[0] - sRGB[1]) / (max - min);
		}
		hue = (60 * hue) % 360;

		return "#" + colorToHex(getHighestContrast(hue));
	}

	/**
	 * @param hue the hue to get the best contrast color for
	 * @return color with the highest contrast
	 */
	private Color getHighestContrast(double hue) {
		// modified values
		if (hue > 46 && hue <= 90) {
			return Color.yellow;
		} else if (hue > 90 && hue <= 135) {
			return Color.green;
		} else if (hue > 135 && hue <= 225) {
			return Color.cyan;
		} else if (hue > 225 && hue <= 270) {
			return Color.blue;
		} else if (hue > 270 && hue <= 315) {
			return Color.magenta;
		} else {
			return Color.red;
		}
	}

	/**
	 * @param color a color :)
	 * @return hex representation as a String of a color
	 */
	private String colorToHex(Color color) {
		String hexColor = Integer.toHexString(color.getRGB() & 0xffffff);
		if (hexColor.length() < 6) {
			hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
		}

		return hexColor;
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

		// force table update
		tableContinueWatching.getColumns().get(0).setVisible(false);
		tableContinueWatching.getColumns().get(0).setVisible(true);
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
	 * Scrolls to the first series that starts with the letter that just got pressed.
	 * If the same gets pressed as the first letter of the currently selected series the next series with that first letter gets selected.
	 * Keys like Shift, Enter etc get ignored.
	 *
	 * @param key KeyEvent of the pressed key.
	 */
	private void scrollToSeries(TableView<MySeries> table, KeyEvent key) {
		if (key.getText().length() == 0) {
			return;
		}

		char c = key.getText().charAt(0);
		ObservableList<MySeries> allSeriesInTable = table.getItems();
		MySeries match = null;

		// if the selected series already starts with that char and the next one in the list still got that char then select the next series
		int index = table.getSelectionModel().getSelectedIndex();
		if (index + 1 < allSeriesInTable.size()) {
			MySeries nextSeries = allSeriesInTable.get(index + 1);
			if (c == nextSeries.getName().toLowerCase().charAt(0)) {
				table.getSelectionModel().select(nextSeries);
				return;
			}
		}

		// select first occurrence
		for (MySeries series : allSeriesInTable) {
			if (c == series.getName().toLowerCase().charAt(0)) {
				match = series;
				break;
			}
		}

		if (match == null) {
			return;
		}

		table.getSelectionModel().select(match);
		table.scrollTo(match);
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
			popUp.showWarning("No series selected!", "Please select a series to get the fitting information.", (Stage) infoButton.getScene().getWindow());
			return;
		}

		try {
			String title = "Information about " + selectedSeries.getName();
			openSceneWithOneParameter((Stage) infoButton.getScene().getWindow(), "/FXML/AdvancedInformation.fxml", title, selectedSeries);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) infoButton.getScene().getWindow());
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
	 * opens scene to delete series
	 *
	 * @see Controller
	 * @see DeleteController
	 */
	@FXML
	private void deleteSeries() {
		try {
			String title = "Delete series";
			openScene((Stage) menuBar.getScene().getWindow(), "/FXML/DeleteSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) menuBar.getScene().getWindow());
		}
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
		infoButton.setDisable(updateRunning);
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
