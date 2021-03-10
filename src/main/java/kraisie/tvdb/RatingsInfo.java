package kraisie.tvdb;

public class RatingsInfo {

	private int average;
	private int count;

	public RatingsInfo(int average, int count) {
		this.average = average;
		this.count = count;
	}

	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
