package SceneController;

import Data.MySeries;
import Dialog.PopUp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class FinishedController extends Controller {

	@FXML
	private TableView<MySeries> tableFinishedSeries;
	@FXML
	private TableColumn<MySeries, String> columnName;
	@FXML
	private TableColumn<MySeries, Integer> columnSeasons;
	@FXML
	private TableColumn<MySeries, Integer> columnEpisodes;
	@FXML
	private Label labelWasted;
	@FXML
	private Button backButton;

	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		ObservableList<MySeries> finishedSeries = FXCollections.observableArrayList();
		ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());

		if (listEntries.isEmpty()) {
			popUp.showError("Failed getting series!", "There was an error while trying to get the list of your series.", false);
		}

		for (MySeries listEntry : listEntries) {
			if (listEntry.getUserState() == 3) {
				finishedSeries.add(listEntry);
			}
		}

		setCellValueFactories();
		tableFinishedSeries.setItems(finishedSeries);
		labelWasted.setText(MySeries.wastedMinutesToString(calcWastedTime(finishedSeries)));
	}

	/*
	 *  set all needed cell value factories
	 */
	private void setCellValueFactories() {
		columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		columnSeasons.setCellValueFactory(new PropertyValueFactory<>("numberOfSeasons"));
		columnEpisodes.setCellValueFactory(new PropertyValueFactory<>("sumEpisodes"));
	}

	/*
	 *  calculate the time needed to watch all finished series
	 */
	private int calcWastedTime(ObservableList<MySeries> finishedSeries) {
		int sum = 0;
		for (MySeries series : finishedSeries) {
			sum += series.getWastedTime();
		}

		return sum;
	}


	/*
	 *  get back to the main menu
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
