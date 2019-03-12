package Data;

import Dialog.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
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
	private boolean sortByName;    // false = sort by less time to completion

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
		pathAPIKey = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/API_Key.json").toString();
		pathSeries = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Series.json").toString();
		pathBackUp = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/BackUp.json").toString();
		backUpCycle = 1;
		sortByName = true;
	}

	/*
	 *   Constructor that receives modified settings from the user
	 */
	public Settings(String pathSeries, String pathBackUp, int backUpCycle, boolean sortByName) {
		this.pathSeries = pathSeries;
		this.pathBackUp = pathBackUp;
		this.backUpCycle = backUpCycle;
		this.sortByName = sortByName;
	}

	/*
	 *   Get the settings from the json-file
	 */
	public static Settings readData() {
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
			json = new String(Files.readAllBytes(PATH));
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
			Files.write(PATH, json.getBytes(), TRUNCATE_EXISTING, CREATE);
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

		return settings.getPathSeries() != null &&
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

	public boolean isSortByName() {
		return sortByName;
	}

	public void setSortByName(boolean sortByName) {
		this.sortByName = sortByName;
	}
}
