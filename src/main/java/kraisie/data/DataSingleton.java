package kraisie.data;

import java.io.IOException;

// not thread safe, not synchronised
public final class DataSingleton {

	private static DataSingleton instance;
	private static Collection collection;
	private static Settings settings;

	private DataSingleton() {
		collection = Collection.readData();
		settings = Settings.readData();
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

	public static void save() {
		try {
			Collection.writeData(collection);
			Settings.writeData(settings);
		} catch (IOException e) {
			// TODO: log error, show popup
			e.printStackTrace();
		}
	}
}
