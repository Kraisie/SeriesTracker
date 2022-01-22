package kraisie.data;

import kraisie.tvdb.TVDB;

import java.io.IOException;

// not thread safe, not synchronised
public final class DataSingleton {

	private static DataSingleton instance;
	private static Collection collection;
	private static Settings settings;
	private static TVDB api;
	private static ImageCache imageCache;

	private DataSingleton() {
		collection = Collection.readData();
		settings = Settings.readData();
		api = new TVDB();
		imageCache = new ImageCache(settings);
	}

	public static DataSingleton getInstance() {
		if (DataSingleton.instance == null) {
			DataSingleton.instance = new DataSingleton();
		}

		return DataSingleton.instance;
	}

	public Collection getCollection() {
		return collection;
	}

	public Settings getSettings() {
		return settings;
	}

	public TVDB getApi() {
		if (api.isTokenExpired()) {
			api = new TVDB();
		}

		return api;
	}

	public ImageCache getImageCache() {
		return imageCache;
	}

	public static void save() throws IOException {
		Collection.writeData(collection);
		Settings.writeData(settings);
	}
}
