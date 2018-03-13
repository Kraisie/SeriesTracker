package Data.TVDB;

public class BasicEpisode {

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

    public BasicEpisode(String absoluteNumber, String airedEpisodeNumber, String airedSeason, String dvdEpisodeNumber, String dvdSeason, String episodeName, String firstAired, String id, String lastUpdated, String overview) {
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

    public String getAbsoluteNumber() {
        return absoluteNumber;
    }

    public void setAbsoluteNumber(String absoluteNumber) {
        this.absoluteNumber = absoluteNumber;
    }

    public String getAiredEpisodeNumber() {
        return airedEpisodeNumber;
    }

    public void setAiredEpisodeNumber(String airedEpisodeNumber) {
        this.airedEpisodeNumber = airedEpisodeNumber;
    }

    public String getAiredSeason() {
        return airedSeason;
    }

    public void setAiredSeason(String airedSeason) {
        this.airedSeason = airedSeason;
    }

    public String getDvdEpisodeNumber() {
        return dvdEpisodeNumber;
    }

    public void setDvdEpisodeNumber(String dvdEpisodeNumber) {
        this.dvdEpisodeNumber = dvdEpisodeNumber;
    }

    public String getDvdSeason() {
        return dvdSeason;
    }

    public void setDvdSeason(String dvdSeason) {
        this.dvdSeason = dvdSeason;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
