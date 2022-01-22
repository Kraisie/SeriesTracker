package kraisie.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kraisie.data.APIKey;
import kraisie.dialog.BrowserControl;
import kraisie.dialog.LogUtil;
import kraisie.dialog.PopUp;

import java.io.IOException;

public class ApiKeyFormController {

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

	@FXML
	private void openTVDB() {
		try {
			BrowserControl.openBrowser("https://thetvdb.com/dashboard/account/apikey");
		} catch (Exception e) {
			PopUp popUp = PopUp.forStage((Stage) doneButton.getScene().getWindow());
			popUp.showError("No supported browser!", "Could not open a supported browser. You can find your API key in your TVDB account information. .", false);
		}
	}

	@FXML
	private void setKey() {
		String apiKey = textApiKey.getText();
		String userKey = textUserKey.getText();
		String userName = textUserName.getText();

		if (apiKey.isBlank() || userKey.isBlank() || userName.isBlank()) {
			PopUp popUp = PopUp.forStage((Stage) doneButton.getScene().getWindow());
			popUp.showError("Missing data!", "Please fill out all fields and try again.", false);
			return;
		}

		APIKey key = new APIKey(apiKey, userKey, userName);
		if (key.isInvalid()) {
			PopUp popUp = PopUp.forStage((Stage) doneButton.getScene().getWindow());
			popUp.showError("Invalid data!", "The data is invalid. Please make sure it is correct and try again.", false);
			return;
		}

		saveKey(key);
		closeStage();
	}

	private void saveKey(APIKey key) {
		try {
			APIKey.writeKey(key);
		} catch (IOException e) {
			LogUtil.logError("Could not save API key!", e);
			PopUp popUp = PopUp.forStage((Stage) doneButton.getScene().getWindow());
			popUp.showError("Saving failed!", "Could not save api key on disk! Please check the logs.", false);
			e.printStackTrace();
		}
	}

	private void closeStage() {
		// the stage this scene runs in only gets created for this form.
		// to continue with the program we need to close it as this
		// stage uses showAndWait()
		Stage stage = (Stage) doneButton.getScene().getWindow();
		stage.close();
	}
}
