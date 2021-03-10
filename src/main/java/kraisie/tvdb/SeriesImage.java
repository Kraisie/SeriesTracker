package kraisie.tvdb;

public class SeriesImage {

	private String fileName;
	private int id;
	private String keyType;
	private int languageId;
	private RatingsInfo ratingsInfo;
	private String resolution;
	private String subKey;
	private String thumbnail;

	public SeriesImage(String fileName, int id, String keyType, int languageId, RatingsInfo ratingsInfo, String resolution, String subKey, String thumbnail) {
		this.fileName = fileName;
		this.id = id;
		this.keyType = keyType;
		this.languageId = languageId;
		this.ratingsInfo = ratingsInfo;
		this.resolution = resolution;
		this.subKey = subKey;
		this.thumbnail = thumbnail;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public RatingsInfo getRatingsInfo() {
		return ratingsInfo;
	}

	public void setRatingsInfo(RatingsInfo ratingsInfo) {
		this.ratingsInfo = ratingsInfo;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSubKey() {
		return subKey;
	}

	public void setSubKey(String subKey) {
		this.subKey = subKey;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
