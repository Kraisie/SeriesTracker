package SceneController;

import Data.MySeries;
import Dialog.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class DeleteController extends Controller {

	@FXML
	private ListView<String> listViewSeries;
	@FXML
	private Button backButton;

	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());
		for (MySeries listEntry : listEntries) {
			listViewSeries.getItems().add(listEntry.getName());
		}
	}

	/*
	 *  delete the selected series
	 */
	@FXML
	private void delete() {
		String name = listViewSeries.getSelectionModel().getSelectedItem();
		ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());
		for (MySeries listEntry : listEntries) {
			if (listEntry.getName().equals(name)) {
				listEntries.remove(listEntry);
				break;
			}
		}

		MySeries.writeData(listEntries);
		popUp.showAlert("Series deleted!", "\"" + name + "\" got deleted successfully.", false);
		back();
	}

	/*
	 *	scroll down to the first series with the first character that just got pressed
	 */
	@FXML
	private void scrollToKey(KeyEvent key) {
		char c = key.getText().charAt(0);
		ObservableList<String> allSeriesInList = listViewSeries.getItems();
		String match = null;

		// if the selected series already starts with that char and the next one in the list still got that char then select the next series
		int index = listViewSeries.getSelectionModel().getSelectedIndex();
		if (index + 1 < allSeriesInList.size()) {
			String nextSeriesName = allSeriesInList.get(index + 1);
			if (c == nextSeriesName.toLowerCase().charAt(0)) {
				listViewSeries.getSelectionModel().select(nextSeriesName);
				return;
			}
		}

		// select first occurrence
		for (String seriesName : allSeriesInList) {
			if (c == seriesName.toLowerCase().charAt(0)) {
				match = seriesName;
				break;
			}
		}

		if (match == null) {
			return;
		}

		listViewSeries.getSelectionModel().select(match);
		listViewSeries.scrollTo(match);
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
