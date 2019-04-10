package SceneController;

import Data.Settings;
import Dialog.PopUp;
import TVDB.APIKey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class SettingsController extends Controller {

	@FXML
	private TextField textPathAPIKey;
	@FXML
	private TextField textPathSave;
	@FXML
	private TextField textPathBackUp;
	@FXML
	private TextField textApiKey;
	@FXML
	private TextField textUserKey;
	@FXML
	private TextField textUserName;
	@FXML
	private Spinner<Integer> frequencySpinner;
	@FXML
	private ComboBox<String> languageCombo;
	@FXML
	private Button backButton;

	private Settings settings;
	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		fillSettings();
	}

	/**
	 * populates TextField, Spinner and ComboBox with data from the Settings
	 *
	 * @see Settings
	 */
	private void fillSettings() {
		settings = Settings.readData();
		APIKey key = APIKey.readKey();

		if (key == null) {
			key = new APIKey();
		}

		textPathAPIKey.setText(settings.getPathAPIKey().toString());
		textPathSave.setText(settings.getPathSeries().toString());
		textPathBackUp.setText(settings.getPathBackUp().toString());

		textApiKey.setText(key.getApikey());
		textUserKey.setText(key.getUserkey());
		textUserName.setText(key.getUsername());

		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 14);
		TextFormatter<Integer> formatter = new TextFormatter<>(valueFactory.getConverter(), valueFactory.getValue());
		frequencySpinner.getEditor().setTextFormatter(formatter);
		valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());
		frequencySpinner.setValueFactory(valueFactory);
		frequencySpinner.getValueFactory().setValue(settings.getBackUpCycle());

		ObservableList<String> languageOptions = FXCollections.observableArrayList();
		languageOptions.addAll("en", "de", "es", "fr", "it");
		languageCombo.setItems(languageOptions);
		languageCombo.getSelectionModel().select(settings.getLangIso());
	}

	/**
	 * changes text in TextField on Path change
	 */
	@FXML
	private void changePathAPIKey() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Set path for your API key file...");
		File dir = fileChooser.showDialog(backButton.getScene().getWindow());

		if (dir == null) {
			return;
		}

		File file = new File(dir, "API_Key.json");
		try {
			Path path = Paths.get(file.getCanonicalPath());
			settings.setPathAPIKey(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		textPathAPIKey.setText(settings.getPathAPIKey().toString());
	}

	/**
	 * changes text in TextField on Path change
	 */
	@FXML
	private void changePathSave() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Set path for your series file...");
		File dir = fileChooser.showDialog(backButton.getScene().getWindow());

		if (dir == null) {
			return;
		}

		File file = new File(dir, "Series.json");
		try {
			Path path = Paths.get(file.getCanonicalPath());
			settings.setPathSeries(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		textPathSave.setText(settings.getPathSeries().toString());
	}

	/**
	 * changes text in TextField on Path change
	 */
	@FXML
	private void changePathBackUp() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Set path for your BackUp file...");
		File dir = fileChooser.showDialog(backButton.getScene().getWindow());

		if (dir == null) {
			return;
		}

		File file = new File(dir, "BackUp.json");
		try {
			Path path = Paths.get(file.getCanonicalPath());
			settings.setPathBackUp(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		textPathBackUp.setText(settings.getPathBackUp().toString());
	}

	/**
	 * gets the data from the UI and saves the Settings to a json file
	 *
	 * @see Settings
	 */
	@FXML
	private void save() {
		Settings oldSettings = Settings.readData();
		if (oldSettings == null) {
			return;
		}

		changeSettings();
		changeApiKey();

		try {
			Path newSeries = settings.getPathSeries();
			Path oldSeries = oldSettings.getPathSeries();
			String fileSeries = "Series.json";
			moveFile(newSeries, oldSeries, fileSeries);

			Path newKey = settings.getPathAPIKey();
			Path oldKey = oldSettings.getPathAPIKey();
			String fileKey = "API_Key.json";
			moveFile(newKey, oldKey, fileKey);

			Path newBackUp = settings.getPathBackUp();
			Path oldBackUp = oldSettings.getPathBackUp();
			String fileBackUp = "BackUp.json";
			moveFile(newBackUp, oldBackUp, fileBackUp);
		} catch (IOException e) {
			popUp.showError("Failed copying old save files!", getStackTrace(e), true);
		}

		Settings.writeData(settings);
		back();
	}

	/**
	 * changes the Settings to the data set in the UI
	 *
	 * @see Settings
	 */
	private void changeSettings() {
		settings.setBackUpCycle(frequencySpinner.getValue());
		settings.setLangIso(languageCombo.getValue());
	}

	/**
	 * transfers changes of the API Key from UI to file
	 *
	 * @see APIKey
	 */
	private void changeApiKey() {
		APIKey key = APIKey.readKey();
		boolean changes = false;
		if (key != null) {
			String apiKey = textApiKey.getText();
			if (!apiKey.equals(key.getApikey())) {
				key.setApikey(apiKey);
				changes = true;
			}

			String userKey = textUserKey.getText();
			if (!userKey.equals(key.getUserkey())) {
				key.setUserkey(userKey);
				changes = true;
			}

			String userName = textUserName.getText();
			if (!userName.equals(key.getUsername())) {
				key.setUsername(userName);
				changes = true;
			}

			if (changes) {
				APIKey.writeKey(key);
			}
		}
	}

	/**
	 * moves save files if needed or deletes old files
	 */
	private void moveFile(Path newPath, Path oldPath, String fileName) throws IOException {
		if (oldPath.equals(newPath)) {
			return;
		}

		if (!oldPath.toFile().exists()) {
			return;
		}

		if (newPath.toFile().exists()) {
			if (popUp.showChoice(fileName + " already exists!", "Do you want to replace it?")) {
				Files.move(oldPath, newPath, REPLACE_EXISTING);
			} else {
				// remove old file
				Files.delete(oldPath);
			}
		} else {
			Files.move(oldPath, newPath, REPLACE_EXISTING);
		}
	}

	/**
	 * restores standard settings
	 *
	 * @see Settings
	 */
	@FXML
	private void standard() {
		settings = new Settings();
		initialize();
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
			openScene((Stage) backButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
		}
	}
}
