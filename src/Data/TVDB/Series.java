package Data.TVDB;

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

    public Series(String added, String airsDayOfWeek, String airsTime, String[] aliases, String banner, String firstAired, String[] genre, String id, String imdbId, String lastUpdated, String network, String networkId, String overview, String rating, String runtime, String seriesId, String seriesName, String siteRating, String siteRatingCount, String status, String zap2itId) {
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

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getAirsDayOfWeek() {
        return airsDayOfWeek;
    }

    public void setAirsDayOfWeek(String airsDayOfWeek) {
        this.airsDayOfWeek = airsDayOfWeek;
    }

    public String getAirsTime() {
        return airsTime;
    }

    public void setAirsTime(String airsTime) {
        this.airsTime = airsTime;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSiteRating() {
        return siteRating;
    }

    public void setSiteRating(String siteRating) {
        this.siteRating = siteRating;
    }

    public String getSiteRatingCount() {
        return siteRatingCount;
    }

    public void setSiteRatingCount(String siteRatingCount) {
        this.siteRatingCount = siteRatingCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getZap2itId() {
        return zap2itId;
    }

    public void setZap2itId(String zap2itId) {
        this.zap2itId = zap2itId;
    }
}
