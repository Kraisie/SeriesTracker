package kraisie.scenes;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kraisie.data.Series;
import kraisie.data.definitions.Scenes;
import kraisie.ui.SceneLoader;

import java.util.List;

public class SearchResultController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private ListView<String> seriesList;

	private List<Series> series;


	@FXML
	private void initialize() {

	}

	public void initData(List<Series> series) {
		this.series = series;
		fillSeriesList();
	}

	private void fillSeriesList() {
		if (series == null) {
			return;
		}

		if (series.size() <= 0) {
			return;
		}

		for (Series s : series) {
			seriesList.getItems().add(s.getName());
		}
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

	private Series getSelectedSeries() {
		String selectedName = seriesList.getSelectionModel().getSelectedItem();
		if (selectedName == null) {
			return null;
		}

		for (Series s : series) {
			if (s.getName().equals(selectedName)) {
				return s;
			}
		}

		return null;
	}

}
