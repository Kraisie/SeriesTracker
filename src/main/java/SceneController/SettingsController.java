package SceneController;

import Data.Settings;
import Dialog.PopUp;
import TVDB.APIKey;
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
	private Button backButton;

	private Settings settings;
	private PopUp popUp = new PopUp();

	@FXML
	private void initialize() {
		fillSettings();
	}

	/*
	 *	populate Settings with data
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
	}

	/*
	 *	change content if path gets changed
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

	/*
	 *	change content if path gets changed
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

	/*
	 *	change content if path gets changed
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

	/*
	 * 	save the given settings
	 */
	@FXML
	private void save() {
		Settings oldSettings = Settings.readData();
		if (oldSettings == null) {
			return;
		}

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

	/*
	 *	moves save files if needed or deletes old files
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

	/*
	 *	restore standard settings
	 */
	@FXML
	private void standard() {
		settings = new Settings();
		initialize();
	}

	/*
	 *	get back to main menu
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
