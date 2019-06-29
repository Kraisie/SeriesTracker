package Kraisie.SceneController;

import Kraisie.Data.MySeries;
import Kraisie.Dialog.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class WaitingController extends Controller {

	@FXML
	private TableView<MySeries> tableWaiting;
	@FXML
	private TableColumn<MySeries, String> columnName;
	@FXML
	private TableColumn<MySeries, String> columnNextAirDate;
	@FXML
	private Button backButton;

	@FXML
	private void initialize() {
		populateTable();
		adjustColumnWidth();
	}

	/**
	 * populates the tables with data
	 */
	private void populateTable() {
		ObservableList<MySeries> waitingSeries = FXCollections.observableArrayList();
		List<MySeries> listEntries = MySeries.readData();

		if (listEntries.isEmpty()) {
			return;
		}

		setCellValueFactories();

		// populate the observable lists for the tables according to the userState
		for (MySeries listEntry : listEntries) {
			if (listEntry.getUserState() == 2) {
				waitingSeries.add(listEntry);
			}
		}

		tableWaiting.setItems(waitingSeries);
	}

	/**
	 * sets cell values factories for every column
	 */
	private void setCellValueFactories() {
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnNextAirDate.setCellValueFactory(new PropertyValueFactory<>("nextAirDate"));
	}

	/**
	 * sets the column width for every column
	 */
	private void adjustColumnWidth() {
		columnName.prefWidthProperty().bind(tableWaiting.widthProperty().divide(2));
		columnNextAirDate.prefWidthProperty().bind(tableWaiting.widthProperty().divide(2));
	}

	/**
	 * scrolls to a series with the same first char as the button pressed for all tables
	 */
	@FXML
	private void scrollToKeyWaiting(KeyEvent key) {
		scrollToSeries(tableWaiting, key);
	}

	/**
	 * opens information scene for a selected series
	 *
	 * @see Controller
	 * @see AdvancedInformationController
	 */
	@FXML
	private void displayInformation() {
		PopUp popUp = new PopUp();
		MySeries selectedSeries = tableWaiting.getSelectionModel().getSelectedItem();

		if (selectedSeries == null) {
			popUp.showWarning("No series selected!", "Please select a series to get the fitting information.", (Stage) tableWaiting.getScene().getWindow());
			return;
		}

		try {
			String title = "Information about " + selectedSeries.getName();
			openSceneWithOneParameter((Stage) tableWaiting.getScene().getWindow(), "/FXML/AdvancedInformation.fxml", title, selectedSeries);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) tableWaiting.getScene().getWindow());
		}
	}

	/**
	 * opens main menu scene
	 *
	 * @see Controller
	 * @see MainSeriesController
	 */
	@FXML
	private void backToMain() {
		try {
			openMain((Stage) backButton.getScene().getWindow());
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) backButton.getScene().getWindow());
		}
	}
}
