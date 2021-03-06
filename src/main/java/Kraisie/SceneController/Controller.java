package Kraisie.SceneController;

import Kraisie.Data.MySeries;
import Kraisie.TVDB.SearchData;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class Controller {

	/**
	 * centers too small images in the ImageView
	 *
	 * @param imageView the ImageView in which the image should get centered
	 */
	void centerImage(ImageView imageView) {
		Image img = imageView.getImage();

		if (img == null) {
			return;
		}

		double ratioX = imageView.getFitWidth() / img.getWidth();
		double ratioY = imageView.getFitHeight() / img.getHeight();

		double reducCoeff;
		if (ratioX >= ratioY) {
			reducCoeff = ratioY;
		} else {
			reducCoeff = ratioX;
		}

		double width = img.getWidth() * reducCoeff;
		double height = img.getHeight() * reducCoeff;

		imageView.setX((imageView.getFitWidth() - width) / 2);
		imageView.setY((imageView.getFitHeight() - height) / 2);
	}

	void setTooltipToLabel(Label label, String text) {
		label.hoverProperty().addListener((observable, oldValue, newValue) -> {
			Tooltip tooltip = new Tooltip(text);
			tooltip.setShowDelay(Duration.millis(200));
			tooltip.setShowDuration(Duration.INDEFINITE);
			label.setTooltip(tooltip);
		});
	}

	/**
	 * opens a new scene in the given Stage
	 *
	 * @param primaryStage the stage in which the scene should get shown
	 * @param fxmlPath     the path to the fxml file which describes the scene
	 * @param title        the title of the Window
	 */
	void openScene(Stage primaryStage, String fxmlPath, String title) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
		setStageProperties(primaryStage, title, root);
	}

	/**
	 * opens a scene in a stage and passes a parameter as Object
	 *
	 * @param primaryStage the stage in which the scene should get shown
	 * @param fxmlPath     the path to the fxml file which describes the scene
	 * @param title        the title of the Window
	 * @param series       Object which shall get passed (List or MySeries)
	 * @throws IOException if fxml file can not be read
	 * @see MySeries
	 */
	@SuppressWarnings("unchecked")
	// cast from object to List<SearchData> is safe
	void openSceneWithOneParameter(Stage primaryStage, String fxmlPath, String title, Object series) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		// get right controller
		if (series instanceof MySeries) {
			AdvancedInformationController controller = loader.getController();
			controller.initData((MySeries) series);
		}
		if (series instanceof List) {
			SelectController controller = loader.getController();
			controller.initData((List<SearchData>) series);
		}

		setStageProperties(primaryStage, title, root);
	}

	/**
	 * opens the search scene without passing a parameter. This is needed if there are no search results which should get shown again.
	 *
	 * @param primaryStage the stage in which the scene should get shown
	 * @param fxmlPath     the path to the fxml file which describes the scene
	 * @param title        the title of the Window
	 * @throws IOException if fxml file can not be read
	 * @see SearchController
	 */
	void openSearch(Stage primaryStage, String fxmlPath, String title) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		// get right controller
		SearchController controller = loader.getController();
		controller.initData();
		setStageProperties(primaryStage, title, root);
	}

	/**
	 * opens advanced information screen and passes two parameters.
	 *
	 * @param primaryStage the stage in which the scene should get shown
	 * @param fxmlPath     the path to the fxml file which describes the scene
	 * @param title        the title of the Window
	 * @param series       series which the user wants to see all information about
	 * @param tmpMatches   a list of all matches found via the search scene
	 * @throws IOException if fxml file can not be read
	 * @see AdvancedInformationController
	 */
	void openAdvancedInformationFromSearch(Stage primaryStage, String fxmlPath, String title, MySeries series, List<MySeries> tmpMatches) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		// get right controller
		AdvancedInformationController controller = loader.getController();
		controller.initData(series, tmpMatches);
		setStageProperties(primaryStage, title, root);
	}

	/**
	 * opens the search from the information scene and passes the search results.
	 *
	 * @param primaryStage the stage in which the scene should get shown
	 * @param fxmlPath     the path to the fxml file which describes the scene
	 * @param title        the title of the Window
	 * @param tmpMatches   a list of all matches found via the search scene
	 * @throws IOException if fxml file can not be read
	 * @see SearchController
	 */
	void openSearchFromAdvancedInformation(Stage primaryStage, String fxmlPath, String title, List<MySeries> tmpMatches) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		// get right controller
		SearchController controller = loader.getController();
		controller.initData(tmpMatches);
		setStageProperties(primaryStage, title, root);
	}

	/**
	 * opens the main menu scene
	 *
	 * @param primaryStage the stage in which the scene should get shown
	 * @throws IOException if fxml file can not be read
	 */
	void openMain(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainSeries.fxml"));
		Parent root = loader.load();
		MainSeriesController controller = loader.getController();

		// initialize Background after scene initialization
		Platform.runLater(controller::initBackground);
		setStageProperties(primaryStage, "Series Control Panel", root);
	}

	/**
	 * sets all stage properties and shows the scene
	 *
	 * @param primaryStage the stage in which the scene should get shown
	 * @param title        the title of the Window
	 * @param root         the Parent which contains the fxml data
	 */
	private void setStageProperties(Stage primaryStage, String title, Parent root) {
		// get center of current stage
		double xPos = primaryStage.getX() + primaryStage.getWidth() / 2d;
		double yPos = primaryStage.getY() + primaryStage.getHeight() / 2d;

		primaryStage.setTitle(title);
		primaryStage.setScene(new Scene(root));
		primaryStage.sizeToScene();

		// Relocate the new scene
		primaryStage.setX(xPos - primaryStage.getWidth() / 2d);
		primaryStage.setY(yPos - primaryStage.getHeight() / 2d);
		primaryStage.show();
	}

	/**
	 * Scrolls to the first series that starts with the letter that just got pressed.
	 * If the same gets pressed as the first letter of the currently selected series the next series with that first letter gets selected.
	 * Keys like Shift, Enter etc get ignored.
	 *
	 * @param key KeyEvent of the pressed key.
	 */
	void scrollToSeries(TableView<MySeries> table, KeyEvent key) {
		if (key.getText().length() == 0) {
			return;
		}

		char c = key.getText().charAt(0);
		ObservableList<MySeries> allSeriesInTable = table.getItems();
		MySeries match = null;

		// if the selected series already starts with that char and the next one in the list still got that char then select the next series
		int index = table.getSelectionModel().getSelectedIndex();
		if (index + 1 < allSeriesInTable.size()) {
			MySeries nextSeries = allSeriesInTable.get(index + 1);
			if (c == nextSeries.getName().toLowerCase().charAt(0)) {
				table.getSelectionModel().select(nextSeries);
				return;
			}
		}

		// select first occurrence
		for (MySeries series : allSeriesInTable) {
			if (c == series.getName().toLowerCase().charAt(0)) {
				match = series;
				break;
			}
		}

		if (match == null) {
			return;
		}

		table.getSelectionModel().select(match);
		table.scrollTo(match);
	}
}
