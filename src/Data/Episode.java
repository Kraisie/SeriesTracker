package Data;

import java.util.List;

public class Episode {

    private Integer epNumberOfSeason;
    private Integer season;
    private String name;
    private String overview;
    private boolean current;
    private boolean watched;

    public Episode(int epNumberOfSeason, int season, String name, String overview){
        this.epNumberOfSeason = epNumberOfSeason;
        this.season = season;
        this.name = name;
        this.overview = overview;
        this.current = false;
        this.watched = false;
    }

    public static List<Episode> deleteNull(List<Episode> allEpisodes){
        //Delete all Episodes that have epNumberOfSeason or season = null/0 since those are just specials
        for(Episode epi : allEpisodes){
            if(epi.getSeason() == 0 || epi.getSeason() == null){
                allEpisodes.remove(epi);
            }else if(epi.getEpNumberOfSeason() == 0 || epi.getEpNumberOfSeason() == null){
                allEpisodes.remove(epi);
            }
        }

        return allEpisodes;
    }

    public static List<Episode> sort(List<Episode> allEpisodes){
        //Sort list regarding the season first and after that the episodes
        allEpisodes.sort((e1, e2) -> {
            int value1 = e1.getSeason().compareTo(e2.getSeason());
            if (value1 == 0) {
                return e1.getEpNumberOfSeason().compareTo(e2.getEpNumberOfSeason());
            }
            return value1;
        });

        return allEpisodes;
    }

    public Integer getEpNumberOfSeason() {
        return epNumberOfSeason;
    }

    public void setEpNumberOfSeason(int epNumberOfSeason) {
        this.epNumberOfSeason = epNumberOfSeason;
    }

    public Integer getSeason() {
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

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    @Override
    public String toString() {
        return name;
    }
}
