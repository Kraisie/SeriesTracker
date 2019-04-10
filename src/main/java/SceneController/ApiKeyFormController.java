package SceneController;

import Dialog.BrowserControl;
import Dialog.PopUp;
import TVDB.APIKey;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class ApiKeyFormController extends Controller {

	@FXML
	private TextField textApiKey;
	@FXML
	private TextField textUserKey;
	@FXML
	private TextField textUserName;
	@FXML
	private Button doneButton;

	@FXML
	private void initialize() {

	}

	/**
	 * opens Browser with theTVDB website to get an API Key
	 */
	@FXML
	private void openTVDB() {
		BrowserControl.openBrowser("https://www.thetvdb.com/member/api");
	}

	/**
	 * gets the data from the UI, saves it and opens the main scene
	 */
	@FXML
	private void setKey() {
		PopUp popUp = new PopUp();
		if(textApiKey.getText().isEmpty() || textUserKey.getText().isEmpty() || textUserName.getText().isEmpty()) {
			popUp.showError("Missing data!", "Please fill in each field correctly.", false);
			return;
		}

		APIKey apiKey = new APIKey(textApiKey.getText(), textUserKey.getText(), textUserName.getText());
		APIKey.writeKey(apiKey);

		try {
			openScene((Stage) doneButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
		}
	}
}
