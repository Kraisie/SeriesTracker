package kraisie;

import javafx.application.Application;
import javafx.stage.Stage;
import kraisie.data.DataSingleton;
import kraisie.data.definitions.Scenes;
import kraisie.ui.SceneLoader;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		doStartUpTask(primaryStage);
		openScene(primaryStage);
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

	@Override
	public void stop() {
		DataSingleton.save();
	}
}
