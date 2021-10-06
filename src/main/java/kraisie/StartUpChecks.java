package kraisie;

import javafx.stage.Stage;
import kraisie.data.APIKey;
import kraisie.data.DataSingleton;
import kraisie.data.Settings;
import kraisie.data.definitions.Scenes;
import kraisie.ui.SceneLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StartUpChecks {

	private final Stage primaryStage;
	private final DataSingleton data;

	public StartUpChecks(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.data = DataSingleton.getInstance();
	}

	public void run() {
		if (isReadMeExpired()) {
			deleteReadMe();
		}

		if (!isSettingsValid()) {
			saveStandardSettings();
		}

		// TODO: rest of startup checks
		// check if backup is older than cycle allows, if yes create a new backup
		// check if new episodes aired, if yes open scene with affected series

		if (!isApiKeyValid()) {
			openApiKeyForm();
		} else {
			// TODO: if token added persistent read it and check if it says "NoConnection", if yes open popup no connection
		}
	}

	private boolean isReadMeExpired() {
		Path path = getReadMePath();
		if (path.toFile().exists()) {
			return isExpired(path);
		}

		return false;
	}

	private Path getReadMePath() {
		String parsedFilePath = System.getProperty("user.home") + "/SERIESTRACKER/README.html";
		return Paths.get(parsedFilePath);
	}

	private boolean isExpired(Path parsedFile) {
		LocalDateTime creation = getFileCreationDate(parsedFile);
		return LocalDateTime.now().minusDays(7).isAfter(creation);
	}

	private LocalDateTime getFileCreationDate(Path parsedFile) {
		LocalDateTime creation = LocalDateTime.now();
		try {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
			BasicFileAttributes attributes = Files.readAttributes(parsedFile, BasicFileAttributes.class);
			creation = LocalDateTime.parse(attributes.creationTime().toString(), format);
		} catch (IOException e) {
			System.err.println("Can not access file properties!");
		}

		return creation;
	}

	private void deleteReadMe() {
		Path path = getReadMePath();
		File file = path.toFile();

		boolean success = file.delete();
		if (!success && file.exists()) {
			System.err.println("Can not delete ReadMe!");
		}
	}

	private boolean isSettingsValid() {
		DataSingleton data = DataSingleton.getInstance();
		Settings settings = data.getSettings();
		return settings.isValid();
	}

	private void saveStandardSettings() {
		Settings settings = new Settings();
		Settings.writeData(settings);
	}

	private boolean isApiKeyValid() {
		APIKey key = APIKey.readKey();
		return key.isValid();
	}

	private void openApiKeyForm() {
		SceneLoader loader = new SceneLoader(new Stage(), Scenes.API_KEY);
		loader.openNewScene();
	}
}
