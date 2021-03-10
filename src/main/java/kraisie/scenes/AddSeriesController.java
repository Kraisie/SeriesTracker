package kraisie.scenes;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kraisie.data.Collection;
import kraisie.data.DataSingleton;
import kraisie.data.definitions.Scenes;
import kraisie.tvdb.SearchResult;
import kraisie.tvdb.TVDB;
import kraisie.ui.SceneLoader;

import java.util.List;

public class AddSeriesController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private TextField nameTVDB;

	private DataSingleton data;

	@FXML
	private void initialize() {
		data = DataSingleton.getInstance();
	}

	@FXML
	private void addTVDB() {
		String name = nameTVDB.getText();
		if (name.isBlank()) {
			return;
		}

		searchSeries(name);
	}

	private void searchSeries(String name) {
		List<SearchResult> possibleMatches = getPossibleMatches(name);
		if (possibleMatches.size() == 1) {
			SearchResult match = possibleMatches.get(0);
			int seriesId = match.getSearchData().getId();
			Collection collection = data.getCollection();
			collection.addNewSeriesById(seriesId);
			showMainScene();
			return;
		}

		if (possibleMatches.size() == 0) {
			// TODO: popUp, not able to get series from TVDB
			return;
		}

		openSelectionScene(possibleMatches);
	}

	private List<SearchResult> getPossibleMatches(String name) {
		TVDB api = new TVDB();
		return api.searchSeries(name);
	}

	private void openSelectionScene(List<SearchResult> data) {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		Scenes scene = Scenes.SELECT;
		SceneLoader loader = new SceneLoader(scene);
		Parent root = loader.loadSceneWithData(data);
		borderPane.setCenter(root);
		stage.setTitle(scene.getTitle());
	}

	@FXML
	private void acceptOnEnter(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			addTVDB();
		}
	}

	private void showMainScene() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		SceneLoader loader = new SceneLoader(Scenes.MAIN);
		Parent root = loader.loadScene();
		stage.setTitle(Scenes.MAIN.getTitle());

		borderPane.setCenter(root);
	}
}
