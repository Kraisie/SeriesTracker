package SceneController;

import Data.MySeries;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

class Controller {

	/*
	 *	center too small images
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

	/*
	 *  open a new scene on the given stage
	 */
	@FXML
	void openScene(Stage primaryStage, String fxmlPath, String title) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
		setStageProperties(primaryStage, title, root);
	}

	/*
	 *  open scene and pass a parameter
	 */
	@FXML
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

	/*
	 *  open search screen without passing a parameter
	 */
	@FXML
	void openSearch(Stage primaryStage, String fxmlPath, String title) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		// get right controller
		SearchController controller = loader.getController();
		controller.initData();
		setStageProperties(primaryStage, title, root);
	}

	/*
	 *  open advanced information screen and pass two parameters (1 needed, 1 to save)
	 */
	@FXML
	void openAdvancedInformationFromSearch(Stage primaryStage, String fxmlPath, String title, MySeries series, List<MySeries> tmpMatches) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		// get right controller
		AdvancedInformationController controller = loader.getController();
		controller.initData(series, tmpMatches);
		setStageProperties(primaryStage, title, root);
	}

	/*
	 *  open advanced information screen and pass two parameters (1 needed, 1 to save)
	 */
	@FXML
	void openSearchFromAdvancedInformation(Stage primaryStage, String fxmlPath, String title, List<MySeries> tmpMatches) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
		Parent root = loader.load();

		// get right controller
		SearchController controller = loader.getController();
		controller.initData(tmpMatches);
		setStageProperties(primaryStage, title, root);
	}

	/*
	 *	set stage properties
	 */
	private void setStageProperties(Stage primaryStage, String title, Parent root) {
		primaryStage.setTitle(title);
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root));
		primaryStage.sizeToScene();
		primaryStage.show();
	}
}
