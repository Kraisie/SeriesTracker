package kraisie.scenes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import kraisie.data.definitions.Scenes;
import kraisie.tvdb.SearchData;
import kraisie.tvdb.SearchResult;
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
	private SearchResult selectedSeries;

	private static final int BTN_PREF_WIDTH = 175;
	private static final int BTN_PREF_HEIGHT = 250;
	private static final int GRID_H_GAP = 25;
	private static final int GRID_V_GAP = 15;

	private List<SearchResult> data;

	@FXML
	private void initialize() {

	}

	public void initData(List<SearchResult> data) {
		this.data = data;
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
		objectsPerPage = Math.min((objectsPerColumn * objectsPerRow), data.size());
	}

	private int calcPageCount() {
		return (int) Math.ceil((double) data.size() / objectsPerPage);
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

		List<SearchResult> series = getPageContentList(pageIndex);
		fillGrid(grid, series);

		hBox.getChildren().add(grid);
		vBox.getChildren().add(hBox);
		return vBox;
	}


	private void fillGrid(GridPane grid, List<SearchResult> series) {
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

	private Button buildButton(List<SearchResult> series, int listIndex) {
		String seriesName = series.get(listIndex).getSeriesName();
		Button btn = new Button(seriesName);

		setButtonProperties(btn);
		setToolTipToButton(btn, series, listIndex);
		setGraphicToButton(btn, series, listIndex);

		return btn;
	}

	private List<SearchResult> getPageContentList(int pageIndex) {
		int startIndex = pageIndex * objectsPerPage;
		int endIndex = Math.min(startIndex + objectsPerPage, data.size());

		List<SearchResult> series = new ArrayList<>();
		for (int i = startIndex; i < endIndex; i++) {
			series.add(data.get(i));
		}

		return series;
	}

	private void setToolTipToButton(Button button, List<SearchResult> series, int index) {
		button.hoverProperty().addListener((observable) -> {
			Tooltip tooltip = buildTooltip(series, index);
			button.setTooltip(tooltip);
		});
	}

	private Tooltip buildTooltip(List<SearchResult> series, int index) {
		String overview = getTooltipText(series, index);
		Tooltip tooltip = new Tooltip(overview);
		tooltip.setShowDelay(Duration.millis(100));
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setMaxWidth(300);
		tooltip.setWrapText(true);
		return tooltip;
	}

	private String getTooltipText(List<SearchResult> series, int index) {
		SearchResult result = series.get(index);
		SearchData searchData = result.getSearchData();
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

	private void setGraphicToButton(Button button, List<SearchResult> series, int index) {
		ImageView img = new ImageView();
		img.setPreserveRatio(true);
		img.setFitWidth(BTN_PREF_WIDTH - 25);
		img.setFitHeight(BTN_PREF_HEIGHT - 50);
		img.setImage(getSeriesImage(series, index));
		button.setGraphic(img);
	}

	private Image getSeriesImage(List<SearchResult> series, int index) {
		SearchResult result = series.get(index);
		return result.getPoster();
	}

	private Image getSeriesImage(SearchResult series) {
		return series.getPoster();
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
		for (SearchResult result : data) {
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
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER);

			Image image = getSeriesImage(selectedSeries);
			Rectangle roundedImage = getRoundedImageRectangle(image);
			SearchData searchData = selectedSeries.getSearchData();

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
			infoBox.getChildren().add(getInfoBox("Name: ", searchData.getSeriesName()));
			infoBox.getChildren().add(spacerV1);
			infoBox.getChildren().add(getInfoBox("Overview: ", searchData.getOverview()));
			infoBox.getChildren().add(spacerV2);
			infoBox.getChildren().add(getInfoBox("Network: ", searchData.getNetwork()));
			infoBox.getChildren().add(spacerV3);
			infoBox.getChildren().add(getInfoBox("First aired: ", searchData.getFirstAired()));
			infoBox.getChildren().add(spacerV4);
			infoBox.getChildren().add(getInfoBox("Status: ", searchData.getStatus()));
			infoBox.getChildren().add(spacerToButtons);
			infoBox.getChildren().add(getButtonBox());

			hBox.getChildren().add(spacerH1);
			hBox.getChildren().add(roundedImage);
			hBox.getChildren().add(spacerH2);
			hBox.getChildren().add(infoBox);
			hBox.getChildren().add(spacerH3);
			vBox.setAlignment(Pos.CENTER);
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
			DataSingleton data = DataSingleton.getInstance();
			Collection collection = data.getCollection();
			collection.addNewSeriesById(selectedSeries.getSearchData().getId());
			showMainScene();
		});

		HBox buttonBox = new HBox();
		buttonBox.getChildren().addAll(back, add);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(50d);

		return buttonBox;
	}

	private void showMainScene() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		SceneLoader loader = new SceneLoader(Scenes.MAIN);
		Parent root = loader.loadScene();
		stage.setTitle(Scenes.MAIN.getTitle());

		borderPane.setCenter(root);
	}
}