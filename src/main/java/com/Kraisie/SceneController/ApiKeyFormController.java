package com.Kraisie.SceneController;

import com.Kraisie.Dialog.BrowserControl;
import com.Kraisie.Dialog.PopUp;
import com.Kraisie.TVDB.APIKey;
import com.Kraisie.TVDB.TVDB_Data;
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
		try {
			BrowserControl.openBrowser("https://www.thetvdb.com/member/api");
		} catch (Exception e) {
			PopUp popUp = new PopUp();
			popUp.showWarning("Can not open browser!", "There is no supported browser installed on your machine.", (Stage) textApiKey.getScene().getWindow());
		}
	}

	/**
	 * gets the data from the UI, saves it and opens the main scene
	 */
	@FXML
	private void setKey() {
		PopUp popUp = new PopUp();
		if (textApiKey.getText().isEmpty() || textUserKey.getText().isEmpty() || textUserName.getText().isEmpty()) {
			popUp.showError("Missing data!", "Please fill in each field correctly.", false, (Stage) textApiKey.getScene().getWindow());
			return;
		}

		// check if key is valid
		APIKey apiKey = new APIKey(textApiKey.getText(), textUserKey.getText(), textUserName.getText());
		TVDB_Data validation = new TVDB_Data(apiKey);
		if (!validation.keyValid()) {
			popUp.showError("API Key not valid!", "Please recheck your data for mistakes.", false, (Stage) textApiKey.getScene().getWindow());
			return;
		}

		try {
			APIKey.writeKey(apiKey);
		} catch (IOException e) {
			popUp.showError("Failed while saving!", "Trying to save the API Key failed. Please check the validity of you Path.", false, (Stage) textApiKey.getScene().getWindow());
		}

		try {
			openScene((Stage) doneButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) textApiKey.getScene().getWindow());
		}
	}
}
