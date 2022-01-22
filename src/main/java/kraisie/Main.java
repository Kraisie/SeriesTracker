package kraisie;

import javafx.application.Application;
import javafx.stage.Stage;
import kraisie.data.DataSingleton;
import kraisie.data.definitions.Scenes;
import kraisie.dialog.LogUtil;
import kraisie.dialog.PopUp;
import kraisie.ui.SceneLoader;

import java.io.IOException;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		addCloseHandler(primaryStage);
		doStartUpTask(primaryStage);
		openScene(primaryStage);
	}

	private void addCloseHandler(Stage primaryStage) {
		primaryStage.setOnCloseRequest(event -> {
			try {
				DataSingleton.save();
			} catch (IOException e) {
				LogUtil.logError("Could not save data!", e);
				PopUp popUp = PopUp.forStage(primaryStage);
				popUp.showError("Could not save data! Check the log for further details.", e, true);
				event.consume();
			}
		});
	}

	private void doStartUpTask(Stage primaryStage) {
		DataSingleton.getInstance();    // set up singleton data
		StartUpChecks checks = new StartUpChecks(primaryStage);
		checks.run();
	}

	private void openScene(Stage stage) {
		SceneLoader loader = new SceneLoader(stage, Scenes.MOTHER);
		loader.loadMotherScene();
	}
}
