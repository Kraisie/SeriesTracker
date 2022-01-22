package kraisie.scenes;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kraisie.data.Collection;
import kraisie.data.DataSingleton;
import kraisie.data.Series;
import kraisie.data.definitions.Scenes;
import kraisie.ui.SceneLoader;

public class MainSeriesController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private Label leftHeader;

	@FXML
	private Label rightHeader;

	@FXML
	private TableView<Series> leftTable;

	@FXML
	private TableColumn<Series, String> columnNameLeft;

	@FXML
	private TableColumn<Series, String> columnCurrentSeason;

	@FXML
	private TableColumn<Series, String> columnCurrentEpisode;

	@FXML
	private TableView<Series> rightTable;

	@FXML
	private TableColumn<Series, String> columnNameRight;

	@FXML
	private TableColumn<Series, String> columnSeasons;

	@FXML
	private Button plusEpisodeButton;

	@FXML
	private Button minusEpisodeButton;

	@FXML
	private Button startSeriesButton;

	@FXML
	private Button displaySwitchButton;

	private Collection collection;

	private boolean displayState = false;

	@FXML
	private void initialize() {
		DataSingleton data = DataSingleton.getInstance();
		collection = data.getCollection();
		loadTables();
	}

	private void updateScene() {
		loadTables();
	}

	private void loadTables() {
		setTableProperties();
		if (displayState) {
			populateTablesAlternate();
		} else {
			populateTablesDefault();
		}
	}

	private void setTableProperties() {
		setCellValueFactories();
		setColumnWidth();
	}

	private void setCellValueFactories() {
		columnNameLeft.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnCurrentSeason.setCellValueFactory(new PropertyValueFactory<>("currentSeason"));
		columnCurrentEpisode.setCellValueFactory(new PropertyValueFactory<>("currentEpisode"));

		columnNameRight.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnSeasons.setCellValueFactory(new PropertyValueFactory<>("numberOfSeasons"));
	}

	private void setColumnWidth() {
		columnNameLeft.prefWidthProperty().bind(leftTable.widthProperty().divide(2));
		columnCurrentSeason.prefWidthProperty().bind(leftTable.widthProperty().divide(4));
		columnCurrentEpisode.prefWidthProperty().bind(leftTable.widthProperty().divide(4));

		columnNameRight.prefWidthProperty().bind(rightTable.widthProperty().divide(1.5));
		columnSeasons.prefWidthProperty().bind(rightTable.widthProperty().divide(3));
	}

	private void populateTablesDefault() {
		ObservableList<Series> started = collection.getObservableStarted();
		ObservableList<Series> unstarted = collection.getObservableUnstarted();

		leftTable.setItems(started);
		rightTable.setItems(unstarted);

		leftTable.refresh();
		rightTable.refresh();
	}

	private void populateTablesAlternate() {
		ObservableList<Series> waiting = collection.getObservableWaiting();
		ObservableList<Series> finished = collection.getObservableFinished();

		leftTable.setItems(waiting);
		rightTable.setItems(finished);

		leftTable.refresh();
		rightTable.refresh();
	}

	@FXML
	private void clickOnTableUnstarted() {
		leftTable.getSelectionModel().clearSelection();
	}

	@FXML
	private void clickOnTableWatching() {
		rightTable.getSelectionModel().clearSelection();
	}

	@FXML
	private void decEpisodeButton() {
		Series selectedSeries = leftTable.getSelectionModel().getSelectedItem();
		if (selectedSeries == null) {
			return;
		}

		int index = collection.getSeries().indexOf(selectedSeries);
		Series series = collection.getSeries().get(index);
		series.decreaseWatchProgress();
		updateScene();
	}

	@FXML
	private void deleteSeries() {
		Series series = getSelectedSeries();
		if (series == null) {
			return;
		}

		collection.getSeries().remove(series);
		updateScene();
	}

	private Series getSelectedSeries() {
		Series selected = leftTable.getSelectionModel().getSelectedItem();
		if (selected != null) {
			return selected;
		}

		return rightTable.getSelectionModel().getSelectedItem();
	}

	@FXML
	private void incEpisodeButton() {
		Series selectedSeries = leftTable.getSelectionModel().getSelectedItem();
		if (selectedSeries == null) {
			return;
		}

		int index = collection.getSeries().indexOf(selectedSeries);
		Series series = collection.getSeries().get(index);
		series.increaseWatchProgress();
		updateScene();
	}

	@FXML
	private void scrollToKeyNotStarted(KeyEvent event) {
		scrollToSeries(rightTable, event);
	}

	@FXML
	private void scrollToKeyWatching(KeyEvent event) {
		scrollToSeries(leftTable, event);
	}

	private void scrollToSeries(TableView<Series> table, KeyEvent event) {
		String pressedKey = event.getText();
		if (pressedKey.length() == 0) {
			return;
		}

		Series selectedSeries = table.getSelectionModel().getSelectedItem();
		if (selectedSeries == null) {
			// if no selection search for first with char
			selectFirstMatch(table, pressedKey);
			return;
		}

		// if selected series and the next series have same first char as the key then select the next series
		String selectedSeriesName = selectedSeries.getName().toLowerCase();
		if (matchesFirstCharIgnoreCase(pressedKey, selectedSeriesName)) {
			if (nextSeriesMatches(table, pressedKey)) {
				table.getSelectionModel().selectNext();
				return;
			}
		}

		// selected or next  does not have key char
		selectFirstMatch(table, pressedKey);
	}

	// reliably check the first char ignoring case
	private boolean matchesFirstCharIgnoreCase(String keyChar, String seriesName) {
		int keyCharLength = keyChar.length();
		return seriesName.regionMatches(true, 0, keyChar, 0, keyCharLength);
	}

	private boolean nextSeriesMatches(TableView<Series> table, String keyChar) {
		int selectedIndex = table.getSelectionModel().getSelectedIndex();
		ObservableList<Series> series = table.getItems();
		if (selectedIndex + 1 >= series.size()) {
			return false;
		}

		Series nextSeries = series.get(selectedIndex + 1);
		String nextSeriesName = nextSeries.getName();
		return matchesFirstCharIgnoreCase(keyChar, nextSeriesName);
	}

	private void selectFirstMatch(TableView<Series> table, String pressedKey) {
		ObservableList<Series> series = table.getItems();
		for (Series s : series) {
			if (matchesFirstCharIgnoreCase(pressedKey, s.getName())) {
				table.getSelectionModel().select(s);
				return;
			}
		}
	}

	@FXML
	private void switchDisplay() {
		displayState = !displayState;
		switchTexts();
		disableButtons();
		updateScene();
		resetScroll();
	}

	private void switchTexts() {
		if (displayState) {
			leftHeader.setText("Awaiting new episodes");
			rightHeader.setText("Finished series");
			displaySwitchButton.setText("Started/Unstarted series");
		} else {
			leftHeader.setText("Continue watching");
			rightHeader.setText("Start watching");
			displaySwitchButton.setText("Awaited/Finished series");
		}
	}

	private void disableButtons() {
		plusEpisodeButton.setDisable(!plusEpisodeButton.isDisable());
		minusEpisodeButton.setDisable(!minusEpisodeButton.isDisable());
		startSeriesButton.setDisable(!startSeriesButton.isDisable());
	}

	private void resetScroll() {
		leftTable.scrollTo(0);
		rightTable.scrollTo(0);
	}

	@FXML
	private void showInformation() {
		Series selected = getSelectedSeries();
		if (selected == null) {
			return;
		}

		Stage stage = (Stage) borderPane.getScene().getWindow();
		Scenes scene = Scenes.INFO;
		SceneLoader loader = new SceneLoader(scene);
		Parent root = loader.loadSceneWithSeries(selected);
		BorderPane motherPane = (BorderPane) borderPane.getParent();
		motherPane.setCenter(root);
		stage.setTitle(scene.getTitle() + " about " + selected.getName());
	}

	@FXML
	private void startSeries() {
		Series selectedSeries = rightTable.getSelectionModel().getSelectedItem();
		if (selectedSeries == null) {
			return;
		}

		collection.startSeries(selectedSeries);
		updateScene();
		leftTable.getSelectionModel().select(selectedSeries);
	}
}
