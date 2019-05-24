package Kraisie.Data;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class BackUp {

	private final long lastSave;
	private final List<MySeries> series;

	/**
	 * Creates a BackUp that contains the creation time of itself and the content that it saves.
	 */
	public BackUp() {
		this.lastSave = System.currentTimeMillis();
		this.series = MySeries.readData();
	}

	/**
	 * Reads the BackUp from the json file specified in the settings. It returns null if there are no settings or the settings do not contain a BackUp path.
	 *
	 * @return BackUp
	 */
	public static BackUp readBackUp() {
		Settings settings = Settings.readData();
		if (settings == null) {
			return null;
		}

		if (!settings.getPathBackUp().toFile().exists()) {
			return null;
		}

		String json;
		try {
			json = Files.readString(settings.getPathBackUp());
		} catch (IOException e) {
			return new BackUp();
		}
		Gson gson = new Gson();

		return gson.fromJson(json, BackUp.class);
	}

	/**
	 * Writes a BackUp to a json file.
	 *
	 * @param backUp an object of type BackUp
	 * @throws IOException when not able to write to file/find file
	 */
	public static void writeBackUp(BackUp backUp) throws IOException {
		Gson gson = new Gson();
		String json = gson.toJson(backUp);
		Settings settings = Settings.readData();
		Files.writeString(settings.getPathBackUp(), json, TRUNCATE_EXISTING, CREATE);
	}

	/**
	 * @return true if the BackUp is older than the BackUp cycle allows
	 * @throws IOException if not able to write BackUp on disk
	 */
	public static boolean checkOldBackUp() throws IOException {
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
