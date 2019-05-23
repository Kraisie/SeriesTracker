package com.Kraisie.Data;

public class SearchParameter {

	private int rating;
	private int duration;
	private int deviation;
	private int seasons;
	private int userState;
	private String status;

	/**
	 * Collects all search parameters that a series needs to fulfill in a search
	 *
	 * @param rating    the minimum rating which should not be undercut by a series
	 * @param duration  the wanted duration of an average episode
	 * @param deviation the maximum deviation from the desired duration
	 * @param seasons   the desired number of seasons
	 * @param userState the userState that a series should have
	 * @param status    the status a series should have
	 * @see MySeries
	 */
	public SearchParameter(int rating, int duration, int deviation, int seasons, int userState, String status) {
		this.rating = rating;
		this.duration = duration;
		this.deviation = deviation;
		this.seasons = seasons;
		this.userState = userState;
		this.status = status;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDeviation() {
		return deviation;
	}

	public void setDeviation(int deviation) {
		this.deviation = deviation;
	}

	public int getSeasons() {
		return seasons;
	}

	public void setSeasons(int seasons) {
		this.seasons = seasons;
	}

	public int getUserStatus() {
		return userState;
	}

	public void setUserStatus(int userStatus) {
		this.userState = userStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
