package kraisie.scenes;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import kraisie.data.Collection;
import kraisie.data.DataSingleton;
import kraisie.data.Series;
import kraisie.data.definitions.UserState;
import kraisie.tvdb.TVDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UpdateSeriesController {

	@FXML
	private TextArea updateLogArea;

	@FXML
	private ProgressBar updateProgress;

	@FXML
	private Button updateContinuing;

	@FXML
	private Button updateEnded;

	@FXML
	private Button updateAll;

	private DataSingleton data;
	private Collection collection;

	@FXML
	private void initialize() {
		data = DataSingleton.getInstance();
		collection = data.getCollection();
		addProgressBarWidthListener();
		addButtonWidthListener();
	}

	private void addProgressBarWidthListener() {
		VBox parent = (VBox) updateLogArea.getParent();
		updateProgress.prefWidthProperty().bind(parent.widthProperty());
	}

	private void addButtonWidthListener() {
		updateEnded.prefWidthProperty().bind(updateContinuing.widthProperty());
		updateAll.prefWidthProperty().bind(updateContinuing.widthProperty());
	}

	@FXML
	private void updateAll() {
		updateSeries(collection.getSeries());
	}

	@FXML
	private void updateContinuing() {
		List<Series> continuingSeries = new ArrayList<>();
		for (Series series : collection.getSeries()) {
			if (series.isContinuing()) {
				continuingSeries.add(series);
			}
		}

		updateSeries(continuingSeries);
	}

	@FXML
	private void updateEnded() {
		List<Series> continuingSeries = new ArrayList<>();
		for (Series series : collection.getSeries()) {
			if (!series.isContinuing()) {
				continuingSeries.add(series);
			}
		}

		updateSeries(continuingSeries);
	}

	private void updateSeries(List<Series> series) {
		if (hasRunningUpdate()) {
			updateLogArea.setText("Error: Please wait for the current update to finish before starting a new one!");
			return;
		}

		setButtonDisable(true);
		updateLogArea.setText("Updating " + series.size() + " series...");
		Task<Void> updateTask = new Task<>() {
			@Override
			protected Void call() {
				TVDB api = data.getApi();
				List<String> changeLog = new ArrayList<>();
				for (int i = 0; i < series.size(); i++) {
					updateLog(series.get(i), i + 1, series.size());
					int collectionIndex = collection.getSeries().indexOf(series.get(i));
					boolean userStateChange = updateSeries(api, collectionIndex);
					if (userStateChange) {
						addToChangeLog(changeLog, series.get(i), collection.getSeries().get(collectionIndex));
					}

					updateProgress(i + 1, series.size());
				}

				Platform.runLater(
						() -> {
							logSummary(changeLog);
							updateLogArea.appendText("Update finished!");
							setButtonDisable(false);
						}
				);
				return null;
			}
		};

		updateProgress.progressProperty().unbind();
		updateProgress.progressProperty().bind(updateTask.progressProperty());
		Thread thread = new Thread(updateTask, "st-update-task");
		thread.setDaemon(true);
		thread.start();
	}

	private void setButtonDisable(boolean disable) {
		updateContinuing.setDisable(disable);
		updateEnded.setDisable(disable);
		updateAll.setDisable(disable);
	}

	private boolean hasRunningUpdate() {
		Set<Thread> threads = Thread.getAllStackTraces().keySet();
		for (Thread thread : threads) {
			if (thread.getName().equalsIgnoreCase("st-update-task")) {
				return thread.isAlive();
			}
		}

		return false;
	}

	private void updateLog(Series series, int current, int total) {
		Platform.runLater(
				() -> updateLogArea.appendText("\n[" + current + "/" + total + "] - Updating \"" + series.getName() + "\".")
		);
	}

	private boolean updateSeries(TVDB api, int collectionIndex) {
		Series series = collection.getSeries().get(collectionIndex);
		UserState oldState = series.getUserStatus();
		Series updatedSeries = api.updateSeries(series);
		UserState newState = updatedSeries.getUserStatus();
		collection.getSeries().set(collectionIndex, updatedSeries);
		return oldState != newState;
	}

	private void addToChangeLog(List<String> changeLog, Series oldSeries, Series newSeries) {
		changeLog.add(
				"\"" + oldSeries.getName() + "\" user state changed: \"" +
						oldSeries.getUserStatus() + "\" to \"" + newSeries.getUserStatus() + "\"." +
						(newSeries.getUserStatus() == UserState.WATCHING ? "\n\tThis series has new episodes available!" : "")
		);
	}

	private void logSummary(List<String> changeLog) {
		updateLogArea.appendText("\n\n");
		for (String change : changeLog) {
			updateLogArea.appendText(change + "\n\n");
		}
	}
}
