package SceneController;

import Data.MySeries;
import Data.SearchParameter;
import Dialog.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class SearchController extends Controller {

	@FXML
	private Spinner<Integer> durationSpinner;
	@FXML
	private Spinner<Integer> derivationSpinner;
	@FXML
	private ComboBox<String> ratingChoice;
	@FXML
	private Spinner<Integer> seasonsSpinner;
	@FXML
	private CheckBox checkStarted;
	@FXML
	private CheckBox checkWatching;
	@FXML
	private CheckBox checkWaiting;
	@FXML
	private CheckBox checkFinished;
	@FXML
	private RadioButton radioContinuing;
	@FXML
	private RadioButton radioEnded;
	@FXML
	private ToggleGroup state;
	@FXML
	private Button infoButton;
	@FXML
	private Button backButton;
	@FXML
	private Button searchButton;
	@FXML
	private Button researchButton;
	@FXML
	private ListView<String> foundMatches;

	private boolean isAlreadyFound = false;
	private PopUp popUp = new PopUp();
	private List<MySeries> tmpMatches;

	/*
	 *	this method is needed as we do not need to pass a parameter if we call from the main menu
	 */
	void initData() {
		this.tmpMatches = null;
		ownInitialize();
	}

	void initData(List<MySeries> tmpMatches) {
		this.tmpMatches = tmpMatches;
		ownInitialize();
	}

	/*
	 *	this method is not needed as it would run as soon as the FXMLLoader loads the fxml file
	 * 	as such the parameters didn't already get passed as the initData can only be called after
	 * 	we initialized the Controller. Thus we later have to call an own InitializeFunction, but can
	 *  also not just remove this function as it initializes all scene content.
	 */
	@FXML
	private void initialize() {

	}

	private void ownInitialize() {
		// check if we need to reuse found series
		if (tmpMatches != null) {
			isAlreadyFound = true;

			for (MySeries series : tmpMatches) {
				foundMatches.getItems().add(series.getName());
			}
		}

		// still have to do the search
		if (!isAlreadyFound) {
			changeSearchMode(true);
			setSearchOptionValues();
		} else {
			changeSearchMode(false);
		}
	}

	/*
	 *  changes between showing results and showing the search options in the scene
	 */
	private void changeSearchMode(boolean mode) {
		durationSpinner.setVisible(mode);
		derivationSpinner.setVisible(mode);
		ratingChoice.setVisible(mode);
		seasonsSpinner.setVisible(mode);
		checkStarted.setVisible(mode);
		checkWatching.setVisible(mode);
		checkWaiting.setVisible(mode);
		checkFinished.setVisible(mode);
		radioContinuing.setVisible(mode);
		radioEnded.setVisible(mode);

		durationSpinner.setDisable(!mode);
		derivationSpinner.setDisable(!mode);
		ratingChoice.setDisable(!mode);
		seasonsSpinner.setDisable(!mode);
		checkStarted.setDisable(!mode);
		checkWatching.setDisable(!mode);
		checkWaiting.setDisable(!mode);
		checkFinished.setDisable(!mode);
		radioContinuing.setDisable(!mode);
		radioEnded.setDisable(!mode);

		foundMatches.setVisible(!mode);
		foundMatches.setDisable(mode);
		searchButton.setVisible(mode);
		searchButton.setDisable(!mode);
		researchButton.setVisible(!mode);
		researchButton.setDisable(mode);
		infoButton.setVisible(!mode);
		infoButton.setDisable(mode);
	}

	/*
	 *  initializes the ranges and starting values of the search fields
	 */
	private void setSearchOptionValues() {
		SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500);
		SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500);
		SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500);

		TextFormatter<Integer> formatter1 = new TextFormatter<>(valueFactory1.getConverter(), valueFactory1.getValue());
		TextFormatter<Integer> formatter2 = new TextFormatter<>(valueFactory2.getConverter(), valueFactory2.getValue());
		TextFormatter<Integer> formatter3 = new TextFormatter<>(valueFactory3.getConverter(), valueFactory3.getValue());

		durationSpinner.getEditor().setTextFormatter(formatter1);
		derivationSpinner.getEditor().setTextFormatter(formatter2);
		seasonsSpinner.getEditor().setTextFormatter(formatter3);

		valueFactory1.valueProperty().bindBidirectional(formatter1.valueProperty());
		valueFactory2.valueProperty().bindBidirectional(formatter2.valueProperty());
		valueFactory3.valueProperty().bindBidirectional(formatter3.valueProperty());

		durationSpinner.setValueFactory(valueFactory1);
		derivationSpinner.setValueFactory(valueFactory2);
		seasonsSpinner.setValueFactory(valueFactory3);

		ObservableList<String> choices = FXCollections.observableArrayList();
		choices.addAll(" 0", " >1", " >2", " >3", " >4", " >5", " >6", " >7", " >8", " >9");
		ratingChoice.setItems(choices);
		ratingChoice.getSelectionModel().select(0);

		radioContinuing.setSelected(false);
		radioEnded.setSelected(false);
	}

	/*
	 *  search for series by set parameters
	 */
	@FXML
	private void search() {
		List<MySeries> allSeries = MySeries.readData();
		List<MySeries> matches = new ArrayList<>();

		SearchParameter parameter = getSearchParameter();
		for (MySeries series : allSeries) {
			if (checkParameters(parameter, series)) {
				// found a matching series
				matches.add(series);
			}
		}

		if (matches.size() == 0) {
			popUp.showWarning("No matches found!", "There are no series that match your search parameters.");
			ownInitialize();
		}

		isAlreadyFound = true;
		tmpMatches = matches;
		ownInitialize();
	}

	/*
	 *  get the chosen parameters from the UI
	 */
	private SearchParameter getSearchParameter() {
		int rating = Integer.parseInt(ratingChoice.getValue().replaceAll(">", " ").replaceAll(" ", ""));
		int duration = durationSpinner.getValue();
		int derivation = derivationSpinner.getValue();
		int seasons = seasonsSpinner.getValue();
		int userState = getIntUserState();
		String status = getStringStatus();

		return new SearchParameter(rating, duration, derivation, seasons, userState, status);

	}

	/*
	 *  transform user state parameter to an integer value
	 */
	private int getIntUserState() {
		if (checkStarted.isSelected()) {
			return 0;
		}

		if (checkWatching.isSelected()) {
			return 1;
		}

		if (checkWaiting.isSelected()) {
			return 2;
		}

		if (checkFinished.isSelected()) {
			return 3;
		}

		return -1;
	}

	/*
	 *  transform status parameter to a string
	 */
	private String getStringStatus() {
		if (radioContinuing.isSelected()) {
			return "Continuing";
		}

		if (radioEnded.isSelected()) {
			return "Ended";
		}

		return "";
	}

	/*
	 *  check if a series fulfills all parameter
	 */
	private boolean checkParameters(SearchParameter parameter, MySeries series) {
		// check rating
		if (parameter.getRating() != 0 && series.getRating() < parameter.getRating()) {
			return false;
		}

		// check duration + derivation
		if (parameter.getDuration() != 0 &&
				(series.getRuntime() < (parameter.getDuration() - parameter.getDerivation()) ||
						series.getRuntime() > (parameter.getDuration() + parameter.getDerivation()))) {
			return false;
		}

		// check seasons
		if (parameter.getSeasons() != 0 && series.getNumberOfSeasons() != parameter.getSeasons()) {
			return false;
		}

		// check user state
		if (parameter.getUserStatus() != -1 && parameter.getUserStatus() != series.getUserState()) {
			return false;
		}

		// check status
		if (!parameter.getStatus().isEmpty()) {
			return parameter.getStatus().equals(series.getStatus());
		}

		return true;
	}

	/*
	 *  Search again after the search was already successful
	 */
	@FXML
	private void reSearch() {
		foundMatches.getItems().clear();
		tmpMatches = null;
		isAlreadyFound = false;
		ownInitialize();
	}

	/*
	 *  show further information about a found series
	 */
	@FXML
	private void showInformation() {
		if (foundMatches.getSelectionModel().getSelectedItem() == null) {
			popUp.showWarning("No series selected!", "Please select a series to get the fitting information.");
			return;
		}

		List<MySeries> allSeries = MySeries.readData();
		MySeries selectedSeries = null;
		for (MySeries series : allSeries) {
			if (series.getName().equals(foundMatches.getSelectionModel().getSelectedItem())) {
				selectedSeries = series;
				break;
			}
		}

		try {
			openAdvancedInformationFromSearch((Stage) infoButton.getScene().getWindow(), "/FXML/AdvancedInformation.fxml", "Information about " + foundMatches.getSelectionModel().getSelectedItem(), selectedSeries, tmpMatches);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
		}
	}

	/*
	 *  go back to main menu
	 */
	@FXML
	private void back() {
		try {
			openScene((Stage) backButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
		}
	}
}
