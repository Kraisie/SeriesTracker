package Data;

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

	public Episode(int epNumberOfSeason, int season, String name, String overview, String firstAired) {
		this.epNumberOfSeason = epNumberOfSeason;
		this.season = season;
		this.name = name;
		this.overview = overview;
		this.firstAired = firstAired;
		this.current = false;
		this.watched = false;
	}

	/*
	 *  delete all episodes that have epNumberOfSeason or season = null/0 since those are just specials
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

	/*
	 *  sort list regarding the seasons and the episodes
	 */
	public static void sort(List<Episode> allEpisodes) {
		//
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

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public boolean isWatched() {
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
