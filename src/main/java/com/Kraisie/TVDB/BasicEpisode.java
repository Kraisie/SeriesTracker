package com.Kraisie.TVDB;

class BasicEpisode {

	private String absoluteNumber;
	private String airedEpisodeNumber;
	private String airedSeason;
	private String dvdEpisodeNumber;
	private String dvdSeason;
	private String episodeName;
	private String firstAired;
	private String id;
	private String lastUpdated;
	private String overview;

	BasicEpisode(String absoluteNumber, String airedEpisodeNumber, String airedSeason, String dvdEpisodeNumber, String dvdSeason, String episodeName, String firstAired, String id, String lastUpdated, String overview) {
		this.absoluteNumber = absoluteNumber;
		this.airedEpisodeNumber = airedEpisodeNumber;
		this.airedSeason = airedSeason;
		this.dvdEpisodeNumber = dvdEpisodeNumber;
		this.dvdSeason = dvdSeason;
		this.episodeName = episodeName;
		this.firstAired = firstAired;
		this.id = id;
		this.lastUpdated = lastUpdated;
		this.overview = overview;
	}

	String getAbsoluteNumber() {
		return absoluteNumber;
	}

	String getAiredEpisodeNumber() {
		return airedEpisodeNumber;
	}

	String getAiredSeason() {
		return airedSeason;
	}

	String getDvdEpisodeNumber() {
		return dvdEpisodeNumber;
	}

	String getDvdSeason() {
		return dvdSeason;
	}

	String getEpisodeName() {
		return episodeName;
	}

	String getFirstAired() {
		return firstAired;
	}

	String getId() {
		return id;
	}

	String getLastUpdated() {
		return lastUpdated;
	}

	String getOverview() {
		return overview;
	}
}
