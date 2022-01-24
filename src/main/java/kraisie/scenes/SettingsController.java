package kraisie.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import kraisie.data.DataSingleton;
import kraisie.data.Settings;
import kraisie.data.definitions.Scenes;
import kraisie.ui.SceneLoader;

public class SettingsController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private CheckBox cycleBackgroundsBox;

	@FXML
	private Slider fadeDurationSlider;

	@FXML
	private Slider cycleDurationSlider;

	@FXML
	private ChoiceBox<String> tvdbLanguageChoice;

	@FXML
	private Label cycleBackgroundsLabel;

	@FXML
	private Label cycleRateLabel;

	@FXML
	private Label fadeDurationLabel;

	@FXML
	private Label tvdbLanguageLabel;

	private MotherController motherScene;

	@FXML
	private void initialize() {
		DataSingleton data = DataSingleton.getInstance();
		Settings settings = data.getSettings();
		addTooltips();
		fillLanguageOptions();
		setSettings(settings);
	}

	public void initData(MotherController motherScene) {
		this.motherScene = motherScene;
	}

	private void addTooltips() {
		addTooltip(tvdbLanguageLabel, "The language in which data from TVDB will be requested in. Not all data is available in all languages. English is recommended.");
		addTooltip(cycleBackgroundsLabel, "Wether the background pictures should be changed if more than one is available.");
		addTooltip(cycleRateLabel, "The time between background picture changes in seconds.");
		addTooltip(fadeDurationLabel, "The duration of the fade animations between background picture changes in milliseconds.");
	}

	private void addTooltip(Label label, String tooltipText) {
//		label.hoverProperty().addListener((observable) -> {
//			Tooltip tooltip = buildTooltip(tooltipText);
//			label.setTooltip(tooltip);
//		});
		Tooltip tooltip = buildTooltip(tooltipText);
		label.setTooltip(tooltip);
	}

	private Tooltip buildTooltip(String text) {
		Tooltip tooltip = new Tooltip(text);
		tooltip.setShowDelay(Duration.millis(100));
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setMaxWidth(300);
		tooltip.setWrapText(true);
		return tooltip;
	}

	private void fillLanguageOptions() {
		ObservableList<String> options = FXCollections.observableArrayList();
		options.addAll("English", "German", "Spanish", "French", "Italian");
		tvdbLanguageChoice.setItems(options);
	}

	private void setSettings(Settings settings) {
		String language = getLanguageNameByIso(settings.getLangIso());
		tvdbLanguageChoice.getSelectionModel().select(language);
		cycleBackgroundsBox.setSelected(settings.isCycleBackgrounds());
		cycleDurationSlider.setValue(settings.getBackgroundCycle());
		cycleDurationSlider.disableProperty().bind(cycleBackgroundsBox.selectedProperty().not());
		fadeDurationSlider.setValue(settings.getFadeDuration());
	}

	private String getLanguageNameByIso(String iso) {
		String name;
		switch (iso.toLowerCase()) {
			case "de":
				name = "German";
				break;
			case "es":
				name = "Spanish";
				break;
			case "fr":
				name = "French";
				break;
			case "it":
				name = "Italian";
				break;
			default:
				name = "English";
		}

		return name;
	}

	@FXML
	private void saveSettings() {
		Settings newSettings = new Settings();
		newSettings.setLangIso(getLanguageIso(tvdbLanguageChoice.getValue()));
		newSettings.setCycleBackgrounds(cycleBackgroundsBox.isSelected());
		newSettings.setBackgroundCycle((int) cycleDurationSlider.getValue());
		newSettings.setFadeDuration((int) fadeDurationSlider.getValue());
		DataSingleton.updateSettings(newSettings);
		motherScene.resetScheduler();
		showMainScene();
	}

	private String getLanguageIso(String name) {
		String iso;
		switch (name.toLowerCase()) {
			case "german":
				iso = "de";
				break;
			case "spanish":
				iso = "es";
				break;
			case "french":
				iso = "fr";
				break;
			case "italian":
				iso = "it";
				break;
			default:
				iso = "en";
		}

		return iso;
	}

	@FXML
	private void resetToDefault() {
		setSettings(new Settings());
	}

	private void showMainScene() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		SceneLoader loader = new SceneLoader(Scenes.MAIN);
		Parent root = loader.loadScene();
		stage.setTitle(Scenes.MAIN.getTitle());
		borderPane.setCenter(root);
	}
}
