package Data;

public class SearchParameter {

	private int rating;
	private int duration;
	private int derivation;
	private int seasons;
	private int userState;
	private String status;

	public SearchParameter(int rating, int duration, int derivation, int seasons, int userState, String status) {
		this.rating = rating;
		this.duration = duration;
		this.derivation = derivation;
		this.seasons = seasons;
		this.userState = userState;
		this.status = status;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDerivation() {
		return derivation;
	}

	public void setDerivation(int derivation) {
		this.derivation = derivation;
	}

	public int getSeasons() {
		return seasons;
	}

	public void setSeasons(int seasons) {
		this.seasons = seasons;
	}

	public int getUserStatus() {
		return userState;
	}

	public void setUserStatus(int userStatus) {
		this.userState = userStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
