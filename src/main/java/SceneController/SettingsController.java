package SceneController;

import Data.Settings;
import Dialog.PopUp;
import TVDB.APIKey;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
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
		// disable the context menus
		textPathAPIKey.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
		textPathSave.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
		textPathBackUp.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
		textApiKey.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
		textUserKey.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
		textUserName.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
		frequencySpinner.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);

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
		if(oldSettings == null) {
			return;
		}

		try {
			if (!oldSettings.getPathSeries().equals(settings.getPathSeries())) {
				Files.move(oldSettings.getPathSeries(), settings.getPathSeries(), REPLACE_EXISTING);
			}

			if (!oldSettings.getPathAPIKey().equals(settings.getPathAPIKey())) {
				Files.move(oldSettings.getPathAPIKey(), settings.getPathAPIKey(), REPLACE_EXISTING);
			}

			if (!oldSettings.getPathBackUp().equals(settings.getPathBackUp())) {
				Files.move(oldSettings.getPathBackUp(), settings.getPathBackUp(), REPLACE_EXISTING);
			}
		} catch (IOException e) {
			popUp.showError("Failed copying old save files!", getStackTrace(e), true);
		}

		APIKey.writeKey(new APIKey(textApiKey.getText(), textUserKey.getText(), textUserName.getText()));
		Settings.writeData(settings);
		back();
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
