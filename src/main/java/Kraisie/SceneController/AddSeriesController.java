package Kraisie.SceneController;

import Kraisie.Data.MySeries;
import Kraisie.Dialog.PopUp;
import Kraisie.TVDB.APIKey;
import Kraisie.TVDB.TVDB_Data;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

	}

	/**
	 * get a maximum of 5 series which fit to the search term
	 */
	@FXML
	private void addTVDB() {
		List<MySeries> allSeries = MySeries.readData();
		if (MySeries.checkDuplicate(allSeries, nameTVDB.getText())) {
			popUp.showAlert("Duplicate found!", "That series is already in you list.", false, (Stage) nameTVDB.getScene().getWindow());
			return;
		}

		TVDB_Data tvdbAPI = new TVDB_Data(APIKey.readKey());
		List<MySeries> possibleSeries = tvdbAPI.searchSeries(nameTVDB.getText());
		if (possibleSeries == null || possibleSeries.size() == 0) {
			popUp.showWarning("No series found!", "No series found by the name of \"" + nameTVDB.getText() + "\".", (Stage) nameTVDB.getScene().getWindow());
			return;
		}

		if (possibleSeries.size() == 1) {
			//might be an empty series (uncommon but happens)
			possibleSeries.set(0, tvdbAPI.getUpdate(possibleSeries.get(0).getTvdbID(), 0, 1, 1));
			if (possibleSeries.get(0).getEpisodes().size() >= 1) {
				try {
					allSeries.add(possibleSeries.get(0));
					MySeries.writeData(allSeries);
				} catch (IOException e) {
					popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) nameTVDB.getScene().getWindow());
					return;
				}
				popUp.showAlert("Series added!", "\"" + possibleSeries.get(0).getName() + "\" has been added to your list.", false, (Stage) nameTVDB.getScene().getWindow());
			} else {
				popUp.showError("Major error!", "The found series is corrupt, please contact @Kraisie with the series name!", false, (Stage) nameTVDB.getScene().getWindow());
			}
			back();
		}

		if (possibleSeries.size() > 1) {
			try {
				openSceneWithOneParameter((Stage) backButton.getScene().getWindow(), "/FXML/SelectFoundSeries.fxml", "Select one of the found series", possibleSeries);
			} catch (IOException e) {
				popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) nameTVDB.getScene().getWindow());
			}
		}
	}

	/**
	 * opens the main menu scene
	 *
	 * @see Controller
	 * @see MainSeriesController
	 */
	@FXML
	private void back() {
		try {
			openScene((Stage) backButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) backButton.getScene().getWindow());
		}
	}
}
