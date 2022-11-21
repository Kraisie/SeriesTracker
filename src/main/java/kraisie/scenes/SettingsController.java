package kraisie.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import kraisie.data.DataSingleton;
import kraisie.data.Settings;
import kraisie.data.definitions.CacheSize;
import kraisie.data.definitions.Scenes;
import kraisie.dialog.PopUp;
import kraisie.ui.SceneLoader;

public class SettingsController {

	@FXML
	private BorderPane borderPane;

	@FXML
	private CheckBox cycleBackgroundsBox;

	@FXML
	private CheckBox cacheBannersBox;

	@FXML
	private Slider fadeDurationSlider;

	@FXML
	private Slider cycleDurationSlider;

	@FXML
	private ChoiceBox<String> tvdbLanguageChoice;

	@FXML
	private ChoiceBox<String> maxCacheSizeChoice;

	@FXML
	private Label cycleBackgroundsLabel;

	@FXML
	private Label cycleRateLabel;

	@FXML
	private Label fadeDurationLabel;

	@FXML
	private Label tvdbLanguageLabel;

	@FXML
	private Label cacheBannersLabel;

	@FXML
	private Label maxCacheSizeLabel;

	@FXML
	private Label currentCacheSizeLabel;

	@FXML
	private Label currentCacheSize;

	@FXML
	private Button clearCache;

	private DataSingleton data;
	private MotherController motherScene;

	@FXML
	private void initialize() {
		data = DataSingleton.getInstance();
		Settings settings = data.getSettings();
		addTooltips();
		fillChoiceOptions();
		setSettings(settings);
	}

	public void initData(MotherController motherScene) {
		this.motherScene = motherScene;
	}

	private void addTooltips() {
		addTooltip(tvdbLanguageLabel, "The language in which data from TVDB will be requested in. Not all data is available in all languages. English is recommended.");
		addTooltip(cycleBackgroundsLabel, "Whether the background pictures should be changed if more than one is available.");
		addTooltip(cacheBannersLabel, "Whether to cache the banners of your series on your drive. Redownloads the banners each time if this is disabled and clears the cache when disabling.");
		addTooltip(maxCacheSizeLabel, "Limits the cache size. New banners replace the oldest banner if size limit is reached. Deletes old banners if your new max size is below your current cache size!");
		addTooltip(currentCacheSizeLabel, "The current size of your cache folder. Does not update if the cache gets modified externally!");
		addTooltip(cycleRateLabel, "The time between background picture changes in seconds.");
		addTooltip(fadeDurationLabel, "The duration of the fade animations between background picture changes in milliseconds.");
		addTooltip(clearCache, "Clears the cached images of series.");
	}

	private void addTooltip(Control node, String tooltipText) {
		Tooltip tooltip = buildTooltip(tooltipText);
		node.setTooltip(tooltip);
	}

	private Tooltip buildTooltip(String text) {
		Tooltip tooltip = new Tooltip(text);
		tooltip.setShowDelay(Duration.millis(100));
		tooltip.setShowDuration(Duration.INDEFINITE);
		tooltip.setMaxWidth(300);
		tooltip.setWrapText(true);
		return tooltip;
	}

	private void fillChoiceOptions() {
		ObservableList<String> languageOptions = FXCollections.observableArrayList();
		languageOptions.addAll("English", "German", "Spanish", "French", "Italian");
		tvdbLanguageChoice.setItems(languageOptions);

		ObservableList<String> sizeOptions = FXCollections.observableArrayList();
		sizeOptions.addAll(CacheSize.toStringList());
		maxCacheSizeChoice.setItems(sizeOptions);
	}

	private void setSettings(Settings settings) {
		String language = getLanguageNameByIso(settings.getLangIso());
		tvdbLanguageChoice.getSelectionModel().select(language);
		cycleBackgroundsBox.setSelected(settings.isCycleBackgrounds());
		cacheBannersBox.setSelected(settings.shouldCacheBanners());
		CacheSize maxSize = settings.getMaxCacheSize();
		maxCacheSizeChoice.getSelectionModel().select(maxSize.toString());
		maxCacheSizeChoice.disableProperty().bind(cacheBannersBox.selectedProperty().not());
		String currentSize = data.getImageCache().getCurrentSizeText();
		currentCacheSize.setText(currentSize);
		cycleDurationSlider.setValue(settings.getBackgroundCycle());
		cycleDurationSlider.disableProperty().bind(cycleBackgroundsBox.selectedProperty().not());
		fadeDurationSlider.setValue(settings.getFadeDuration());
		fadeDurationSlider.disableProperty().bind(cycleBackgroundsBox.selectedProperty().not());
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
		newSettings.setCacheBanners(cacheBannersBox.isSelected());
		newSettings.setMaxCacheSize(CacheSize.get(maxCacheSizeChoice.getValue()));
		DataSingleton.updateSettings(newSettings);
		if (newSettings.shouldCacheBanners()) {
			data.getImageCache().purgeToMaxCacheSize();
		} else {
			data.getImageCache().clear();
		}

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

	@FXML
	private void clearCache() {
		boolean success = data.getImageCache().clear();
		PopUp popUp = PopUp.forStage((Stage) clearCache.getScene().getWindow());
		if (success) {
			popUp.showAlert("Cache cleared!", "The image cache has been cleared successfully.", false);
		} else {
			popUp.showError("Cache could not get cleared!", "The image cache could not get cleared. Please check the log file for further details.", false);
		}

		setSettings(data.getSettings());
	}

	private void showMainScene() {
		Stage stage = (Stage) borderPane.getScene().getWindow();
		SceneLoader loader = new SceneLoader(Scenes.MAIN);
		Parent root = loader.loadScene();
		stage.setTitle(Scenes.MAIN.getTitle());
		borderPane.setCenter(root);
	}
}
