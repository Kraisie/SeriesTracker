package Data;

public class Episode {

    private int epNumberOfSeason;
    private int season;
    private String name;
    private String overview;

    public Episode(int epNumberOfSeason, int season, String name, String overview){
        this.epNumberOfSeason = epNumberOfSeason;
        this.season = season;
        this.name = name;
        this.overview = overview;
    }

    public int getEpNumberOfSeason() {
        return epNumberOfSeason;
    }

    public void setEpNumberOfSeason(int epNumberOfSeason) {
        this.epNumberOfSeason = epNumberOfSeason;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public String toString() {
        return name;
    }
}
