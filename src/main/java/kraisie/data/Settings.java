package kraisie.data;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Settings {

	private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Settings.json");
	private String pathAPIKey;
	private String pathSeries;
	private boolean sortByCompletion;
	private boolean sortByTime;
	private String langIso;
	private int[] prefSize;
	private boolean cycleBackgrounds;
	private int backgroundCycle;    // every x minutes
	private int fadeDuration;        // x millis TODO: slider in settings e.g. 100-2500

	public Settings() {
		this.pathAPIKey = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/API_Key.json").toString();
		this.pathSeries = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Series.json").toString();
		this.sortByCompletion = false;
		this.sortByTime = false;
		this.langIso = "en";
		this.prefSize = new int[]{1280, 720};
		this.cycleBackgrounds = true;
		this.backgroundCycle = 1;
		this.fadeDuration = 500;
	}

	public Settings(String pathAPIKey, String pathSeries, boolean sortByCompletion, boolean sortByTime, String langIso, int[] prefSize, boolean cycleBackgrounds, int backgroundCycle, int fadeDuration) {
		this.pathAPIKey = pathAPIKey;
		this.pathSeries = pathSeries;
		this.langIso = langIso;
		this.prefSize = prefSize;
		this.cycleBackgrounds = cycleBackgrounds;
		this.backgroundCycle = backgroundCycle;
		this.fadeDuration = fadeDuration;
		selectSorting(sortByCompletion, sortByTime);
	}

	public static Settings readData() {
		if (PATH == null) {
			createSettingsSave();
			return new Settings();
		}

		if (!PATH.toFile().exists()) {
			createSettingsSave();
			return new Settings();
		}

		String json;
		try {
			json = Files.readString(PATH);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: log error, could not read settings file content
			createSettingsSave();
			return new Settings();
		}

		Gson gson = new Gson();
		return gson.fromJson(json, Settings.class);
	}

	private static void createSettingsSave() {
		try {
			Files.createDirectories(PATH.getParent());
			Files.createFile(PATH);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: log error, could not create save location for settings
		}

		writeData(new Settings());
	}

	public static void writeData(Settings settings) {
		Gson gson = new Gson();
		String json = gson.toJson(settings);
		try {
			Files.writeString(PATH, json, TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: log error, could not write to file/error while saving
		}
	}

	private void selectSorting(boolean sortByCompletion, boolean sortByTime) {
		if (sortByCompletion && sortByTime) {
			this.sortByCompletion = false;
			this.sortByTime = false;
		} else {
			this.sortByCompletion = sortByCompletion;
			this.sortByTime = sortByTime;
		}
	}

	public boolean isValid() {
		if (this.sortByCompletion && this.sortByTime) {
			sortByCompletion = false;
			sortByTime = false;
		}

		if (!getPathAPIKey().toFile().exists()) {
			return false;
		}

		if (!getPathSeries().toFile().exists()) {
			return false;
		}

		return prefSize[0] > 0 && prefSize[1] > 0;
	}

	public Path getPathAPIKey() {
		return Paths.get(pathAPIKey);
	}

	public void setPathAPIKey(Path pathAPIKey) {
		this.pathAPIKey = pathAPIKey.toString();
	}

	public Path getPathSeries() {
		return Paths.get(pathSeries);
	}

	public void setPathSeries(Path pathSeries) {
		this.pathSeries = pathSeries.toString();
	}

	public boolean isSortByCompletion() {
		return sortByCompletion;
	}

	public void setSortByCompletion(boolean sortByCompletion) {
		this.sortByCompletion = sortByCompletion;
	}

	public boolean isSortByTime() {
		return sortByTime;
	}

	public void setSortByTime(boolean sortByTime) {
		this.sortByTime = sortByTime;
	}

	public String getLangIso() {
		return langIso;
	}

	public void setLangIso(String langIso) {
		this.langIso = langIso;
	}

	public int[] getPrefSize() {
		return prefSize;
	}

	public void setPrefSize(int[] prefSize) {
		this.prefSize = prefSize;
	}

	public boolean isCycleBackgrounds() {
		return cycleBackgrounds;
	}

	public void setCycleBackgrounds(boolean cycleBackgrounds) {
		this.cycleBackgrounds = cycleBackgrounds;
	}

	public int getBackgroundCycle() {
		return backgroundCycle;
	}

	public void setBackgroundCycle(int backgroundCycle) {
		this.backgroundCycle = backgroundCycle;
	}

	public int getFadeDuration() {
		return fadeDuration;
	}

	public void setFadeDuration(int fadeDuration) {
		this.fadeDuration = fadeDuration;
	}
}
