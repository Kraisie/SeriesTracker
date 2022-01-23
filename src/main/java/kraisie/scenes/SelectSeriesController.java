package kraisie.scenes;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import kraisie.data.Collection;
import kraisie.data.DataSingleton;
import kraisie.data.ImageCache;
import kraisie.data.definitions.Scenes;
import kraisie.dialog.LogUtil;
import kraisie.tvdb.SearchData;
import kraisie.tvdb.SeriesImage;
import kraisie.tvdb.SeriesPosters;
import kraisie.tvdb.TVDB;
import kraisie.ui.SceneLoader;

import java.util.ArrayList;
import java.util.List;

public class SelectSeriesController {

	@FXML
	private BorderPane borderPane;
	@FXML
	private Pagination pagination;

	private int objectsPerRow;
	private int objectsPerColumn;
	private int objectsPerPage;
	private boolean seriesFocused;
	private SearchData selectedSeries;

	private static final int BTN_PREF_WIDTH = 175;
	private static final int BTN_PREF_HEIGHT = 250;
	private static final int GRID_H_GAP = 25;
	private static final int GRID_V_GAP = 15;

	private DataSingleton data;
	private List<SearchData> searchData;

	@FXML
	private void initialize() {
		data = DataSingleton.getInstance();
	}

	public void initData(List<SearchData> data) {
		this.searchData = data;
		// listeners get triggered when pagination gets rendered (first for width, then height)
		setResizeListener();
		setPageFactoryGrid();
	}

	private void setResizeListener() {
		pagination.widthProperty().addListener((observable, oldValue, newValue) -> updatePagination());
		pagination.heightProperty().addListener((observable, oldValue, newValue) -> updatePagination());
	}

	private void updatePagination() {
		double width = pagination.getWidth();
		double height = pagination.getHeight();

		if (width != 0 && height != 0) {
			fitPageToSize(width, height);
		}
	}

	private void fitPageToSize(double width, double height) {
		if (seriesFocused) {
			pagination.setPageCount(1);
			Platform.runLater(this::setPageFactoryInfo);
		} else {
			calcObjectsPerPage(width, height);
			pagination.setPageCount(calcPageCount());        // TODO: prevent flickering of page buttons on resize
			Platform.runLater(this::setPageFactoryGrid);
		}
	}

	private void calcObjectsPerPage(double width, double height) {
		objectsPerColumn = (int) width / (BTN_PREF_WIDTH + (GRID_H_GAP * 2));
		objectsPerRow = (int) height / (BTN_PREF_HEIGHT + (GRID_V_GAP * 2));
		objectsPerPage = Math.min((objectsPerColumn * objectsPerRow), searchData.size());
	}

	private int calcPageCount() {
		return (int) Math.ceil((double) searchData.size() / objectsPerPage);
	}

	private void setPageFactoryGrid() {
		pagination.setPageFactory(this::buildPage);
	}

	private VBox buildPage(int pageIndex) {
		// to center content in Pagination
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER);

		GridPane grid = new GridPane();
		grid.setHgap(GRID_H_GAP);
		grid.setVgap(GRID_V_GAP);
		grid.setPadding(new Insets(GRID_V_GAP, GRID_H_GAP, GRID_V_GAP, GRID_H_GAP));

		List<SearchData> series = getPageContentList(pageIndex);
		fillGrid(grid, series);

		hBox.getChildren().add(grid);
		vBox.getChildren().add(hBox);
		return vBox;
	}


	private void fillGrid(GridPane grid, List<SearchData> series) {
		int listIndex = 0;
		for (int i = 0; i < objectsPerRow; i++) {
			for (int j = 0; j < objectsPerColumn; j++) {
				if (listIndex >= series.size()) {
					return;
				}

				Button btn = buildButton(series, listIndex);
				GridPane.setRowIndex(btn, i);
				GridPane.setColumnIndex(btn, j);
				grid.getChildren().add(btn);

				listIndex++;
			}
		}
	}

	private Button buildButton(List<SearchData> series, int listIndex) {
		String seriesName = series.get(listIndex).getSeriesName();
		Button btn = new Button(seriesName);

		setButtonProperties(btn);
		setToolTipToButton(btn, series, listIndex);
		setGraphicToButton(btn, series, listIndex);     // TODO: query in another thread, dont preload all images for the buttons on a page

		return btn;
	}

	private List<SearchData> getPageContentList(int pageIndex) {
		int startIndex = pageIndex * objectsPerPage;
		int endIndex = Math.min(startIndex + objectsPerPage, searchData.size());

		List<SearchData> series = new ArrayList<>();
		for (int i = startIndex; i < endIndex; i++) {
			series.add(searchData.get(i));
		}

		return series;
	}

	private void setToolTipToButton(Button button, List<SearchData> series, int index) {
		button.hoverProperty().addListener((observable) -> {
			Tooltip tooltip = buildTooltip(series, index);
			button.setTooltip(tooltip);
		});
	}

	private Tooltip buildTooltip(List<SearchData> series, int index) {
		String overview = getTooltipText(series, index);
		Tooltip tooltip = new Tooltip(overview);
		tooltip.setShowDelay(Duration.millis(100));
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setMaxWidth(300);
		tooltip.setWrapText(true);
		return tooltip;
	}

	private String getTooltipText(List<SearchData> series, int index) {
		SearchData searchData = series.get(index);
		String overview = searchData.getOverview();

		if (overview == null) {
			overview = "No overview given!";
		}

		if (overview.isBlank()) {
			overview = "No overview given!";
		}

		if (searchData.getNetwork() != null) {
			overview = overview + "\nA show by " + searchData.getNetwork();
		}

		return overview;
	}

	private void setGraphicToButton(Button button, List<SearchData> series, int index) {
		ImageView img = new ImageView();
		img.setPreserveRatio(true);
		img.setFitWidth(BTN_PREF_WIDTH - 25);
		img.setFitHeight(BTN_PREF_HEIGHT - 50);
		img.setImage(getSeriesImage(series, index));
		button.setGraphic(img);
	}

	private Image getSeriesImage(List<SearchData> series, int index) {
		return getSeriesImage(series.get(index));
	}

	private Image getSeriesImage(SearchData series) {
		TVDB api = data.getApi();
		SeriesPosters posters = api.getSeriesPosters(series.getId());
		return getTvdbPoster(posters);
	}

	private Image getTvdbPoster(SeriesPosters posters) {
		if (posters == null) {
			return TVDB.getFallbackImage();
		}

		SeriesImage[] seriesImages = posters.getData();
		if (seriesImages.length == 0) {
			return TVDB.getFallbackImage();
		}

		String posterName = seriesImages[0].getFileName();
		return TVDB.getBannerImage(posterName);
	}

	private void setButtonProperties(Button btn) {
		btn.setPrefWidth(BTN_PREF_WIDTH);
		btn.setPrefHeight(BTN_PREF_HEIGHT);

		btn.setWrapText(true);
		btn.setTextAlignment(TextAlignment.CENTER);
		btn.setId("selectionButton");
		btn.setContentDisplay(ContentDisplay.TOP);
		btn.setOnAction(event -> showSelectedSeriesInfo(btn));
	}

	private void showSelectedSeriesInfo(Button btn) {
		String btnLabel = btn.getText();
		for (SearchData result : searchData) {
			if (btnLabel.equals(result.getSeriesName())) {
				seriesFocused = true;
				selectedSeries = result;
				pagination.setPageCount(1);
				setPageFactoryInfo();
				return;
			}
		}
	}

	private void setPageFactoryInfo() {
		// one page
		// hbox: img | vbox: infos
		//             add | back

		pagination.setPageFactory(pageIndex -> {
			VBox vBox = new VBox();
			vBox.setAlignment(Pos.CENTER);
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER);

			Image image = getSeriesImage(selectedSeries);
			Rectangle roundedImage = getRoundedImageRectangle(image);
			roundedImage.setUserData("poster");

			Region spacerH1 = new Region();
			spacerH1.setMinWidth(25d);
			Region spacerH2 = new Region();
			spacerH2.setMinWidth(25d);
			Region spacerH3 = new Region();
			spacerH3.setMinWidth(25d);

			Region spacerV1 = new Region();
			spacerV1.setMinHeight(25d);
			Region spacerV2 = new Region();
			spacerV2.setMinHeight(25d);
			Region spacerV3 = new Region();
			spacerV3.setMinHeight(25d);
			Region spacerV4 = new Region();
			spacerV4.setMinHeight(25d);

			Region spacerToButtons = new Region();
			VBox.setVgrow(spacerToButtons, Priority.ALWAYS);

			VBox infoBox = new VBox();
			infoBox.getChildren().add(getInfoBox("Name: ", selectedSeries.getSeriesName()));
			infoBox.getChildren().add(spacerV1);
			infoBox.getChildren().add(getInfoBox("Overview: ", selectedSeries.getOverview()));
			infoBox.getChildren().add(spacerV2);
			infoBox.getChildren().add(getInfoBox("Network: ", selectedSeries.getNetwork()));
			infoBox.getChildren().add(spacerV3);
			infoBox.getChildren().add(getInfoBox("First aired: ", selectedSeries.getFirstAired()));
			infoBox.getChildren().add(spacerV4);
			infoBox.getChildren().add(getInfoBox("Status: ", selectedSeries.getStatus()));
			infoBox.getChildren().add(spacerToButtons);
			infoBox.getChildren().add(getButtonBox());

			hBox.getChildren().add(spacerH1);
			hBox.getChildren().add(roundedImage);
			hBox.getChildren().add(spacerH2);
			hBox.getChildren().add(infoBox);
			hBox.getChildren().add(spacerH3);
			vBox.getChildren().add(hBox);

			return vBox;
		});
	}

	private Rectangle getRoundedImageRectangle(Image image) {
		double[] sizes = calcImageViewHeight(image);
		Rectangle rectangle = new Rectangle(GRID_H_GAP, GRID_V_GAP, sizes[0], sizes[1]);
		rectangle.setArcWidth(20.0);
		rectangle.setArcHeight(20.0);

		ImagePattern pattern = new ImagePattern(image);

		rectangle.setFill(pattern);
		rectangle.setEffect(new DropShadow(20, Color.BLACK));
		return rectangle;
	}

	private double[] calcImageViewHeight(Image image) {
		double imgW = image.getWidth();
		double imgH = image.getHeight();
		double ratio = imgH / imgW;
		double widthmax = (pagination.getWidth() / 2) - (6 * GRID_H_GAP);
		double heightmax = pagination.getHeight() - (6 * GRID_V_GAP);

		if (imgH > imgW) {
			return new double[]{heightmax / ratio, heightmax};
		} else {
			return new double[]{widthmax, widthmax * ratio};
		}
	}

	private HBox getInfoBox(String title, String text) {
		HBox hBox = new HBox();
		Label labelTitle = new Label(title);
		labelTitle.setId("normalTitle");
		labelTitle.setMinWidth(Region.USE_PREF_SIZE);

		Label labelText = new Label(text);
		labelText.setId("normalLabel");
		labelText.setWrapText(true);

		hBox.getChildren().add(labelTitle);
		hBox.getChildren().add(labelText);
		return hBox;
	}

	private HBox getButtonBox() {
		Button back = new Button("Back");
		back.setOnAction(event -> {
			seriesFocused = false;
			updatePagination();
		});

		Button add = new Button("Add series");
		add.setOnAction(event -> {
			Image poster = findPoster();
			cachePoster(poster, selectedSeries);
			Collection collection = data.getCollection();
			collection.addNewSeriesById(selectedSeries.getId());
			showMainScene();
		});

		HBox buttonBox = new HBox();
		buttonBox.getChildren().addAll(back, add);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(50d);
		return buttonBox;
	}

	private Image findPoster() {
		Rectangle posterNode = (Rectangle) findPosterNode(pagination);
		if (posterNode == null) {
			LogUtil.logError("Did not find Rectangle Node for poster!");
			return null;
		}

		ImagePattern p = (ImagePattern) posterNode.getFill();
		return p.getImage();
	}

	private Node findPosterNode(Parent root) {
		ObservableList<Node> children = root.getChildrenUnmodifiable();
		for (Node child : children) {
			if ((child instanceof Rectangle) && child.getUserData().equals("poster")) {
				return child;
			}

			if (child instanceof Pane) {
				return findPosterNode((Parent) child);
			}
		}

		return null;
	}

	private void cachePoster(Image img, SearchData series) {
		if (img == null) {
			LogUtil.logDebug("Image to cache is null!");
			return;
		}

		ImageCache cache = data.getImageCache();
		if (cache.isCached(series.getId())) {
			return;
		}

		cache.save(img, series.getId());
	}

	private void showMainScene() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		SceneLoader loader = new SceneLoader(Scenes.MAIN);
		Parent root = loader.loadScene();
		stage.setTitle(Scenes.MAIN.getTitle());

		borderPane.setCenter(root);
	}
}
