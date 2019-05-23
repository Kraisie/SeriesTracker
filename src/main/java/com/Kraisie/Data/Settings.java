package com.Kraisie.Data;

import com.Kraisie.Dialog.PopUp;
import com.Kraisie.TVDB.APIKey;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Settings {

	private String pathAPIKey;
	private String pathSeries;
	private String pathBackUp;
	private int backUpCycle;      // every x days BackUp
	private boolean sortByCompletion;
	private boolean sortByTime;
	private String langIso;

	private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Settings.json");

	/**
	 * Constructor that sets the standard values
	 */
	public Settings() {
		this.pathAPIKey = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/API_Key.json").toString();
		this.pathSeries = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Series.json").toString();
		this.pathBackUp = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/BackUp.json").toString();
		this.backUpCycle = 1;
		this.sortByCompletion = false;
		this.sortByTime = false;
		this.langIso = "en";
	}

	/**
	 * Gson can not save a Path as those have a cyclic reference in them.
	 * 'fs' to 'WindowsFileSystem' to 'provider' to 'WindowsFileSystem' to 'provider' to ...
	 * That's why the Path gets saved as String.
	 * If sortByCompletion and sortByTime are both true both get set to false and thus everything gets sorted by name
	 *
	 * @param pathAPIKey       Path to the json file that saves the APIKey
	 * @param pathSeries       Path to the json file that saves all series (MySeries)
	 * @param pathBackUp       Path to the json file that saves the BackUp
	 * @param backUpCycle      number of days until a new BackUp gets created
	 * @param sortByCompletion saves if the tables in the main menu should get sorted by completion rate
	 * @param sortByTime       saves if the tables in the main menu should get sorted by less time to completion
	 * @param langIso          saves the language which should get used when getting data from the TVDB API in ISO639-2
	 * @see APIKey
	 * @see MySeries
	 * @see BackUp
	 */
	public Settings(String pathAPIKey, String pathSeries, String pathBackUp, int backUpCycle, boolean sortByCompletion, boolean sortByTime, String langIso) {
		this.pathAPIKey = pathAPIKey;
		this.pathSeries = pathSeries;
		this.pathBackUp = pathBackUp;
		this.backUpCycle = backUpCycle;
		this.langIso = langIso;

		if (sortByCompletion && sortByTime) {
			this.sortByCompletion = false;
			this.sortByTime = false;
		} else {
			this.sortByCompletion = sortByCompletion;
			this.sortByTime = sortByTime;
		}
	}

	/**
	 * Reads settings from a json file
	 *
	 * @return Settings
	 */
	public static Settings readData() {
		if (PATH == null) {
			return new Settings();
		}

		String json;
		if (!PATH.toFile().exists()) {
			// if the path to the files does not exist, create it
			try {
				Files.createDirectories(PATH.getParent());
				Files.createFile(PATH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			json = Files.readString(PATH);
		} catch (IOException e) {
			json = null;
		}

		Gson gson = new Gson();
		return gson.fromJson(json, Settings.class);
	}


	/**
	 * Writes the settings to a json-file
	 *
	 * @param settings object of type Settings
	 */
	public static void writeData(Settings settings) {
		Gson gson = new Gson();
		String json = gson.toJson(settings);
		try {
			Files.writeString(PATH, json, TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false);
		}
	}

	/**
	 * @param settings object of type Settings
	 * @return true if all paths are set
	 */
	public static boolean checkSettings(Settings settings) {
		if (settings == null) return false;

		return settings.getPathAPIKey() != null &&
				settings.getPathSeries() != null &&
				settings.getPathBackUp() != null &&
				settings.getBackUpCycle() != 0;
	}


	public Path getPathAPIKey() {
		return Paths.get(pathAPIKey);
	}

	public void setPathAPIKey(Path path) {
		pathAPIKey = path.toString();
	}

	public Path getPathSeries() {
		return Paths.get(pathSeries);
	}

	public void setPathSeries(Path path) {
		pathSeries = path.toString();
	}

	public Path getPathBackUp() {
		return Paths.get(pathBackUp);
	}

	public void setPathBackUp(Path path) {
		pathBackUp = path.toString();
	}

	public int getBackUpCycle() {
		return backUpCycle;
	}

	public void setBackUpCycle(int cycle) {
		backUpCycle = cycle;
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
}
