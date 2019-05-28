package Kraisie.TVDB;

public class Series {

	private String added;
	private String airsDayOfWeek;
	private String airsTime;
	private String[] aliases;
	private String banner;
	private String firstAired;
	private String[] genre;
	private String id;
	private String imdbId;
	private String lastUpdated;
	private String network;
	private String networkId;
	private String overview;
	private String rating;
	private String runtime;
	private String seriesId;
	private String seriesName;
	private String siteRating;
	private String siteRatingCount;
	private String status;
	private String zap2itId;

	Series(String added, String airsDayOfWeek, String airsTime, String[] aliases, String banner, String firstAired, String[] genre, String id, String imdbId, String lastUpdated, String network, String networkId, String overview, String rating, String runtime, String seriesId, String seriesName, String siteRating, String siteRatingCount, String status, String zap2itId) {
		this.added = added;
		this.airsDayOfWeek = airsDayOfWeek;
		this.airsTime = airsTime;
		this.aliases = aliases;
		this.banner = banner;
		this.firstAired = firstAired;
		this.genre = genre;
		this.id = id;
		this.imdbId = imdbId;
		this.lastUpdated = lastUpdated;
		this.network = network;
		this.networkId = networkId;
		this.overview = overview;
		this.rating = rating;
		this.runtime = runtime;
		this.seriesId = seriesId;
		this.seriesName = seriesName;
		this.siteRating = siteRating;
		this.siteRatingCount = siteRatingCount;
		this.status = status;
		this.zap2itId = zap2itId;
	}

	String getAdded() {
		return added;
	}

	String getAirsDayOfWeek() {
		return airsDayOfWeek;
	}

	String getAirsTime() {
		return airsTime;
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

	String[] getGenre() {
		return genre;
	}

	String getId() {
		return id;
	}

	String getImdbId() {
		return imdbId;
	}

	String getLastUpdated() {
		return lastUpdated;
	}

	String getNetwork() {
		return network;
	}

	String getNetworkId() {
		return networkId;
	}

	String getOverview() {
		return overview;
	}

	String getRating() {
		return rating;
	}

	String getRuntime() {
		return runtime;
	}

	String getSeriesId() {
		return seriesId;
	}

	String getSeriesName() {
		return seriesName;
	}

	String getSiteRating() {
		return siteRating;
	}

	String getSiteRatingCount() {
		return siteRatingCount;
	}

	String getStatus() {
		return status;
	}

	String getZap2itId() {
		return zap2itId;
	}
}
