package kraisie.tvdb;

class SeriesSearchData {

	private SearchData[] data;

	SeriesSearchData(SearchData[] data) {
		this.data = data;
	}

	SearchData[] getData() {
		return data;
	}
}
