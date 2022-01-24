package kraisie.scenes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import kraisie.data.DataSingleton;
import kraisie.data.Episode;
import kraisie.data.ImageCache;
import kraisie.data.Series;
import kraisie.tvdb.SeriesImage;
import kraisie.tvdb.SeriesPosters;
import kraisie.tvdb.TVDB;

public class InfoController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private Rectangle seriesImage;    // not visible when first loaded

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

	@FXML
	private Label overviewEpisodeLabel;

	@FXML
	private TextArea overviewArea;

	private Image image;
	private Series series;
	private DataSingleton data;
	private int episodePreviewIndex;

	private static final int PANE_MARGIN = 25;

	@FXML
	private void initialize() {
		data = DataSingleton.getInstance();
	}

	public void initData(Series series) {
		this.series = series;
		episodePreviewIndex = series.getEpisodeList().getCurrentIndex();
		loadPoster(series.getTvdbID());
		setResizeListener();
	}

	private void setResizeListener() {
		borderPane.widthProperty().addListener((observable, oldValue, newValue) -> updateContent());
		borderPane.heightProperty().addListener((observable, oldValue, newValue) -> updateContent());
	}

	private void updateContent() {
		setSeriesImage();
		fillInfo();
	}

	private void loadPoster(String tvdbId) {
		ImageCache cache = data.getImageCache();
		int id = Integer.parseInt(tvdbId);
		if (cache.isCached(id)) {
			image = cache.get(id);
			seriesImage.setVisible(true);
		}

		Task<Void> updateTask = new Task<>() {
			@Override
			protected Void call() {
				retrievePoster(cache, id);
				return null;
			}
		};

		Thread thread = new Thread(updateTask, "info-poster-task");
		thread.setDaemon(true);
		thread.start();
	}

	private void retrievePoster(ImageCache cache, int id) {
		TVDB api = data.getApi();
		SeriesPosters posters = api.getSeriesPosters(id);
		if (posters == null) {
			image = TVDB.getFallbackImage();
			seriesImage.setVisible(true);
			return;
		}

		SeriesImage[] seriesImages = posters.getData();
		if (seriesImages.length == 0) {
			image = TVDB.getFallbackImage();
			seriesImage.setVisible(true);
			return;
		}

		String posterName = seriesImages[0].getFileName();
		image = TVDB.getBannerImage(posterName);
		Platform.runLater(this::updateContent);
		if (!cache.isCached(id)) {
			cache.save(image, id);
		}
	}

	private void setSeriesImage() {
		double[] sizes = calcImageViewHeight(image);
		seriesImage.setX(PANE_MARGIN);
		seriesImage.setY(PANE_MARGIN);
		seriesImage.setWidth(sizes[0]);
		seriesImage.setHeight(sizes[1]);
		seriesImage.setArcWidth(20.0);
		seriesImage.setArcHeight(20.0);
		seriesImage.setEffect(new DropShadow(20, Color.BLACK));
		if (image != null) {
			seriesImage.setFill(new ImagePattern(image));
			seriesImage.setVisible(true);
		}
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

	private void fillInfo() {
		seriesName.setText(series.getName());
		seriesOverview.setText(series.getDescription());
		seriesNetwork.setText(series.getNetwork());
		seriesStatus.setText(series.getStatus());
		seriesEpisodeLength.setText(series.getRuntime() + " minutes");
		seriesRating.setText(series.getRating() + " / 10");
		seriesSeasons.setText(series.getCurrentSeason() + " / " + series.getNumberOfSeasons());
		seriesEpisodes.setText(series.getCurrentEpisodeOverall() + " / " + series.getNumberOfEpisodes());
		seriesCompletion.setText(series.getCompletion());
		fillEpisodeOverview();
	}

	private void fillEpisodeOverview() {
		Episode episode = series.getEpisodeList().get(episodePreviewIndex);
		overviewEpisodeLabel.setText(episode.getSeason() + "." + episode.getEpNumberOfSeason());
		String overview = getEpisodeOverview(episode);
		overviewArea.setText(overview);
	}

	private String getEpisodeOverview(Episode episode) {
		if (episode == null) {
			return "No episode overview given!";
		}

		String overview = episode.getOverview();
		if (overview == null) {
			return "No episode overview given!";
		}

		if (overview.isBlank()) {
			return "No episode overview given!";
		}

		return episode.getOverview();
	}

	@FXML
	private void incEpisode() {
		episodePreviewIndex = Math.min(series.getEpisodeList().getEpisodes().size() - 1, episodePreviewIndex + 1);
		fillEpisodeOverview();
	}

	@FXML
	private void decEpisode() {
		episodePreviewIndex = Math.max(0, episodePreviewIndex - 1);
		fillEpisodeOverview();
	}
}
