package kraisie.data;

import kraisie.data.definitions.UserState;

public class Series {

	private String name;
	private String tvdbID;
	private EpisodeList episodeList;
	private UserState userStatus;
	private String status;
	private int runtime;
	private String description;
	private double rating;
	private String banner;
	private String network;

	public Series(String name, String tvdbID, EpisodeList episodeList, UserState userState, String status, int runtime, String description, double rating, String banner, String network) {
		this.name = name;
		this.tvdbID = tvdbID;
		this.episodeList = episodeList;
		this.userStatus = userState;
		this.status = status;
		this.runtime = runtime;
		this.description = description;
		this.rating = rating;
		this.banner = banner;
		this.network = network;
	}

	/*
	 *  getCurrentSeason, getCurrentEpisode and getNumberOfSeasons are methods for the main menu tables to get their needed data
	 */
	public int getCurrentSeason() {
		return episodeList.getCurrentSeason();
	}

	public int getCurrentEpisode() {
		return episodeList.getCurrentEpisode();
	}

	public int getCurrentEpisodeOverall() {
		return episodeList.getNumberOfSeenEpisodes();
	}

	public String getCompletion() {
		double completion = ((double) getCurrentEpisodeOverall() / getNumberOfEpisodes()) * 100d;
		return String.format("%.2f", completion) + "%";
	}

	public int getNumberOfSeasons() {
		return episodeList.getNumberOfSeasons();
	}

	public int getNumberOfEpisodes() {
		return episodeList.getNumberOfEpisodes();
	}

	public void increaseWatchProgress() {
		if (!episodeList.isIncrementable()) {
			updateUserStatus();
			return;
		}

		episodeList.incrementCurrent();
	}

	private void updateUserStatus() {
		userStatus = status.equalsIgnoreCase("Ended") ? UserState.FINISHED : UserState.WAITING;
	}

	public void decreaseWatchProgress() {
		if (!episodeList.isDecrementable()) {
			return;
		}

		episodeList.decrementCurrent();
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

	public EpisodeList getEpisodeList() {
		return episodeList;
	}

	public void setEpisodeList(EpisodeList episodeList) {
		this.episodeList = episodeList;
	}

	public UserState getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserState userStatus) {
		this.userStatus = userStatus;
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

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public boolean isContinuing() {
		return status.equalsIgnoreCase("Continuing");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Series series = (Series) o;

		return tvdbID.equals(series.tvdbID);
	}

	@Override
	public int hashCode() {
		return tvdbID.hashCode();
	}
}
