package kraisie.data;

import java.util.List;

public class EpisodeList {

	private final List<Episode> episodes;

	public EpisodeList(List<Episode> episodes) {
		this.episodes = episodes;
	}

	public List<Episode> getEpisodes() {
		return episodes;
	}

	public int getCurrentSeason() {
		if (getCurrent() == null) {
			return 0;
		}

		return getCurrent().getSeason();
	}

	public int getCurrentEpisode() {
		if (getCurrent() == null) {
			return 0;
		}

		return getCurrent().getEpNumberOfSeason();
	}

	public int getNumberOfSeenEpisodes() {
		return Math.max(0, getCurrentIndex());
	}

	public int getNumberOfSeasons() {
		if (episodes.size() == 0) {
			return 0;
		}

		Episode episode = episodes.get(episodes.size() - 1);
		return episode.getSeason();
	}

	public int getNumberOfEpisodes() {
		return episodes.size();
	}

	public Episode getCurrent() {
		for (Episode ep : episodes) {
			if (ep.isCurrent()) {
				return ep;
			}
		}

		return null;
	}

	public Episode get(int index) {
		return episodes.get(index);
	}

	public boolean isIncrementable() {
		if (!hasNextEpisode()) {
			return false;
		}

		return hasNextAiredEpisode();
	}

	private boolean hasNextEpisode() {
		int nextIndex = getCurrentIndex() + 1;
		if (nextIndex == -1) {
			return false;
		}

		int maxIndex = episodes.size() - 1;
		return nextIndex <= maxIndex;
	}

	private int getCurrentIndex() {
		return episodes.indexOf(getCurrent());
	}

	private boolean hasNextAiredEpisode() {
		// some episodes do not have air dates but later ones have.
		// because of that we need to check all later episodes for an air date to make sure.
		for (int i = getCurrentIndex() + 1; i < episodes.size(); i++) {
			Episode next = episodes.get(i);
			if (next.isAired()) {
				return true;
			}
		}

		return false;
	}

	private Episode getNextEpisode() {
		int nextIndex = getCurrentIndex() + 1;
		return episodes.get(nextIndex);
	}

	public void incrementCurrent() {
		Episode current = getCurrent();
		Episode next = getNextEpisode();

		current.setCurrent(false);
		next.setCurrent(true);
	}

	public boolean isDecrementable() {
		return getCurrentIndex() > 0;
	}

	public void decrementCurrent() {
		Episode current = getCurrent();
		Episode prev = getPreviousEpisode();

		current.setCurrent(false);
		prev.setCurrent(true);
	}

	private Episode getPreviousEpisode() {
		int prevIndex = getCurrentIndex() - 1;
		return episodes.get(prevIndex);
	}
}
