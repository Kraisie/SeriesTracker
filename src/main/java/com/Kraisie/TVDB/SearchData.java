package com.Kraisie.TVDB;

class SearchData {

	private String[] aliases;
	private String banner;
	private String firstAired;
	private int id;
	private String network;
	private String overview;
	private String seriesName;
	private String status;

	SearchData(String[] aliases, String banner, String firstAired, int id, String network, String overview, String seriesName, String status) {
		this.aliases = aliases;
		this.banner = banner;
		this.firstAired = firstAired;
		this.id = id;
		this.network = network;
		this.overview = overview;
		this.seriesName = seriesName;
		this.status = status;
	}

	String[] getAliases() {
		return aliases;
	}

	String getBanner() {
		return banner;
	}

	String getFirstAired() {
		return firstAired;
	}

	int getId() {
		return id;
	}

	String getNetwork() {
		return network;
	}

	String getOverview() {
		return overview;
	}

	String getSeriesName() {
		return seriesName;
	}

	String getStatus() {
		return status;
	}
}
