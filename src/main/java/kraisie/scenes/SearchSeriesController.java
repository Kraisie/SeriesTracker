package kraisie.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kraisie.data.DataSingleton;
import kraisie.data.Series;
import kraisie.data.definitions.Scenes;
import kraisie.data.definitions.UserState;
import kraisie.ui.SceneLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchSeriesController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private CheckBox choiceNotStarted;

	@FXML
	private CheckBox choiceFinished;

	@FXML
	private CheckBox choiceWaiting;

	@FXML
	private CheckBox choiceWatching;

	@FXML
	private ChoiceBox<String> seriesState;

	@FXML
	private Spinner<Integer> spinnerDuration;

	@FXML
	private Spinner<Integer> spinnerSeasons;

	private DataSingleton data;

	@FXML
	private void initialize() {
		data = DataSingleton.getInstance();
		fillStates();
		fillDurationSpinner();
		fillSeasonSpinner();
	}

	private void fillStates() {
		ObservableList<String> states = FXCollections.observableArrayList();
		states.add("All");
		states.add("Continuing");
		states.add("Ended");
		seriesState.setItems(states);
		seriesState.getSelectionModel().selectFirst();
	}

	private void fillDurationSpinner() {
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500);
		fillSpinner(spinnerDuration, valueFactory);
	}

	private void fillSeasonSpinner() {
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
		fillSpinner(spinnerSeasons, valueFactory);
	}

	private void fillSpinner(Spinner<Integer> spinner, SpinnerValueFactory<Integer> valueFactory) {
		TextFormatter<Integer> formatter = new TextFormatter<>(valueFactory.getConverter(), valueFactory.getValue());
		spinner.getEditor().setTextFormatter(formatter);
		valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());
		spinner.setValueFactory(valueFactory);
	}

	@FXML
	private void searchSeries() {
		List<UserState> acceptableUserStates = getSelectedUserStates();
		String state = seriesState.getSelectionModel().getSelectedItem();
		int numberOfSeasons = spinnerSeasons.getValue();
		int duration = spinnerDuration.getValue();
		List<Series> matchingSeries = getMatchingSeries(acceptableUserStates, state, numberOfSeasons, duration);
		// TODO: if size = 0 popup -> no series found
		showResults(matchingSeries);
	}

	private List<UserState> getSelectedUserStates() {
		List<UserState> acceptableUserStates = new ArrayList<>();
		if (choiceNotStarted.isSelected()) {
			acceptableUserStates.add(UserState.NOT_STARTED);
		}

		if (choiceWatching.isSelected()) {
			acceptableUserStates.add(UserState.WATCHING);
		}

		if (choiceWaiting.isSelected()) {
			acceptableUserStates.add(UserState.WAITING);
		}

		if (choiceFinished.isSelected()) {
			acceptableUserStates.add(UserState.FINISHED);
		}

		return acceptableUserStates;
	}

	private List<Series> getMatchingSeries(List<UserState> acceptableUserStates, String state, int numberOfSeasons, int duration) {
		List<Series> matchingSeries = new ArrayList<>();
		for (Series series : data.getCollection().getSeries()) {
			if (numberOfSeasons != 0 && series.getNumberOfSeasons() != numberOfSeasons) {
				continue;
			}

			if (hasNoMatchingDuration(series, duration)) {
				continue;
			}

			if (!state.equalsIgnoreCase("All") && !series.getStatus().equalsIgnoreCase(state)) {
				continue;
			}

			if (acceptableUserStates.size() == 0 || acceptableUserStates.contains(series.getUserStatus())) {
				matchingSeries.add(series);
			}
		}

		return matchingSeries;
	}

	private boolean hasNoMatchingDuration(Series series, int duration) {
		return duration != 0 &&
				(series.getRuntime() < duration - 5 || series.getRuntime() > duration + 5);
	}

	private void showResults(List<Series> results) {
		if (results.size() == 0) {
			return;
		}

		Stage stage = (Stage) borderPane.getScene().getWindow();
		Scenes scene = Scenes.SEARCH_RESULTS;
		SceneLoader loader = new SceneLoader(scene);
		Parent root = loader.loadSceneWithSeriesList(results);
		BorderPane motherPane = (BorderPane) borderPane.getParent();
		motherPane.setCenter(root);
		stage.setTitle(scene.getTitle());
	}
}
