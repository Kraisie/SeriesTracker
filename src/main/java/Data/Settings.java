package Data;

import Dialog.PopUp;
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

	private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Settings.json");

	/*
	 * GSON CAN NOT SAVE A PATH AS IT HAS A CYCLIC REFERENCE IN IT!
	 * fs -> WindowsFileSystem -> provider -> WindowsFileSystem -> provider -> ...
	 * THAT'S WHY WE SAVE THE PATH AS STRING AND RETURN THEM AS PATH IN THE GETTER
	 * AND GET THEM AS PATH IN THE SETTER WHILST SAVING THEM AS STRINGS
	 */

	/*
	 *   Constructor that sets the standard values
	 */
	public Settings() {
		this.pathAPIKey = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/API_Key.json").toString();
		this.pathSeries = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Series.json").toString();
		this.pathBackUp = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/BackUp.json").toString();
		this.backUpCycle = 1;
		this.sortByCompletion = false;
		this.sortByTime = false;
	}

	/*
	 *   Constructor that receives modified settings from the user
	 */
	public Settings(String pathSeries, String pathBackUp, int backUpCycle, boolean sortByCompletion, boolean sortByTime) {
		this.pathSeries = pathSeries;
		this.pathBackUp = pathBackUp;
		this.backUpCycle = backUpCycle;

		if(sortByCompletion && sortByTime) {
			this.sortByCompletion = false;
			this.sortByTime = false;
		} else {
			this.sortByCompletion = sortByCompletion;
			this.sortByTime = sortByTime;
		}
	}

	/*
	 *   Get the settings from the json-file
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
			json = new String(Files.readAllBytes(PATH), StandardCharsets.UTF_8);
		} catch (IOException e) {
			json = null;
		}

		Gson gson = new Gson();
		return gson.fromJson(json, Settings.class);
	}


	/*
	 *   Write the settings to a json-file
	 */
	public static void writeData(Settings settings) {
		Gson gson = new Gson();
		String json = gson.toJson(settings);
		try {
			Files.write(PATH, json.getBytes(StandardCharsets.UTF_8), TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false);
		}
	}

	/*
	 *  returns true if Settings.json exists
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
}
