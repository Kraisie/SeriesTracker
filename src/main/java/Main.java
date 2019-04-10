import Data.BackUp;
import Data.MySeries;
import Data.Settings;
import Dialog.PopUp;
import TVDB.APIKey;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static Data.BackUp.checkOldBackUp;
import static Data.BackUp.writeBackUp;
import static Data.Settings.checkSettings;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		PopUp popUp = new PopUp();

		// create new Settings if file does not exist
		boolean clearStart = false;
		Settings settings = Settings.readData();
		if (!checkSettings(settings)) {
			Settings.writeData(new Settings());
			clearStart = true;
		}

		// create BackUp if last BackUp is older than 24 hours/back up cycle in settings
		if(!clearStart && settings.getPathBackUp().toFile().exists()) {
			if (checkOldBackUp()) {
				writeBackUp(new BackUp());
			}
		}

		// check API Key
		APIKey key = APIKey.readKey();
		if (key == null) {
			// request an API Key
			try {
				openScene(primaryStage, "/FXML/ApiKeyForm.fxml");
				return;
			} catch (IOException e) {
				popUp.showError("Failed to open the scene!", getStackTrace(e), true);
			}
		}

		// check for newly aired episodes
		List<MySeries> updatedSeries = MySeries.checkAirDates();

		if (updatedSeries.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (MySeries series : updatedSeries) {
				sb.append(series.getName()).append("\n");
			}

			popUp.showAlert(updatedSeries.size() + " series got modified they may have some new content you do not want to miss!", sb.toString(), true);
		}

		// start program / open scene
		try {
			openScene(primaryStage, "/FXML/MainSeries.fxml");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
		}
	}

	/**
	 * opens a scene by fxml
	 *
	 * @param primaryStage stage to open scene in
	 * @throws IOException if fxml or icon file can not be read
	 * @see SceneController.MainSeriesController
	 */
	private void openScene(Stage primaryStage, String fxmlPath) throws IOException {
		URL resource = getClass().getResource("/Pics/Icon/series.png");

		if (resource != null) {
			Image img = new Image(resource.toString());
			Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
			primaryStage.setTitle("Series Control Panel");
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(img);
			primaryStage.setScene(new Scene(root));
			primaryStage.sizeToScene();
			primaryStage.show();
		} else {
			throw new FileNotFoundException();
		}
	}
}
