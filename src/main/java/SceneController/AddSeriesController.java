package SceneController;

import Data.MySeries;
import Dialog.PopUp;
import TVDB.TVDB_Data;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class AddSeriesController extends Controller {

	@FXML
	private TextField nameTVDB;
	@FXML
	private Button backButton;

	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		// disable the context menu
		nameTVDB.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
	}

	/*
	 *	get a maximum of 5 series which fit to the search term
	 */
	@FXML
	private void addTVDB() {
		List<MySeries> allSeries = MySeries.readData();
		if (MySeries.checkDuplicate(allSeries, nameTVDB.getText())) {
			popUp.showAlert("Duplicate found!", "That series is already in you list.", false);
			return;
		}

		List<MySeries> possibleSeries = TVDB_Data.searchSeries(nameTVDB.getText());
		if (possibleSeries == null || possibleSeries.size() == 0) {
			popUp.showWarning("No series found!", "No series found by the name of \"" + nameTVDB.getText() + "\".");
			return;
		}

		if (possibleSeries.size() == 1) {
			//might be an empty series (uncommon but happens)
			possibleSeries.set(0, TVDB_Data.getUpdate(possibleSeries.get(0).getTvdbID(), 0, 1, 1));
			if (possibleSeries.get(0).getEpisodes().size() >= 1) {
				allSeries.add(possibleSeries.get(0));
				MySeries.writeData(allSeries);
				popUp.showAlert("Series added!", "\"" + possibleSeries.get(0).getName() + "\" has been added to your list.", false);
			} else {
				popUp.showError("Major error!", "The found series is corrupt, please contact @Kraisie with the series name!", false);
			}
			back();
		}

		try {
			openSceneWithOneParameter((Stage) backButton.getScene().getWindow(), "/FXML/SelectFoundSeries.fxml", "Select one of the found series", possibleSeries);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(19);
		}
	}

	/*
	 *	get back to the main menu
	 */
	@FXML
	private void back() {
		try {
			openScene((Stage) backButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			System.exit(20);
		}
	}
}
