package kraisie.tvdb;

public class SeriesPosters {

	private SeriesImage[] data;

	public SeriesPosters(SeriesImage[] data) {
		this.data = data;
	}

	public SeriesImage[] getData() {
		return data;
	}

	public void setData(SeriesImage[] data) {
		this.data = data;
	}
}
