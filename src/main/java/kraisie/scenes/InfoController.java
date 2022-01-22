package kraisie.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import kraisie.data.DataSingleton;
import kraisie.data.ImageCache;
import kraisie.data.Series;
import kraisie.tvdb.SeriesImage;
import kraisie.tvdb.SeriesPosters;
import kraisie.tvdb.TVDB;

public class InfoController {

	@FXML
	private Button backButton;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Rectangle seriesImage;

	@FXML
	private Label seriesCompletion;

	@FXML
	private Label seriesEpisodeLength;

	@FXML
	private Label seriesEpisodes;

	@FXML
	private Label seriesName;

	@FXML
	private Label seriesNetwork;

	@FXML
	private Label seriesOverview;

	@FXML
	private Label seriesRating;

	@FXML
	private Label seriesSeasons;

	@FXML
	private Label seriesStatus;

	private Image image;
	private Series series;
	private DataSingleton data;

	private static final int PANE_MARGIN = 25;

	@FXML
	private void initialize() {
		data = DataSingleton.getInstance();
	}

	public void initData(Series series) {
		this.series = series;
		this.image = retrievePoster(series.getTvdbID());
		setResizeListener();
	}

	private void setResizeListener() {
		borderPane.widthProperty().addListener((observable, oldValue, newValue) -> updateContent());
		borderPane.heightProperty().addListener((observable, oldValue, newValue) -> updateContent());
	}

	private void updateContent() {
		setSeriesImage();
		setInfo();
	}

	private Image retrievePoster(String tvdbId) {
		ImageCache cache = data.getImageCache();
		int id = Integer.parseInt(tvdbId);
		if (cache.isCached(id)) {
			return cache.get(id);
		}

		TVDB api = data.getApi();
		SeriesPosters posters = api.getSeriesPosters(id);
		if (posters == null) {
			return TVDB.getFallbackImage();
		}

		SeriesImage[] seriesImages = posters.getData();
		if (seriesImages.length == 0) {
			return TVDB.getFallbackImage();
		}

		String posterName = seriesImages[0].getFileName();
		Image banner = TVDB.getBannerImage(posterName);
		if (!cache.isCached(id)) {
			cache.save(banner, id);
		}

		return banner;
	}

	private void setSeriesImage() {
		double[] sizes = calcImageViewHeight(image);
		seriesImage.setX(PANE_MARGIN);
		seriesImage.setY(PANE_MARGIN);
		seriesImage.setWidth(sizes[0]);
		seriesImage.setHeight(sizes[1]);
		seriesImage.setArcWidth(20.0);
		seriesImage.setArcHeight(20.0);
		ImagePattern pattern = new ImagePattern(image);
		seriesImage.setFill(pattern);
		seriesImage.setEffect(new DropShadow(20, Color.BLACK));
	}

	private double[] calcImageViewHeight(Image image) {
		double imgWidth = image.getWidth();
		double imgHeight = image.getHeight();
		double ratio = imgHeight / imgWidth;
		double widthMax = (borderPane.getWidth() / 2) - (6 * PANE_MARGIN);
		double heightMax = borderPane.getHeight() - (6 * PANE_MARGIN);

		if (imgHeight > imgWidth) {
			return new double[]{heightMax / ratio, heightMax};
		} else {
			return new double[]{widthMax, widthMax * ratio};
		}
	}

	private void setInfo() {
		seriesName.setText(series.getName());
		seriesOverview.setText(series.getDescription());
		seriesNetwork.setText(series.getNetwork());
		seriesStatus.setText(series.getStatus());
		seriesEpisodeLength.setText(series.getRuntime() + " minutes");
		seriesRating.setText(series.getRating() + " / 10");
		seriesSeasons.setText(series.getCurrentSeason() + " / " + series.getNumberOfSeasons());
		seriesEpisodes.setText(series.getCurrentEpisodeOverall() + " / " + series.getNumberOfEpisodes());
		seriesCompletion.setText(series.getCompletion());
	}

}
