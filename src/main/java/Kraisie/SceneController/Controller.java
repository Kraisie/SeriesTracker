package Kraisie.SceneController;

import Kraisie.Data.MySeries;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

class Controller {

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
	// cast from object to List<MySeries> is safe
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
			controller.initData((List<MySeries>) series);
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
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root));
		primaryStage.sizeToScene();

		// Relocate the new scene
		primaryStage.setX(xPos - primaryStage.getWidth() / 2d);
		primaryStage.setY(yPos - primaryStage.getHeight() / 2d);
		primaryStage.show();
	}
}
