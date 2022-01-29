package kraisie;

import javafx.application.Application;
import javafx.stage.Stage;
import kraisie.data.DataSingleton;
import kraisie.data.definitions.Scenes;
import kraisie.dialog.ExceptionHandler;
import kraisie.dialog.PopUp;
import kraisie.ui.SceneLoader;
import kraisie.util.LogUtil;

import java.io.IOException;

public class Main extends Application {

	public static void main(String[] args) {
		setupLogger();
		launch(args);
	}

	private static void setupLogger() {
		String logFileLocation = System.getProperty("user.home") + "/SERIESTRACKER/seriestracker.log";
		System.setProperty("org.slf4j.simpleLogger.logFile", logFileLocation);
	}

	public void start(Stage primaryStage) {
		addExceptionHandler();
		addCloseHandler(primaryStage);
		doStartUpTask(primaryStage);
		openScene(primaryStage);
	}

	private void addExceptionHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
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
