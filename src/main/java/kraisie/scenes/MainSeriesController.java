package kraisie.scenes;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
	private TableView<Series> tableContinueWatching;

	@FXML
	private TableColumn<Series, String> columnContinueName;

	@FXML
	private TableColumn<Series, String> columnContinueSeason;

	@FXML
	private TableColumn<Series, String> columnContinueEpisode;

	@FXML
	private TableView<Series> tableStartWatching;

	@FXML
	private TableColumn<Series, String> columnStartName;

	@FXML
	private TableColumn<Series, String> columnStartSeasons;

	private Collection collection;

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
		populateTables();
	}

	private void setTableProperties() {
		setCellValueFactories();
		setColumnWidth();
	}

	private void setCellValueFactories() {
		columnContinueName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnContinueSeason.setCellValueFactory(new PropertyValueFactory<>("currentSeason"));
		columnContinueEpisode.setCellValueFactory(new PropertyValueFactory<>("currentEpisode"));

		columnStartName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnStartSeasons.setCellValueFactory(new PropertyValueFactory<>("numberOfSeasons"));
	}

	private void setColumnWidth() {
		columnContinueName.prefWidthProperty().bind(tableContinueWatching.widthProperty().divide(2));
		columnContinueSeason.prefWidthProperty().bind(tableContinueWatching.widthProperty().divide(4));
		columnContinueEpisode.prefWidthProperty().bind(tableContinueWatching.widthProperty().divide(4));

		columnStartName.prefWidthProperty().bind(tableStartWatching.widthProperty().divide(1.5));
		columnStartSeasons.prefWidthProperty().bind(tableStartWatching.widthProperty().divide(3));
	}

	private void populateTables() {
		ObservableList<Series> started = collection.getObservableStarted();
		ObservableList<Series> unstarted = collection.getObservableUnstarted();

		tableContinueWatching.setItems(started);
		tableStartWatching.setItems(unstarted);

		tableContinueWatching.refresh();
		tableStartWatching.refresh();
	}

	@FXML
	private void clickOnTableUnstarted() {
		tableContinueWatching.getSelectionModel().clearSelection();
	}

	@FXML
	private void clickOnTableWatching() {
		tableStartWatching.getSelectionModel().clearSelection();
	}

	@FXML
	private void decEpisodeButton() {
		Series selectedSeries = tableContinueWatching.getSelectionModel().getSelectedItem();
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
		Series selected = tableContinueWatching.getSelectionModel().getSelectedItem();
		if (selected != null) {
			return selected;
		}

		return tableStartWatching.getSelectionModel().getSelectedItem();
	}

	@FXML
	private void incEpisodeButton() {
		Series selectedSeries = tableContinueWatching.getSelectionModel().getSelectedItem();
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
		scrollToSeries(tableStartWatching, event);
	}

	@FXML
	private void scrollToKeyWatching(KeyEvent event) {
		scrollToSeries(tableContinueWatching, event);
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
	private void showFinishedSeries() {

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
		BorderPane motherScene = (BorderPane) borderPane.getParent();
		motherScene.setCenter(root);
		stage.setTitle(scene.getTitle());
	}

	@FXML
	private void showWaiting() {

	}

	@FXML
	private void startSeries() {
		Series selectedSeries = tableStartWatching.getSelectionModel().getSelectedItem();
		if (selectedSeries == null) {
			return;
		}

		collection.startSeries(selectedSeries);
		updateScene();
		tableContinueWatching.getSelectionModel().select(selectedSeries);
	}
}
