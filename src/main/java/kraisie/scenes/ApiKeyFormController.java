package kraisie.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kraisie.data.APIKey;
import kraisie.dialog.BrowserControl;

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
			// TODO: PopUp no supported browser
		}
	}

	@FXML
	private void setKey() {
		String apiKey = textApiKey.getText();
		String userKey = textUserKey.getText();
		String userName = textUserName.getText();

		if (apiKey.isBlank() || userKey.isBlank() || userName.isBlank()) {
			// TODO: pop up missing data
			return;
		}

		APIKey key = new APIKey(apiKey, userKey, userName);
		if (!key.isValid()) {
			// TODO: pop up wrong data
			System.out.println("wrong");
			return;
		}

		saveKey(key);
		closeStage();
	}

	private void saveKey(APIKey key) {
		try {
			APIKey.writeKey(key);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: pop up could not save
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
