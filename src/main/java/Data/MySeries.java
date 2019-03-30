package Data;

import Dialog.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class MySeries {

	private String name;
	private String tvdbID;
	private List<Episode> episodes;
	private int userState;      //0=not started; 1=watching; 2=wait for new episodes; 3=finished
	private String status;      //Continuing / Ended
	private int runtime;
	private String description;
	private double rating;
	private String banner;

	public MySeries(String name, String tvdbID, List<Episode> episodes, int userState, String status, int runtime, String description, double rating, String banner) {
		this.name = name;
		this.tvdbID = tvdbID;
		this.episodes = episodes;
		this.userState = userState;
		this.status = status;
		this.runtime = runtime;
		this.description = description;
		this.rating = rating;
		this.banner = banner;
	}

	/*
	 *  read the list of series from the json file
	 */
	public static List<MySeries> readData() {
		String json;
		Settings settings = Settings.readData();
		if (settings == null) {
			return new ArrayList<>();
		}

		try {
			json = new String(Files.readAllBytes(settings.getPathSeries()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return new ArrayList<>();
		}

		Gson gson = new Gson();
		MySeries[] entries = gson.fromJson(json, MySeries[].class);

		return new ArrayList<>(Arrays.asList(entries));
	}

	/*
	 *  write the ordered list of series to a json file
	 */
	public static void writeData(List<MySeries> allEntries) {
		for (MySeries series : allEntries) {
			Episode.deleteNull(series.episodes);
			Episode.sort(series.episodes);
		}

		//sort series by name
		allEntries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

		Gson gson = new Gson();
		String json = gson.toJson(allEntries);
		Settings settings = Settings.readData();

		try {
			Files.write(settings.getPathSeries(), json.getBytes(StandardCharsets.UTF_8), TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false);
		}
	}

	/*
	 *  checks if there are any new episodes which aired and monitors status changes
	 */
	public static List<MySeries> checkAirDates() {
		List<MySeries> allEntries = MySeries.readData();
		List<MySeries> updatedSeries = new ArrayList<>();
		for (MySeries series : allEntries) {
			if (series.getUserState() == 3) {
				if (series.getStatus().equals("Ended")) {
					continue;
				}

				series.setUserState(2);
				updatedSeries.add(series);
			}

			//if there are episodes after current in a series with UserState 2 and a date before today or today set UserState to 1
			if (series.getUserState() == 2) {
				if (series.getStatus().equals("Ended")) {
					series.setUserState(3);
					updatedSeries.add(series);
					continue;
				}

				int index = series.getEpisodes().indexOf(series.getCurrent());
				if (!series.hasNext() || series.getEpisodes().get(index + 1).getFirstAired().equals("Not given!")) {
					continue;
				}

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(series.getEpisodes().get(index + 1).getFirstAired(), formatter);
				if (!date.isBefore(LocalDate.now()) && !date.equals(LocalDate.now())) {
					continue;
				}

				series.setUserState(1);
				series.setNewCurrent(series.getCurrent(), true);    //add 1 episode as we are changing from last episode of a season to the first episode of a new season
				updatedSeries.add(series);
			}
		}

		//save changes if necessary
		if (updatedSeries.size() > 0) {
			MySeries.writeData(allEntries);
		}

		return updatedSeries;
	}

	/*
	 *  check if a series is already tracked
	 */
	public static boolean checkDuplicate(List<MySeries> allSeries, String name) {
		if (allSeries.isEmpty()) {
			return false;
		}

		for (MySeries series : allSeries) {
			if (name.equals(series.getName())) {
				return true;
			}
		}

		return false;
	}

	/*
	 *  converts the calculated time to a more readable format depending on the number
	 */
	public static String wastedMinutesToString(int time) {
		// if lower than 2 hours print minutes, lower than 7 days print hours, else use days
		if (time < 120) {
			return time + " minutes wasted";
		} else if (time < (7 * 24 * 60)) {
			String wastedTime = String.format("%.2f", ((double) time / 60));
			return wastedTime + " hours wasted";
		} else {
			String wastedTime = String.format("%.2f", (((double) time / 60) / 24));
			return wastedTime + " days wasted";
		}
	}

	/*
	 *  get the current episode to watch
	 */
	public Episode getCurrent() {
		for (Episode ep : episodes) {
			if (ep.isCurrent()) {
				return ep;
			}
		}

		return null;
	}

	/*
	 *  set the current episode to watch
	 */
	public void setCurrent(Episode newCurrent) {
		//just sets watched right for newCurrent after current, if it is before it won't change the ones behind
		for (Episode ep : episodes) {
			if (ep.equals(newCurrent)) {
				ep.setCurrent(true);
				break;
			}
			ep.setWatched(true);
		}
	}

	/*
	 *  check if the series got a episode after the current one
	 */
	public boolean hasNext() {
		Episode current = getCurrent();
		int index = episodes.indexOf(current);

		return index < episodes.size() - 1;
	}

	/*
	 *  getCurrentSeason, getCurrentEpisode and getNumberOfSeasons are methods for the main menu tables to get their needed data
	 */
	public int getCurrentSeason() {
		if (getCurrent() == null) {
			return -1;
		}

		return getCurrent().getSeason();
	}

	public int getCurrentEpisode() {
		if (getCurrent() == null) {
			return -1;
		}

		return getCurrent().getEpNumberOfSeason();
	}

	public int getNumberOfSeasons() {
		Episode episode = episodes.get(episodes.size() - 1);
		return episode.getSeason();
	}

	/*
	 *  set new current episode 1 next to the current one depending on which button clicked (inc/dec)
	 */
	public void setNewCurrent(Episode current, boolean direction) {         //true = ++ ; false = --
		int pos = episodes.indexOf(current);
		if (pos < 0 || (pos + 1) >= episodes.size()) {
			return;
		}

		if (direction) {
			setCurrent(episodes.get(pos + 1));
		} else {
			setCurrent(episodes.get(pos - 1));
		}
		episodes.get(pos).setCurrent(false);
	}

	/*
	 *  calculates the completion of a series as a percentage
	 */
	public Double getCompletionRate() {
		double sum = 0;
		double size = 0;
		for (Episode epi : episodes) {
			if (epi.isWatched()) {
				sum++;
			}

			if (epi.isAired()) {
				size++;
			}
		}

		return (sum / size) * 100;
	}

	/*
	 *  calculates the approximate 'wasted' time on a series
	 */
	public int getWastedTime() {
		int sum = 0;
		for (Episode epi : episodes) {
			if (epi.isWatched()) {
				sum++;
			}
		}

		return sum * runtime;
	}

	/*
	 *	get needed time to end the series
	 */
	public int getTimeToEnd() {
		int sum = 0;
		for (Episode epi : episodes) {
			if (!epi.isWatched() && epi.isAired()) {
				sum++;
			}
		}

		return sum * runtime;
	}

	/*
	 *  calculates the sum of all episodes a series got
	 */
	public int getSumEpisodesOfSeason(Episode current) {
		int sum = 0;
		List<Episode> allEpisodes = this.getEpisodes();
		for (Episode episode : allEpisodes) {
			if (episode.getSeason().equals(current.getSeason())) {
				sum++;
			}
		}

		return sum;
	}

	public int getSumEpisodes() {
		return episodes.size();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTvdbID() {
		return tvdbID;
	}

	public void setTvdbID(String tvdbID) {
		this.tvdbID = tvdbID;
	}

	public List<Episode> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Episode> episodes) {
		this.episodes = episodes;
	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MySeries series = (MySeries) o;
		return Objects.equals(name, series.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
