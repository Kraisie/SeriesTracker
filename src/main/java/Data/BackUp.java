package Data;

import Dialog.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class BackUp {

	private final long lastSave;
	private final List<MySeries> series;

	public BackUp() {
		this.lastSave = System.currentTimeMillis();
		this.series = MySeries.readData();
	}

	/*
	 *  read the backUp from the json file
	 */
	public static BackUp readBackUp() {
		Settings settings = Settings.readData();
		if (!settings.getPathBackUp().toFile().exists()) {
			PopUp popUp = new PopUp();
			popUp.showError("No BackUp available!", "You do not have a local BackUp available. Please make sure it is at the right location.", false);
			return null;
		}

		String json;
		try {
			json = new String(Files.readAllBytes(settings.getPathBackUp()));
		} catch (IOException e) {
			return new BackUp();
		}
		Gson gson = new Gson();

		return gson.fromJson(json, BackUp.class);
	}

	/*
	 *  write a backUp to a json file
	 */
	public static void writeBackUp(BackUp backUp) {
		Gson gson = new Gson();
		String json = gson.toJson(backUp);
		Settings settings = Settings.readData();
		try {
			Files.write(settings.getPathBackUp(), json.getBytes(), TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("BackUp failed!", "The BackUp failed. Please check the validity of your Path.", false);
		}
	}

	/*
	 *  checks if the backUp is older than the chosen backUp cycle allows
	 */
	public static boolean checkOldBackUp() {
		Settings settings = Settings.readData();

		if (settings == null) {
			// shouldn't get here since Settings get created on start, but maybe the cheeky user deletes his settings
			settings = new Settings();
			Settings.writeData(settings);
		}

		BackUp backUp = readBackUp();

		// check if backUp exists
		if (backUp == null) {
			backUp = new BackUp();
			BackUp.writeBackUp(backUp);
			return false;
		}

		// return true if backUp is older than backUpCycle * 24 hours (ms) = x days
		return (System.currentTimeMillis() - backUp.lastSave) >= settings.getBackUpCycle() * 86400000L;
	}

	public List<MySeries> getSeries() {
		return series;
	}
}
