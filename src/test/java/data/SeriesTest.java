package data;

import kraisie.data.Episode;
import kraisie.data.EpisodeList;
import kraisie.data.Series;
import kraisie.data.definitions.UserState;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SeriesTest {

	@Test
	public void updateUserStatusEndedTest() {
		Series series = buildEndedSeries();
		series.increaseWatchProgress();
	}

	private Series buildEndedSeries() {
		List<Episode> episodes = buildEpisodeList();
		EpisodeList episodeList = new EpisodeList(episodes);
		return new Series("name", "1", episodeList, UserState.WATCHING, "Ended", 20, "description", 10d, "/banner/1.jpg", "Netflix");
	}

	private List<Episode> buildEpisodeList() {
		List<Episode> episodes = new ArrayList<>();
		episodes.add(new Episode(1, 1, "name1", "overview1", "2000-01-01"));
		episodes.add(new Episode(1, 2, "name2", "overview2", "2000-01-02"));
		episodes.add(new Episode(2, 1, "name3", "overview3", "2002-02-19"));
		episodes.add(new Episode(2, 2, "name4", "overview4", "2002-02-20"));
		episodes.add(new Episode(2, 3, "name5", "overview5", "2002-02-21"));
		episodes.add(new Episode(3, 1, "name6", "overview6", "2007-11-11"));
		episodes.add(new Episode(3, 2, "name7", "overview7", "2007-12-12"));
		episodes.get(6).setCurrent(true);
		return episodes;
	}
}
