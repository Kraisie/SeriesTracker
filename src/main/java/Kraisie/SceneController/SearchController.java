package Kraisie.SceneController;

import Kraisie.Data.MySeries;
import Kraisie.Data.SearchParameter;
import Kraisie.Dialog.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class SearchController extends Controller {

	@FXML
	private VBox containerSearchData;
	@FXML
	private VBox containerResultSet;
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
	private Label labelDerivation;
	@FXML
	private Label labelUserstate;
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

	/**
	 * means that there are no search results we need to show again
	 */
	void initData() {
		this.tmpMatches = null;
		ownInitialize();
	}

	/**
	 * used to receive data from another controller via Dependency Injection
	 *
	 * @param tmpMatches a list of previously found matches
	 */
	void initData(List<MySeries> tmpMatches) {
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

		// add hover listeners to labels that show a tooltip
		setTooltipToLabel(labelDerivation,
				"If you search for series with a duration of 30 minutes\n" +
						"and choose a derivation of 5 minutes you will get all\n" +
						"series with episodes that are 25 to 35 minutes long."
		);

		setTooltipToLabel(labelUserstate,
				"If you search for series with a duration of 30 minutes\n" +
						"and choose a derivation of 5 minutes you will get all\n" +
						"series with episodes that are 25 to 35 minutes long."
		);
	}

	/**
	 * disables or enables specific scene content to either show matches of the search or the UI to select search parameters
	 *
	 * @param mode if true it shows the search template, if false it shows the found series
	 */
	private void changeSearchMode(boolean mode) {
		containerSearchData.setVisible(mode);
		containerSearchData.setDisable(!mode);
		searchButton.setVisible(mode);
		searchButton.setDisable(!mode);

		containerResultSet.setVisible(!mode);
		containerResultSet.setDisable(mode);
		researchButton.setVisible(!mode);
		researchButton.setDisable(mode);
	}

	/**
	 * initializes the ranges and starting values of the search fields
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
		choices.addAll("0", ">1", ">2", ">3", ">4", ">5", ">6", ">7", ">8", ">9");
		ratingChoice.setItems(choices);
		ratingChoice.getSelectionModel().select(0);

		radioContinuing.setSelected(false);
		radioEnded.setSelected(false);
	}

	/**
	 * searches for series by set parameters
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
			popUp.showWarning("No matches found!", "There are no series that match your search parameters.", (Stage) searchButton.getScene().getWindow());
			ownInitialize();
			return;
		}

		isAlreadyFound = true;
		tmpMatches = matches;
		ownInitialize();
	}

	/**
	 * opens information scene for a selected series
	 *
	 * @see Controller
	 * @see AdvancedInformationController
	 */
	@FXML
	private void displayInformation() {
		MySeries selectedSeries = getSelectedSeries();

		if (selectedSeries == null) {
			popUp.showWarning("No series selected!", "Please select a series to get the fitting information.", (Stage) foundMatches.getScene().getWindow());
			return;
		}

		try {
			String title = "Information about " + selectedSeries.getName();
			openAdvancedInformationFromSearch((Stage) foundMatches.getScene().getWindow(), "/FXML/AdvancedInformation.fxml", title, selectedSeries, tmpMatches);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) foundMatches.getScene().getWindow());
		}
	}

	/**
	 * @return series that is selected in ListView or null if not found
	 */
	private MySeries getSelectedSeries() {
		String name = foundMatches.getSelectionModel().getSelectedItem();
		List<MySeries> allSeries = MySeries.readData();
		for (MySeries series : allSeries) {
			if (series.getName().equals(name)) {
				return series;
			}
		}

		return null;
	}

	/**
	 * gets the chosen parameters from the UI
	 *
	 * @return SearchParameter
	 * @see SearchParameter
	 */
	private SearchParameter getSearchParameter() {
		int rating = Integer.parseInt(ratingChoice.getValue().replaceAll(">", ""));
		int duration = durationSpinner.getValue();
		int derivation = derivationSpinner.getValue();
		int seasons = seasonsSpinner.getValue();
		int userState = getIntUserState();
		String status = getStringStatus();

		return new SearchParameter(rating, duration, derivation, seasons, userState, status);
	}

	/**
	 * transforms user state parameter to an integer value
	 *
	 * @return userState as integer, -1 if none is selected
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

	/**
	 * @return selected status as String
	 * @see MySeries
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

	/**
	 * Checks if a series fulfills all set SearchParameter
	 *
	 * @param parameter search parameters that get used to filter series
	 * @param series    series that gets matched with the parameters
	 * @return true if series fulfills all parameters
	 * @see SearchParameter
	 */
	private boolean checkParameters(SearchParameter parameter, MySeries series) {
		// check rating
		if (parameter.getRating() != 0 && series.getRating() < parameter.getRating()) {
			return false;
		}

		// check duration + derivation
		if (parameter.getDuration() != 0 || parameter.getDeviation() != 0) {
			if (series.getRuntime() < (parameter.getDuration() - parameter.getDeviation()) ||
					series.getRuntime() > (parameter.getDuration() + parameter.getDeviation())) {
				return false;
			}
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

	/**
	 * do a new search
	 */
	@FXML
	private void reSearch() {
		foundMatches.getItems().clear();
		tmpMatches = null;
		isAlreadyFound = false;
		ownInitialize();
	}

	/**
	 * opens main menu scene
	 *
	 * @see Controller
	 * @see MainSeriesController
	 */
	@FXML
	private void back() {
		try {
			openMain((Stage) backButton.getScene().getWindow());
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) backButton.getScene().getWindow());
		}
	}
}
