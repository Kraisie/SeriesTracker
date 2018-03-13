package Data.TVDB;

public class SeriesSearchData {

    private SearchData[] data;

    public SeriesSearchData(SearchData[] data) {
        this.data = data;
    }

    public SearchData[] getData() {
        return data;
    }

    public void setData(SearchData[] data) {
        this.data = data;
    }
}
