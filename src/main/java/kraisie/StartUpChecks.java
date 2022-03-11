package kraisie;

import javafx.application.Platform;
import javafx.stage.Stage;
import kraisie.data.APIKey;
import kraisie.data.DataSingleton;
import kraisie.data.Series;
import kraisie.data.definitions.Scenes;
import kraisie.dialog.PopUp;
import kraisie.ui.SceneLoader;

import java.util.List;

public class StartUpChecks {

	private final Stage primaryStage;
	private final DataSingleton data;

	public StartUpChecks(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.data = DataSingleton.getInstance();
	}

	public void run() {
		// check each series for episodes that aired
		checkAirDates();

		// check for the api key
		APIKey apiKey = APIKey.readKey();
		if (apiKey.isInvalid()) {
			openApiKeyForm();
		}
	}

	private void openApiKeyForm() {
		Stage newStage = buildStage();
		SceneLoader loader = new SceneLoader(newStage, Scenes.API_KEY);
		loader.openNewScene();
	}

	private Stage buildStage() {
		Stage newStage = new Stage();
		newStage.setOnCloseRequest(e -> {
			e.consume();
			PopUp popUp = PopUp.forStage(newStage);
			boolean quit = popUp.showChoice("API key required to continue!", "Do you really want to stop the program?");
			if (quit) {
				Platform.exit();
				System.exit(0);
			}
		});

		return newStage;
	}
	
	private void checkAirDates() {
		List<Series> newContent = data.getCollection().checkAirDates();
		if (newContent.size() > 0) {
			displayNewContent(newContent);
		}
	}

	private void displayNewContent(List<Series> newContent) {
		String seriesList = buildSeriesNameList(newContent);
		PopUp popUp = PopUp.forStage(primaryStage);
		String verb = newContent.size() == 1 ? "is" : "are";
		popUp.showAlert("There " + verb + " " + newContent.size() + " series with new content!", seriesList, true);
	}

	private String buildSeriesNameList(List<Series> series) {
		StringBuilder sb = new StringBuilder();
		for (Series s : series) {
			sb.append(s.getName()).append("\n");
		}

		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
