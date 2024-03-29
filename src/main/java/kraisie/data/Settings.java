package kraisie.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kraisie.data.definitions.CacheSize;
import kraisie.util.LogUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Settings {

	private String langIso;
	private boolean cycleBackgrounds;
	private int backgroundCycle;    // every x seconds
	private int fadeDuration;
	private boolean cacheBanners;
	private CacheSize maxCacheSize;

	private static final Path PATH = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Settings.json");
	private static final String PATH_API_KEY = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/API_Key.json").toString();
	private static final String PATH_SERIES = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/Series.json").toString();
	private static final String PATH_CACHE = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/.cache").toString();

	public Settings() {
		this.langIso = "en";
		this.cycleBackgrounds = true;
		this.backgroundCycle = 60;
		this.fadeDuration = 500;
		this.cacheBanners = true;
		this.maxCacheSize = CacheSize.UNLIMITED;
	}

	public Settings(String langIso, boolean cycleBackgrounds, int backgroundCycle, int fadeDuration,
					boolean cacheBanners, CacheSize maxCacheSize) {
		this.langIso = langIso;
		this.cycleBackgrounds = cycleBackgrounds;
		this.backgroundCycle = backgroundCycle;
		this.fadeDuration = fadeDuration;
		this.cacheBanners = cacheBanners;
		this.maxCacheSize = maxCacheSize;
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
			LogUtil.logError("Could not read settings file content! Creating new settings file.", e);
			createSettingsSave();
			return new Settings();
		}

		Gson gson = new Gson();
		return gson.fromJson(json, Settings.class);
	}

	private static void createSettingsSave() {
		try {
			createDirectories();
			Files.createFile(PATH);
		} catch (IOException e) {
			LogUtil.logError("Could not create save location for settings!", e);
		}

		writeData(new Settings());
	}

	private static void createDirectories() throws IOException {
		Path parent = PATH.getParent();
		if (parent == null) {
			throw new IOException("Parent path is null! " + PATH);
		}

		Path dirs = Files.createDirectories(parent);
		if (dirs == null) {
			throw new IOException("Directory path is null! " + parent);
		}
	}

	public static void writeData(Settings settings) {
		// save as pretty json as users might edit this by hand
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(settings);
		try {
			Files.writeString(PATH, json, TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			LogUtil.logError("Error while saving settings!", e);
		}
	}

	public Path getPathAPIKey() {
		return Paths.get(PATH_API_KEY);
	}

	public Path getPathSeries() {
		return Paths.get(PATH_SERIES);
	}

	public Path getPathCache() {
		return Paths.get(PATH_CACHE);
	}

	public String getLangIso() {
		return langIso;
	}

	public void setLangIso(String langIso) {
		this.langIso = langIso;
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

	public boolean shouldCacheBanners() {
		return cacheBanners;
	}

	public void setCacheBanners(boolean cacheBanners) {
		this.cacheBanners = cacheBanners;
	}

	public CacheSize getMaxCacheSize() {
		return maxCacheSize;
	}

	public void setMaxCacheSize(CacheSize maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}
}
