package kraisie.tvdb;

import javafx.scene.image.Image;

public class SearchResult {

	private SearchData searchData;
	private Image poster;

	public SearchResult(SearchData searchData, SeriesPosters images) {
		this.searchData = searchData;
		this.poster = getTvdbPoster(images);
	}

	public SearchData getSearchData() {
		return searchData;
	}

	public String getSeriesName() {
		return searchData.getSeriesName();
	}

	public Image getPoster() {
		return poster;
	}

	private Image getTvdbPoster(SeriesPosters images) {
		if (images == null) {
			return TVDB.getFallbackImage();
		}

		SeriesImage[] seriesImages = images.getData();
		if (seriesImages.length == 0) {
			return TVDB.getFallbackImage();
		}

		String posterName = seriesImages[0].getFileName();
		return TVDB.getBannerImage(posterName);
	}
}
