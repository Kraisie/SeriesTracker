package kraisie.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kraisie.data.definitions.Scenes;
import kraisie.scenes.MotherController;
import kraisie.scenes.SelectSeriesController;
import kraisie.tvdb.SearchResult;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SceneLoader {

	private Stage stage;
	private Scenes scene;

	public SceneLoader(Scenes scene) {
		this.scene = scene;
	}

	public SceneLoader(Stage stage, Scenes scene) {
		this.stage = stage;
		this.scene = scene;
	}

	public void loadMotherScene() {
		String pathFxml = scene.getPath();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(pathFxml));
		setStageProperties();
		setScene(loader);

		MotherController controller = loader.getController();
		stage.addEventHandler(WindowEvent.WINDOW_SHOWN, window -> controller.initOnShow());

		stage.show();
	}

	private void setStageProperties() {
		String pathIcon = scene.getIcon();
		setIcon(pathIcon);
		stage.setTitle(scene.getTitle());
		stage.sizeToScene();
	}

	private void setIcon(String path) {
		URL resource = getClass().getResource(path);
		Image img = new Image(resource.toString());
		stage.getIcons().add(img);
	}

	private void setScene(FXMLLoader loader) {
		Parent root = loadRoot(loader);
		stage.setScene(new Scene(root));
	}

	private Parent loadRoot(FXMLLoader loader) {
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO: Logger
			e.printStackTrace();
		}

		return root;
	}

	public void openNewScene() {
		String pathFxml = scene.getPath();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(pathFxml));
		setScene(loader);
		stage.showAndWait();
	}

	public Parent loadScene() {
		String pathFxml = scene.getPath();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(pathFxml));

		return loadRoot(loader);
	}

	public Parent loadSceneWithData(List<SearchResult> data) {
		String pathFxml = scene.getPath();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(pathFxml));
		Parent root = loadRoot(loader);

		Object tmpController = loader.getController();
		if (tmpController instanceof SelectSeriesController) {
			SelectSeriesController controller = loader.getController();
			controller.initData(data);
		}

		return root;
	}
}