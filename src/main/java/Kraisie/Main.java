package Kraisie;

import Kraisie.Data.BackUp;
import Kraisie.Data.MySeries;
import Kraisie.Data.Settings;
import Kraisie.Dialog.PopUp;
import Kraisie.SceneController.MainSeriesController;
import Kraisie.TVDB.APIKey;
import Kraisie.TVDB.TVDB_Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static Kraisie.Data.BackUp.checkOldBackUp;
import static Kraisie.Data.BackUp.writeBackUp;
import static Kraisie.Data.Settings.checkSettings;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class Main extends Application {

	private PopUp popUp = new PopUp();

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		startUp(Settings.readData(), primaryStage);

		// start program / open scene
		try {
			openScene(primaryStage, "/FXML/MainSeries.fxml");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, primaryStage);
		}
	}

	/**
	 * Starts the startUp routine
	 *
	 * @param settings     Settings read from disk if available
	 * @param primaryStage Stage to display scenes in
	 */
	private void startUp(Settings settings, Stage primaryStage) {
		boolean firstStart = startUpCheckSettings(settings, primaryStage);
		startUpCheckBackUp(firstStart, settings, primaryStage);
		startUpCheckApiKey(primaryStage);
		startUpCheckNewEpisodes(primaryStage);
	}

	/**
	 * Checks if settings exist and creates new Settings if they don't
	 *
	 * @param settings     Settings read from disk if available
	 * @param primaryStage Stage to center the PopUp to
	 * @return true if it is a clear start and no settings are available
	 */
	private boolean startUpCheckSettings(Settings settings, Stage primaryStage) {
		// create new Settings if file does not exist
		boolean clearStart = false;
		if (!checkSettings(settings)) {
			try {
				Settings.writeData(new Settings());
				clearStart = true;
			} catch (IOException e) {
				popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, primaryStage);
			}
		}

		return clearStart;
	}

	/**
	 * Checks if the BackUp is too old and creates a new one if it is
	 *
	 * @param clearStart   indicates if the start was a fresh start, if it is it does not need to read any BackUp
	 * @param settings     Settings read from disk or created by first startUpCheck
	 * @param primaryStage Stage to center the PopUp to
	 */
	private void startUpCheckBackUp(boolean clearStart, Settings settings, Stage primaryStage) {
		// create BackUp if last BackUp is older than 24 hours/back up cycle in settings
		if (!clearStart && settings.getPathBackUp().toFile().exists()) {
			try {
				if (checkOldBackUp()) {
					writeBackUp(new BackUp());
				}
			} catch (IOException e) {
				popUp.showError("BackUp failed!", "The BackUp failed. Please check the validity of your Path.", false, primaryStage);
			}
		}
	}

	/**
	 * Checks if API Key is available and valid
	 *
	 * @param primaryStage Stage to open a request for the API Key in or center the PopUp to
	 */
	private void startUpCheckApiKey(Stage primaryStage) {
		// check API Key
		APIKey key = APIKey.readKey();
		TVDB_Data validation = new TVDB_Data(key);

		if (key == null || !validation.keyValid()) {
			// request a valid API Key
			try {
				openScene(primaryStage, "/FXML/ApiKeyForm.fxml");
			} catch (IOException e) {
				popUp.showError("Failed to open the scene!", getStackTrace(e), true, primaryStage);
			}
		}
	}

	/**
	 * Checks if new episodes aired and informs the user
	 *
	 * @param primaryStage Stage to center the PopUp to
	 */
	private void startUpCheckNewEpisodes(Stage primaryStage) {
		// check for newly aired episodes
		List<MySeries> updatedSeries = MySeries.checkAirDates();

		// save changes if necessary
		if (updatedSeries.size() > 0) {
			List<MySeries> allSeries = MySeries.readData();
			for (MySeries series : allSeries) {
				if (updatedSeries.contains(series)) {
					allSeries.set(allSeries.indexOf(series), updatedSeries.get(updatedSeries.indexOf(series)));
				}
			}

			try {
				MySeries.writeData(allSeries);
			} catch (IOException e) {
				popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, primaryStage);
			}

			StringBuilder sb = new StringBuilder();
			for (MySeries series : updatedSeries) {
				sb.append(series.getName()).append("\n");
			}
			popUp.showAlert(updatedSeries.size() + " series got modified. They may have some new content you do not want to miss!", sb.toString(), true, primaryStage);
		}
	}

	/**
	 * opens a scene by fxml
	 *
	 * @param primaryStage stage to open scene in
	 * @param fxmlPath     Path to FXML file which contains the content of the scene
	 * @throws IOException if fxml or icon file can not be read
	 * @see MainSeriesController
	 */
	private void openScene(Stage primaryStage, String fxmlPath) throws IOException {
		URL resource = getClass().getResource("/Pics/Icon/series.png");

		if (resource != null) {
			Image img = new Image(resource.toString());
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
			Parent root = loader.load();

			MainSeriesController controller = loader.getController();
			primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, window -> controller.initBackground());


			primaryStage.setTitle("Series Control Panel");
			primaryStage.getIcons().add(img);
			primaryStage.setScene(new Scene(root));
			primaryStage.sizeToScene();
			primaryStage.show();    // XSetErrorHandler() called with a GDK error trap pushed
		} else {
			throw new FileNotFoundException();
		}
	}
}
