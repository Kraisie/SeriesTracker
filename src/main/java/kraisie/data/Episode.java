package kraisie.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Episode {

	private int epNumberOfSeason;
	private int season;
	private String name;
	private String overview;
	private String firstAired;
	private boolean current;

	public Episode(int epNumberOfSeason, int season, String name, String overview, String firstAired) {
		this.epNumberOfSeason = epNumberOfSeason;
		this.season = season;
		this.name = name;
		this.overview = overview;
		this.firstAired = firstAired;
		this.current = false;
	}

	public boolean isAired() {
		if (firstAired.isBlank()) {
			return false;
		}

		LocalDate today = LocalDate.now();
		LocalDate airDate = LocalDate.parse(firstAired, DateTimeFormatter.ISO_LOCAL_DATE);

		return !airDate.isAfter(today);
	}

	public int getEpNumberOfSeason() {
		return epNumberOfSeason;
	}

	public void setEpNumberOfSeason(int epNumberOfSeason) {
		this.epNumberOfSeason = epNumberOfSeason;
	}

	public int getSeason() {
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

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Episode episode = (Episode) o;

		if (epNumberOfSeason != episode.epNumberOfSeason) return false;
		return season == episode.season;
	}

	@Override
	public int hashCode() {
		int result = epNumberOfSeason;
		result = 31 * result + season;
		return result;
	}
}
