package kraisie.tvdb;

public class SearchData {

	private String[] aliases;
	private String banner;
	private String firstAired;
	private int id;
	private String network;
	private String overview;
	private String seriesName;
	private String status;

	SearchData(String[] aliases, String banner, String firstAired, int id, String network, String overview, String seriesName, String status) {
		this.aliases = aliases;
		this.banner = banner;
		this.firstAired = firstAired;
		this.id = id;
		this.network = network;
		this.overview = overview;
		this.seriesName = seriesName;
		this.status = status;
	}

	public String[] getAliases() {
		return aliases;
	}

	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getFirstAired() {
		return firstAired;
	}

	public void setFirstAired(String firstAired) {
		this.firstAired = firstAired;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
