package SceneController;

import Data.BackUp;
import Data.Episode;
import Data.MySeries;
import Data.Settings;
import Dialog.PopUp;
import TVDB.TVDB_Data;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class MainSeriesController extends Controller {

	@FXML
	private MenuBar menuBar;
	@FXML
	private ImageView imageBackground;
	@FXML
	private TableView<MySeries> tableContinueWatching;
	@FXML
	private TableView<MySeries> tableWaiting;
	@FXML
	private TableView<MySeries> tableStartWatching;
	@FXML
	private TableColumn<MySeries, String> columnContinueName;
	@FXML
	private TableColumn<MySeries, Integer> columnContinueSeason;
	@FXML
	private TableColumn<MySeries, Integer> columnContinueEpisode;
	@FXML
	private TableColumn<MySeries, String> columnWaitName;
	@FXML
	private TableColumn<MySeries, Integer> columnWaitSeason;
	@FXML
	private TableColumn<MySeries, Integer> columnWaitEpisode;
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
	private Label labelWaiting;
	@FXML
	private Label labelStarting;
	@FXML
	private ProgressIndicator progressIndicator;

	private BufferedImage bufImg;
	private boolean backgroundSet = false;
	private boolean updating = false;
	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		if (updating) {
			return;
		}

		if (!backgroundSet) {
			setBackground();
			backgroundSet = true;
		}

		setHeaderContrastColor(labelWatching);
		setHeaderContrastColor(labelWaiting);
		setHeaderContrastColor(labelStarting);

		populateTables();
	}

	/*
	 *  sets the background picture of the scene. Images have to be larger than 1124x632 or equal and should be 16:9 for perfect fit.
	 */
	private void setBackground() {
		File backgroundFolder;
		File[] files;

		// get all png/jpg files of background folder
		try {
			// create folder if it does not exist
			backgroundFolder = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/");
			boolean createdDir = backgroundFolder.mkdir();

			// check if there are fitting files
			if (createdDir || Objects.requireNonNull(backgroundFolder.listFiles()).length == 0) {
				throw new Exception();
			}

			files = backgroundFolder.listFiles((dir, name) -> (name.toLowerCase().endsWith(".png") ||
					name.toLowerCase().endsWith(".jpg") ||
					name.toLowerCase().endsWith(".jpeg")));
			if (files == null || files.length == 0) {
				throw new Exception();
			}

			// check if size fits
			List<File> fittingImages = new ArrayList<>();
			for (File image : files) {
				InputStream is = new FileInputStream(image);
				BufferedImage tmpImg = ImageIO.read(is);

				if (tmpImg.getWidth() >= 1124 && tmpImg.getHeight() >= 632) {
					fittingImages.add(image);
				}
			}

			if (fittingImages.isEmpty()) {
				throw new Exception();
			}

			// select a random image
			Random random = new Random();
			InputStream is = new FileInputStream(fittingImages.get(random.nextInt(fittingImages.size())));
			bufImg = ImageIO.read(is);
			imageBackground.setImage(SwingFXUtils.toFXImage(bufImg, null));
		} catch (Exception e) {
			setFallbackImage();
		}
	}

	/*
	 *  uses a non local picture as background if there is no local available
	 */
	private void setFallbackImage() {
		try {
			URL url = new URL("https://i.imgur.com/iJYsAF4.jpg");
			bufImg = ImageIO.read(url);
			File file = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/fallback.jpg");
			ImageIO.write(bufImg, "jpg", file);
			imageBackground.setImage(SwingFXUtils.toFXImage(bufImg, null));
		} catch (IOException e) {
			popUp.showWarning("No background picture found!", "Please make sure that local pictures are in the correct folder or that you are connected to the internet to see a background picture.");
		}
	}

	/*
	 *  set header text color to one with a high contrast
	 */
	private void setHeaderContrastColor(Label label) {
		// get far most left (x) and far most up point (y)
		List<Color> pixels = new ArrayList<>();
		for (int x = (int) label.getLayoutX(); x < (int) (label.getLayoutX() + label.getPrefWidth()); x++) {
			for (int y = (int) label.getLayoutY(); y < (int) (label.getLayoutY() + label.getPrefHeight()); y++) {
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

	/*
	 *  get a contrasting color (hex-code) to a given color
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

	/*
	 *  return the color with the highest contrast to a hue value
	 */
	private Color getHighestContrast(double hue) {
		// modified values
		if (hue > 5 && hue <= 90) {                 // normal 46-90
			return Color.yellow;
		} else if (hue > 90 && hue <= 176) {        // normal 91-135
			return Color.green;
		} else if (hue > 177 && hue <= 225) {       // normal 136-225
			return Color.cyan;
		} else if (hue > 225 && hue <= 239) {       // normal 226-270
			return Color.blue;
		} else if (hue > 239 && hue <= 315) {       // normal 271-315
			return Color.magenta;
		} else {                                    // normal 316-45
			return Color.red;
		}
	}

	/*
	 *  returns a hex representation of a RGB color
	 */
	private String colorToHex(Color color) {
		String hexColor = Integer.toHexString(color.getRGB() & 0xffffff);
		if (hexColor.length() < 6) {
			hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
		}

		return hexColor;
	}

	/*
	 *  populate the tables
	 */
	private void populateTables() {
		ObservableList<MySeries> unstartedSeries = FXCollections.observableArrayList();
		ObservableList<MySeries> watchingSeries = FXCollections.observableArrayList();
		ObservableList<MySeries> waitingSeries = FXCollections.observableArrayList();
		List<MySeries> listEntries = MySeries.readData();

		if (listEntries.isEmpty()) {
			return;
		}

		setCellValueFactories();

		// populate the observable lists for the tables according to the UserState
		for (MySeries listEntry : listEntries) {
			switch (listEntry.getUserState()) {
				case 0:
					unstartedSeries.add(listEntry);
					break;
				case 1:
					watchingSeries.add(listEntry);
					break;
				case 2:
					waitingSeries.add(listEntry);
					break;
				// finished series get ignored
			}
		}

		insertTableData(unstartedSeries, watchingSeries, waitingSeries);
	}

	/*
	 *  set cell values factories for every column
	 */
	private void setCellValueFactories() {
		columnContinueName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnContinueSeason.setCellValueFactory(new PropertyValueFactory<>("currentSeason"));
		columnContinueEpisode.setCellValueFactory(new PropertyValueFactory<>("currentEpisode"));

		columnWaitName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnWaitSeason.setCellValueFactory(new PropertyValueFactory<>("currentSeason"));
		columnWaitEpisode.setCellValueFactory(new PropertyValueFactory<>("currentEpisode"));

		columnStartName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnStartSeasons.setCellValueFactory(new PropertyValueFactory<>("numberOfSeasons"));
	}

	/*
	 *  insert the series data into the cells of the table
	 */
	private void insertTableData(ObservableList<MySeries> unstarted, ObservableList<MySeries> watching, ObservableList<MySeries> waiting) {
		if (!Settings.readData().isSortByName()) {
			// only watching as the others are all 100% or 0%
			watching.sort(Comparator.comparing(MySeries::getCompletionRate));
		}

		tableStartWatching.setItems(unstarted);
		tableContinueWatching.setItems(watching);
		tableWaiting.setItems(waiting);

		// force table update
		tableContinueWatching.getColumns().get(0).setVisible(false);
		tableContinueWatching.getColumns().get(0).setVisible(true);
		tableWaiting.getColumns().get(0).setVisible(false);
		tableWaiting.getColumns().get(0).setVisible(true);
	}

	/*
	 *  reacts to a button click to increase the episode counter of a series
	 */
	@FXML
	private void incEpisodeButton() {
		if (tableContinueWatching.getSelectionModel().getSelectedItem() == null) {
			popUp.showWarning("No series selected!", "Please select a series to increase the episode of.");
			return;
		}

		List<MySeries> allSeries = MySeries.readData();
		for (MySeries series : allSeries) {
			if (!series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
				continue;
			}

			series.getCurrent().setWatched(true);

			if (!series.hasNext()) {
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

		MySeries.writeData(allSeries);
		initialize();
	}

	/*
	 *  increases the episode counter of a series
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

	/*
	 *  reacts to a button click to decrease the episode counter of a series
	 */
	@FXML
	private void decEpisodeButton() {
		if (tableContinueWatching.getSelectionModel().getSelectedItem() == null) {
			popUp.showWarning("No series selected!", "Please select a series to decrease the episode of.");
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

		MySeries.writeData(allSeries);
		initialize();
	}

	/*
	 *  decreases the episode counter of a series
	 */
	private void decEpisode(MySeries series) {
		series.setNewCurrent(series.getCurrent(), false);                      //true = ++ ; false = --
		series.getCurrent().setWatched(false);
	}

	/*
	 *   change user state of a series from not started to watching
	 */
	@FXML
	private void startSeries() {
		if (tableStartWatching.getSelectionModel().getSelectedItem() == null) {
			popUp.showWarning("No series selected!", "Please select a series you want to start watching.");
			return;
		}

		List<MySeries> allSeries = MySeries.readData();
		for (MySeries series : allSeries) {
			if (series.equals(tableStartWatching.getSelectionModel().getSelectedItem())) {
				series.setUserState(1);
				break;
			}
		}

		MySeries.writeData(allSeries);
		initialize();
	}

	/*
	 *	scroll to a series with the same first char as the button pressed for all tables
	 */
	@FXML
	private void scrollToKeyWatching(KeyEvent key) {
		scrollToSeries(tableContinueWatching, key);
	}

	@FXML
	private void scrollToKeyWaiting(KeyEvent key) {
		scrollToSeries(tableWaiting, key);
	}

	@FXML
	private void scrollToKeyNotStarted(KeyEvent key) {
		scrollToSeries(tableStartWatching, key);
	}

	/*
	 *	scroll down to the first series with the first character that just got pressed
	 */
	private void scrollToSeries(TableView<MySeries> table, KeyEvent key) {
		if(key.getText().length() == 0) {
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

	/*
	 *	change the sorting behaviour to "by Name"
	 */
	@FXML
	private void sortByName() {
		setSortingBehaviour(true);
	}

	/*
	 *	change the sorting behaviour to "by Completion"
	 */
	@FXML
	private void sortByCompletion() {
		setSortingBehaviour(false);
	}

	/*
	 *	change the sorting behaviour in the settings
	 */
	private void setSortingBehaviour(boolean sortBehaviour) {
		Settings settings = Settings.readData();

		if (settings.isSortByName() == sortBehaviour) {
			return;
		}

		settings.setSortByName(sortBehaviour);
		Settings.writeData(settings);

		initialize();
	}

	/*
	 * 	clears selections on other tables
	 */
	@FXML
	private void clickOnTableWatching() {
		tableWaiting.getSelectionModel().clearSelection();
		tableStartWatching.getSelectionModel().clearSelection();
	}

	/*
	 * 	clears selections on other tables
	 */
	@FXML
	private void clickOnTableWaiting() {
		tableContinueWatching.getSelectionModel().clearSelection();
		tableStartWatching.getSelectionModel().clearSelection();
	}

	/*
	 * 	clears selections on other tables
	 */
	@FXML
	private void clickOnTableUnstarted() {
		tableContinueWatching.getSelectionModel().clearSelection();
		tableWaiting.getSelectionModel().clearSelection();
	}

	/*
	 *  import a local backup
	 */
	@FXML
	private void importBackUp() {
		boolean choice = popUp.showChoice("Import BackUp", "Are you sure you want to import the BackUp?");
		if (!choice) {
			return;
		}

		BackUp backUp = BackUp.readBackUp();
		if (backUp == null) {
			popUp.showError("Failed to read BackUp!", "There was an error while trying to read the BackUp.", false);
			return;
		}

		List<MySeries> series = backUp.getSeries();
		MySeries.writeData(series);
		initialize();
	}

	/*
	 *  create a new local backup
	 */
	@FXML
	private void createBackUp() {
		boolean choice = popUp.showChoice("Create BackUp", "Are you sure you want to create a new BackUp?");
		if (!choice) {
			return;
		}

		BackUp backUp = new BackUp();
		BackUp.writeBackUp(backUp);
		popUp.showAlert("BackUp created", "Created a new BackUp on your drive.", false);

	}

	/*
	 *  opens HowTo in a browser
	 */
	@FXML
	private void showHowTo() {
		String url = "https://github.com/Kraisie/SeriesTracker/blob/master/README.md";
		String os = System.getProperty("os.name").toLowerCase();
		Runtime runtime = Runtime.getRuntime();

		try {
			// Windows
			if (os.contains("win")) {
				runtime.exec("rundll32 url.dll, FileProtocolHandler" + url);
				return;
			}

			// Linux
			if (os.contains("nux") || os.contains("nix")) {
				String[] browsers = {"firefox", "google-chrome", "chromium-browser", "opera", "epiphany", "mozilla", "netscape", "konqueror"};
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++) {
					if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
						browser = browsers[count];
					}
				}

				if (browser == null) {
					throw new Exception();
				}

				Runtime.getRuntime().exec(new String[]{browser, url});
				return;
			}

			// Mac
			if (os.contains("mac")) {
				runtime.exec("open " + url);
			}
		} catch (Exception e) {
			popUp.showWarning("Can not open browser!", "There is no supported browser installed on your machine.");
		}
	}

	/*
	 *  closes the stage and thus the program
	 */
	@FXML
	private void close() {
		Stage stage = (Stage) menuBar.getScene().getWindow();
		stage.close();
	}

	/*
	 *  display further information about a series
	 */
	@FXML
	private void displayInformation() {
		MySeries selectedSeries = tableContinueWatching.getSelectionModel().getSelectedItem();

		if (selectedSeries == null) {
			selectedSeries = tableWaiting.getSelectionModel().getSelectedItem();
		}

		if (selectedSeries == null) {
			selectedSeries = tableStartWatching.getSelectionModel().getSelectedItem();
		}

		if (selectedSeries == null) {
			popUp.showWarning("No series selected!", "Please select a series to get the fitting information.");
			return;
		}

		try {
			String title = "Information about " + selectedSeries.getName();
			openSceneWithOneParameter((Stage) infoButton.getScene().getWindow(), "/FXML/AdvancedInformation.fxml", title, selectedSeries);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(2);
		}

	}

	/*
	 *  opens a new scene to search for a series by specific parameters
	 */
	@FXML
	private void searchSeries() {
		try {
			String title = "Search one of your series by attributes";
			openSearch((Stage) menuBar.getScene().getWindow(), "/FXML/SearchSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(8);
		}
	}

	/*
	 *  display all finished series
	 */
	@FXML
	private void displayFinishedSeries() {
		try {
			String title = "All finished Series";
			openScene((Stage) buttonFinishedSeries.getScene().getWindow(), "/FXML/FinishedSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(9);
		}
	}

	/*
	 *  add a new series to the tracker
	 */
	@FXML
	private void addSeries() {
		try {
			String title = "Add a new series";
			openScene((Stage) menuBar.getScene().getWindow(), "/FXML/AddSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(10);
		}
	}

	/*
	 *  delete a series from the tracker
	 */
	@FXML
	private void deleteSeries() {
		try {
			String title = "Delete series";
			openScene((Stage) menuBar.getScene().getWindow(), "/FXML/DeleteSeries.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(11);
		}
	}

	/*
	 *  open the settings screen
	 */
	@FXML
	private void showSettings() {
		try {
			String title = "Settings";
			openScene((Stage) menuBar.getScene().getWindow(), "/FXML/Settings.fxml", title);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(12);
		}
	}

	/*
	 *  update all series which are not ended
	 */
	@FXML
	private void menuUpdateAll() {
		startUpdate();
		update("Continuing");
		initialize();
	}

	/*
	 *  update all ended series
	 */
	@FXML
	private void menuUpdateEnded() {
		startUpdate();
		update("Ended");
		initialize();
	}

	/*
	 *   disable everything to prohibit changing the data while updating
	 */
	private void startUpdate() {
		progressIndicator.setVisible(true);
		buttonIncEpisode.setDisable(true);
		buttonDecEpisode.setDisable(true);
		buttonStartedSeries.setDisable(true);
		infoButton.setDisable(true);
		buttonFinishedSeries.setDisable(true);
		menuBar.setDisable(true);
	}

	/*
	 *  reactivate the UI features after the update
	 */
	private void finishedUpdate() {
		progressIndicator.setVisible(false);
		buttonIncEpisode.setDisable(false);
		buttonDecEpisode.setDisable(false);
		buttonStartedSeries.setDisable(false);
		infoButton.setDisable(false);
		buttonFinishedSeries.setDisable(false);
		menuBar.setDisable(false);
	}

	/*
	 *  update the series according to the selected mode in a second thread
	 */
	private void update(String mode) {
		List<MySeries> allSeries = MySeries.readData();
		List<MySeries> updatedAllSeries = new ArrayList<>();
		updating = true;

		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() {
				for (MySeries series : allSeries) {
					if (!series.getStatus().equals(mode)) {
						updatedAllSeries.add(series);
						continue;
					}

					TVDB_Data data = new TVDB_Data();
					MySeries updatedSeries = data.getUpdate(series.getTvdbID(), series.getUserState(), series.getCurrentSeason(), series.getCurrentEpisode());
					if (updatedSeries == null) {
						System.out.println(series.getName());
						updatedAllSeries.add(series);
						continue;
					}

					Episode.sort(updatedSeries.getEpisodes());
					updatedSeries.setCurrent(series.getCurrent());
					updatedAllSeries.add(updatedSeries);
					updateProgress(updatedAllSeries.size(), allSeries.size());
				}

				MySeries.writeData(updatedAllSeries);

				//TODO: this thread can't access the JavaFX/PopUp stuff so any alerts won't get shown to the user!

				// recheck dates as they may updated
				MySeries.checkAirDates();
				// update tables
				initialize();

				finishedUpdate();
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
