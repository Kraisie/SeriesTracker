package Kraisie.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Episode {

	private Integer epNumberOfSeason;
	private Integer season;
	private String name;
	private String overview;
	private String firstAired;
	private boolean current;
	private boolean watched;

	/**
	 * Creates a new episode
	 *
	 * @param epNumberOfSeason The number of the episode in a specific season
	 * @param season           The number of the season the episode belongs to
	 * @param name             Name of the episode
	 * @param overview         A small overview of what happens in the episode
	 * @param firstAired       Date as String of the first air date
	 */
	public Episode(int epNumberOfSeason, int season, String name, String overview, String firstAired) {
		this.epNumberOfSeason = epNumberOfSeason;
		this.season = season;
		this.name = name;
		this.overview = overview;
		this.firstAired = firstAired;
		this.current = false;
		this.watched = false;
	}

	/**
	 * Deletes all special episodes that do not belong to official seasons
	 *
	 * @param allEpisodes A list of all episodes that a series has
	 */
	static void deleteNull(List<Episode> allEpisodes) {
		List<Episode> remove = new ArrayList<>();
		for (Episode epi : allEpisodes) {
			if (epi.getSeason() == 0 || epi.getSeason() == null) {
				remove.add(epi);
			}
			if (epi.getEpNumberOfSeason() == 0 || epi.getEpNumberOfSeason() == null) {
				remove.add(epi);
			}
		}

		allEpisodes.removeAll(remove);
	}

	/**
	 * Sorts a list of episodes regarding their season and their episode number
	 *
	 * @param allEpisodes A list of all episodes that a series has
	 */
	public static void sort(List<Episode> allEpisodes) {
		allEpisodes.sort((e1, e2) -> {
			int value1 = e1.getSeason().compareTo(e2.getSeason());
			if (value1 == 0) {
				return e1.getEpNumberOfSeason().compareTo(e2.getEpNumberOfSeason());
			}
			return value1;
		});
	}

	public Integer getEpNumberOfSeason() {
		return epNumberOfSeason;
	}

	public void setEpNumberOfSeason(int epNumberOfSeason) {
		this.epNumberOfSeason = epNumberOfSeason;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	/**
	 * @return true if the air date of the episode is before today or today
	 */
	boolean isAired() {
		if (firstAired.equals("Not given!")) {
			return false;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(firstAired, formatter);
		return (date.isBefore(LocalDate.now()) || date.equals(LocalDate.now()));
	}

	boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	boolean isWatched() {
		return watched;
	}

	public void setWatched(boolean watched) {
		this.watched = watched;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Episode episode = (Episode) o;
		return Objects.equals(epNumberOfSeason, episode.epNumberOfSeason) &&
				Objects.equals(season, episode.season) &&
				Objects.equals(name, episode.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(epNumberOfSeason, season, name);
	}

	@Override
	public String toString() {
		return name;
	}
}
