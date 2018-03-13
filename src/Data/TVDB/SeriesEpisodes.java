package Data.TVDB;

public class SeriesEpisodes {

    private BasicEpisode[] data;
    private JSONErrors errors;
    private Links links;

    public SeriesEpisodes(BasicEpisode[] data, JSONErrors errors, Links links) {
        this.data = data;
        this.errors = errors;
        this.links = links;
    }

    public BasicEpisode[] getData() {
        return data;
    }

    public void setData(BasicEpisode[] data) {
        this.data = data;
    }

    public JSONErrors getErrors() {
        return errors;
    }

    public void setErrors(JSONErrors errors) {
        this.errors = errors;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
