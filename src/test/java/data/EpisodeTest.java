package data;

import kraisie.data.Episode;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class EpisodeTest {

	@Test
	public void airedEpisodeIsAiredTest() {
		Episode airedEpisode = new Episode(1, 1, "name", "overview", "2000-01-01");
		Assert.assertTrue(airedEpisode.isAired());
	}

	@Test
	public void nonAiredEpisodeIsNotAired() {
		Episode notAiredEpisode = new Episode(1, 1, "name", "overview", LocalDate.MAX.toString());
		Assert.assertFalse(notAiredEpisode.isAired());
	}

	@Test
	public void todayAiredEpisodeIsAired() {
		LocalDate today = LocalDate.now();
		Episode todayEpisode = new Episode(1, 1, "name", "overview", today.toString());
		Assert.assertTrue(todayEpisode.isAired());
	}

}
