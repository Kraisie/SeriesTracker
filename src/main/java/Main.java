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
		if (!checkSettings(Settings.readData())) {
			Settings.writeData(new Settings());
		}

		// check API Key
		APIKey key = APIKey.readKey();
		if (key == null) {
			popUp.showWarning("Missing API Key!", "Please set an API-Key in the settings to use the program.");
		}

		// create BackUp if last BackUp is older than 24 hours/back up cycle in settings
		if (checkOldBackUp()) {
			writeBackUp(new BackUp());
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
			openScene(primaryStage);
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
		}
	}


	/*
	 *	open the main scene
	 */
	private void openScene(Stage primaryStage) throws IOException {
		URL resource = getClass().getResource("/Pics/Icon/series.png");

		if (resource != null) {
			Image img = new Image(resource.toString());
			Parent root = FXMLLoader.load(getClass().getResource("/FXML/MainSeries.fxml"));
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
