package kraisie.data;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kraisie.data.definitions.UserState;
import kraisie.tvdb.TVDB;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Collection {

	private List<Series> series;
	private long lastUpdate;

	public Collection() {
		this.series = new ArrayList<>();
		this.lastUpdate = System.currentTimeMillis();
	}

	public Collection(List<Series> series, long lastUpdate) {
		this.series = series;
		this.lastUpdate = lastUpdate;
	}

	public static Collection readData() {
		Settings settings = Settings.readData();
		if (settings == null) {
			System.err.println("No existing Settings file found. Creating new Collection.");
			return new Collection();
		}

		Path path = settings.getPathSeries();
		if (!path.toFile().exists()) {
			System.err.println("No existing Collection file found. Creating new Collection.");
			return new Collection();
		}

		String json;
		try {
			json = Files.readString(path);
		} catch (IOException e) {
			System.err.println("Could not read Collection file content!");
			return new Collection();
		}

		Gson gson = new Gson();
		return gson.fromJson(json, Collection.class);
	}

	public static void writeData(Collection collection) throws IOException {
		// sort series by name
		List<Series> allSeries = collection.getSeries();
		allSeries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
		collection.setSeries(allSeries);

		Gson gson = new Gson();
		String json = gson.toJson(collection);
		Settings settings = Settings.readData();
		Files.writeString(settings.getPathSeries(), json, TRUNCATE_EXISTING, CREATE);
	}

	public void addNewSeriesById(int id) {
		TVDB api = new TVDB();
		Series newSeries = api.getSeries(id);

		if (seriesExists(id)) {
			return;
		}

		series.add(newSeries);
	}

	public ObservableList<Series> getObservableStarted() {
		return getSeriesByUserState(UserState.WATCHING);
	}

	public ObservableList<Series> getObservableUnstarted() {
		return getSeriesByUserState(UserState.NOT_STARTED);
	}

	private ObservableList<Series> getSeriesByUserState(UserState filter) {
		return FXCollections.observableArrayList(
				series.stream()
						.filter(s -> s.getUserStatus() == filter)
						.collect(Collectors.toList())
		);
	}

	// check air dates

	public boolean seriesExists(int id) {
		String sId = String.valueOf(id);
		for (Series s : series) {
			if (s.getTvdbID().equals(sId)) {
				return true;
			}
		}

		return false;
	}

	public void startSeries(Series startedSeries) {
		int index = series.indexOf(startedSeries);
		Series match = series.get(index);
		match.setUserStatus(UserState.WATCHING);
		match.getEpisodeList().get(0).setCurrent(true);
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
