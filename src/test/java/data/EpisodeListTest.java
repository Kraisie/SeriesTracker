package data;

import kraisie.data.Episode;
import kraisie.data.EpisodeList;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EpisodeListTest {

	@Test
	public void isIncrementableTest() {
		List<Episode> episodes = buildEpisodeList();
		episodes.get(0).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);

		Assert.assertTrue(episodeList.isIncrementable());
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
		return episodes;
	}

	@Test
	public void isIncrementableBeforeEndTest() {
		List<Episode> episodes = buildEpisodeList();
		int lastEpisodeIndex = episodes.size() - 2;
		episodes.get(lastEpisodeIndex).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);

		Assert.assertTrue(episodeList.isIncrementable());
	}

	@Test
	public void isNotIncrementableEndReachedTest() {
		List<Episode> episodes = buildEpisodeList();
		int lastEpisodeIndex = episodes.size() - 1;
		episodes.get(lastEpisodeIndex).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);

		Assert.assertFalse(episodeList.isIncrementable());
	}

	@Test
	public void isNotIncrementableUnairedEpisodeTest() {
		List<Episode> episodes = buildEpisodeList();
		episodes.add(new Episode(4, 1, "name8", "overview8", LocalDate.MAX.toString()));
		int lastAiredEpisodeIndex = episodes.size() - 2;
		episodes.get(lastAiredEpisodeIndex).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);

		Assert.assertFalse(episodeList.isIncrementable());
	}

	@Test
	public void isNotIncrementableEmptyListTest() {
		List<Episode> episodes = new ArrayList<>();
		EpisodeList episodeList = new EpisodeList(episodes);
		Assert.assertFalse(episodeList.isIncrementable());
	}

	@Test
	public void incrementCurrentIndexTest() {
		int currentIndex = 0;
		List<Episode> episodes = buildEpisodeList();
		episodes.get(currentIndex).setCurrent(true);

		EpisodeList episodeList = new EpisodeList(episodes);
		episodeList.incrementCurrent();

		int incCurrentIndex = episodeList.getEpisodes().indexOf(episodeList.getCurrent());
		Assert.assertEquals(currentIndex + 1, incCurrentIndex);
	}

	@Test
	public void incrementCurrentTest() {
		int currentIndex = 0;
		int incCurrentIndex = currentIndex + 1;
		List<Episode> episodes = buildEpisodeList();
		episodes.get(currentIndex).setCurrent(true);

		EpisodeList episodeList = new EpisodeList(episodes);
		episodeList.incrementCurrent();

		Episode incCurrent = episodeList.getCurrent();
		Assert.assertEquals(incCurrent, episodeList.get(incCurrentIndex));
	}

	@Test
	public void isDecrementableTest() {
		List<Episode> episodes = buildEpisodeList();
		episodes.get(1).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);

		Assert.assertTrue(episodeList.isDecrementable());
	}

	@Test
	public void isNotDecrementableFirstEpisodeTest() {
		List<Episode> episodes = buildEpisodeList();
		episodes.get(0).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);

		Assert.assertFalse(episodeList.isDecrementable());
	}

	@Test
	public void isDecrementableLastEpisodeTest() {
		List<Episode> episodes = buildEpisodeList();
		episodes.get(episodes.size() - 1).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);

		Assert.assertTrue(episodeList.isDecrementable());
	}

	@Test
	public void decrementCurrentTest() {
		List<Episode> episodes = buildEpisodeList();
		episodes.get(1).setCurrent(true);
		EpisodeList episodeList = new EpisodeList(episodes);
		episodeList.decrementCurrent();

		Assert.assertEquals(episodes.get(0), episodeList.getCurrent());
	}
}
