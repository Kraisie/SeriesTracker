package Kraisie.SceneController;

import Kraisie.Data.Settings;
import Kraisie.Dialog.PopUp;
import Kraisie.TVDB.APIKey;
import Kraisie.TVDB.TVDB_Data;
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
	private Label labelBackUp;
	@FXML
	private Label labelLanguage;
	@FXML
	private Button backButton;
	@FXML
	private Button saveButton;

	private Settings settings;
	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		fillSettings();

		// add hover listeners to labels that show a tooltip
		setTooltipToLabel(labelBackUp,
				"If set to one it will create an update every day.\n" +
						"You can increase the duration of BackUp cycle\n" +
						"to a maximum of 14 days."
		);

		setTooltipToLabel(labelLanguage,
				"You can select the language which you want to use\n" +
						"to search for data in TVDB. If the data is not provided\n" +
						"in your desired language it will use english."
		);
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

		if (changeApiKey()) {
			popUp.showError("APIKey not valid!", "The given APIKey is not valid. Please insert a valid APIKey to use this program.", false, (Stage) saveButton.getScene().getWindow());
			return;
		}

		changeSettings();

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
			popUp.showError("Failed copying old save files!", getStackTrace(e), true, (Stage) saveButton.getScene().getWindow());
		}

		try {
			Settings.writeData(settings);
		} catch (IOException e) {
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) saveButton.getScene().getWindow());
		}
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
	 * @return true if APIKey is valid
	 * @see APIKey
	 */
	private boolean changeApiKey() {
		APIKey key = APIKey.readKey();
		boolean changes = false;
		if (key == null) {
			return false;
		}

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

		TVDB_Data validation = new TVDB_Data(key);
		if (!validation.keyValid()) {
			return false;
		}

		if (changes) {
			try {
				APIKey.writeKey(key);
			} catch (IOException e) {
				popUp.showError("Failed while saving!", "Trying to save the API Key failed. Please check the validity of you Path.", false, (Stage) saveButton.getScene().getWindow());
			}
		}

		return true;
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
			if (popUp.showChoice(fileName + " already exists!", "Do you want to replace it?", (Stage) saveButton.getScene().getWindow())) {
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
			openMain((Stage) backButton.getScene().getWindow());
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) backButton.getScene().getWindow());
		}
	}
}
