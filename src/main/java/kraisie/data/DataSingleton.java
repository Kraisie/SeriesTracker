package kraisie.data;

import kraisie.tvdb.TVDB;

import java.io.IOException;

// not thread safe, not synchronised
public final class DataSingleton {

	private static DataSingleton instance;
	private static Collection collection;
	private static Settings settings;
	private static TVDB api;

	private DataSingleton() {
		collection = Collection.readData();
		settings = Settings.readData();
		api = new TVDB();
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

	public static TVDB getApi() {
		if (api.isTokenExpired()) {
			api = new TVDB();
		}

		return api;
	}

	public static void save() {
		try {
			Collection.writeData(collection);
			Settings.writeData(settings);
		} catch (IOException e) {
			// TODO: log error, show popup -> requires stage -> return something/throw exception/cont catch it here
			e.printStackTrace();
		}
	}
}
