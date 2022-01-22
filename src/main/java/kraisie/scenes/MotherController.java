package kraisie.scenes;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import kraisie.data.Settings;
import kraisie.data.definitions.Scenes;
import kraisie.ui.BackgroundManager;
import kraisie.ui.SceneLoader;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MotherController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private VBox navBar;

	@FXML
	private Button mainMenu;

	@FXML
	private Button addSeries;

	@FXML
	private Button searchSeries;

	@FXML
	private Button updateSeries;

	@FXML
	private Button settingsMenu;

	@FXML
	private Button help;

	private boolean navBarVisibility = true;
	private Settings settings;

	@FXML
	private void initialize() {
		this.settings = Settings.readData();        // TODO: update settings in this class when changed in Settings screen
		initBackgroundCycle();
	}

	private void initBackgroundCycle() {
		if (settings.isCycleBackgrounds()) {
			int cycle = settings.getBackgroundCycle();
			ScheduledExecutorService scheduler = createDaemonExecutorService();
			scheduler.scheduleAtFixedRate(this::initBackground, cycle, cycle, TimeUnit.MINUTES);
		}
	}

	private ScheduledExecutorService createDaemonExecutorService() {
		// create a ScheduleExecutorService that is based on daemon threads, so it gets stopped when the application closes
		return Executors.newScheduledThreadPool(1, runnable -> {
			Thread t = Executors.defaultThreadFactory().newThread(runnable);
			t.setDaemon(true);
			return t;
		});
	}

	public void initOnShow() {
		initBackground();
		initCenterScene();
	}

	private void initBackground() {
		BackgroundManager bm;
		try {
			bm = new BackgroundManager();
		} catch (IOException e) {
			// TODO: exception handling (logger, as normal background shows if no connection and no files)
			e.printStackTrace();
			return;
		}

		if (borderPane.getBackground().getImages().isEmpty()) {
			Background background = bm.getBackground(borderPane);
			borderPane.setBackground(background);
		}

		if (bm.hasBackgroundChoice()) {
			transition(bm);
		}

		addSizeListeners();
	}

	private void transition(BackgroundManager bm) {
		// change background of highest pane in center to hide borderpane background while changing picture
		// without using BackgroundFill on the real Background
		Pane pane = (Pane) borderPane.getCenter();
		final Animation animationIn = createAnimationIn(pane);
		final Animation animationOut = createAnimationOut(pane);

		animationIn.setOnFinished(event -> {
			Background background = bm.getBackground(borderPane);
			borderPane.setBackground(background);
			animationOut.play();
		});
		animationIn.play();
	}

	private Transition createAnimationIn(Pane pane) {
		return new Transition() {
			{
				setCycleDuration(Duration.millis(settings.getFadeDuration()));
				setInterpolator(Interpolator.EASE_OUT);
			}

			@Override
			protected void interpolate(double frac) {
				Color vColor = new Color(0, 0, 0, frac);
				pane.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		};
	}

	private Transition createAnimationOut(Pane pane) {
		return new Transition() {
			{
				setCycleDuration(Duration.millis(settings.getFadeDuration()));
				setInterpolator(Interpolator.EASE_IN);
			}

			@Override
			protected void interpolate(double frac) {
				Color vColor = new Color(0, 0, 0, 1d - frac);
				pane.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
			}
		};
	}

	private void addSizeListeners() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		stage.widthProperty().addListener((obs, oldVal, newVal) -> updateBackgroundSize());
		stage.heightProperty().addListener((obs, oldVal, newVal) -> updateBackgroundSize());
	}

	private void updateBackgroundSize() {
		Image image = borderPane.getBackground().getImages().get(0).getImage();
		BackgroundManager bm = new BackgroundManager(SwingFXUtils.fromFXImage(image, null));
		Background background = bm.getBackground(borderPane);
		borderPane.setBackground(background);
	}

	private void initCenterScene() {
		showMainMenu();
	}

	@FXML
	private void onNavBarTrigger() {
		navBarVisibility = !navBarVisibility;
		toggleNavBar();
	}

	@FXML
	private void toggleNavBar() {
		toggleButtons();
		setNavBarSize();
		toggleSideBar();
	}

	private void setNavBarSize() {
		int navBarWidth = navBarVisibility ? 300 : 0;
		navBar.setMaxWidth(navBarWidth);
	}

	private void toggleButtons() {
		setButtonVisibilities();
		setButtonStates();
	}

	private void setButtonVisibilities() {
		mainMenu.setVisible(navBarVisibility);
		addSeries.setVisible(navBarVisibility);
		searchSeries.setVisible(navBarVisibility);
		updateSeries.setVisible(navBarVisibility);
		settingsMenu.setVisible(navBarVisibility);
		help.setVisible(navBarVisibility);
	}

	private void setButtonStates() {
		mainMenu.setDisable(!navBarVisibility);
		addSeries.setDisable(!navBarVisibility);
		searchSeries.setDisable(!navBarVisibility);
		updateSeries.setDisable(!navBarVisibility);
		settingsMenu.setDisable(!navBarVisibility);
		help.setDisable(!navBarVisibility);
	}

	private void toggleSideBar() {
		navBar.setVisible(navBarVisibility);
		navBar.setDisable(!navBarVisibility);
		navBar.setManaged(navBarVisibility);
	}

	@FXML
	private void showMainMenu() {
		showScene(Scenes.MAIN);
	}

	@FXML
	private void showAddSeries() {
		showScene(Scenes.ADD);
	}

	@FXML
	private void showSearchSeries() {
		showScene(Scenes.SEARCH);
	}

	@FXML
	private void showUpdate() {
		showScene(Scenes.UPDATE);
	}

	@FXML
	private void showSettings() {
		showScene(Scenes.SETTINGS);
	}

	@FXML
	private void showHelp() {
		showScene(Scenes.HELP);
	}

	private void showScene(Scenes scene) {
		onNavBarTrigger();
		Stage stage = (Stage) borderPane.getScene().getWindow();
		if (stage.getTitle().equals(scene.getTitle())) {
			return;
		}

		SceneLoader loader = new SceneLoader(scene);
		Parent root = loader.loadScene();
		stage.setTitle(scene.getTitle());

		borderPane.setCenter(root);
	}
}
