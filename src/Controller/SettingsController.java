package Controller;

import Code.PopUp;
import Data.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SettingsController {

    @FXML
    public TextField textPathMovies;
    @FXML
    public TextField textPathSave;
    @FXML
    public TextField textPathBackUp;
    @FXML
    public Spinner<Integer> frequencySpinner;
    @FXML
    public Button backButton;

    private Settings settings;

    public void initialize() {
        settings = Settings.readData();
        textPathMovies.setText(settings.getPathMovies().toString());
        textPathSave.setText(settings.getPathSeries().toString());
        textPathBackUp.setText(settings.getPathBackUp().toString());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 14);
        TextFormatter<Integer> formatter = new TextFormatter<>(valueFactory.getConverter(), valueFactory.getValue());
        frequencySpinner.getEditor().setTextFormatter(formatter);
        valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());
        frequencySpinner.setValueFactory(valueFactory);

        //check for json modification
        if (settings.getBackUpCycle() > 0 && settings.getBackUpCycle() < 15) {
            frequencySpinner.getValueFactory().setValue(settings.getBackUpCycle());
        } else {
            settings.setBackUpCycle(1);
            frequencySpinner.getValueFactory().setValue(settings.getBackUpCycle());
            Settings.writeData(settings);
        }
    }

    public void changePathMovies() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Set path for your movies file...");
        File dir = fileChooser.showDialog(backButton.getScene().getWindow());

        if (dir != null) {
            File file = new File(dir, "Movies.json");

            try {
                Path path = Paths.get(file.getCanonicalPath());
                settings.setPathMovies(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        textPathMovies.setText(settings.getPathMovies().toString());
    }

    public void changePathSave() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Set path for your movies file...");
        File dir = fileChooser.showDialog(backButton.getScene().getWindow());

        if (dir != null) {
            File file = new File(dir, "Series.json");

            try {
                Path path = Paths.get(file.getCanonicalPath());
                settings.setPathSeries(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        textPathSave.setText(settings.getPathSeries().toString());
    }

    public void changePathBackUp() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Set path for your BackUp file...");
        File dir = fileChooser.showDialog(backButton.getScene().getWindow());

        if (dir != null) {
            File file = new File(dir, "BackUp.json");

            try {
                Path path = Paths.get(file.getCanonicalPath());
                settings.setPathBackUp(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        textPathBackUp.setText(settings.getPathBackUp().toString());
    }

    public void save() {
        Settings oldSettings = Settings.readData();
        if(!oldSettings.getPathSeries().equals(settings.getPathSeries())) {
            try {
                Files.move(oldSettings.getPathSeries(), settings.getPathSeries(), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                PopUp.error("Trying to move old save files failed.");
            }
        }

        if(!oldSettings.getPathMovies().equals(settings.getPathMovies())) {
            try {
                Files.move(oldSettings.getPathMovies(), settings.getPathMovies(), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                PopUp.error("Trying to move old save files failed.");
            }
        }

        if(!oldSettings.getPathBackUp().equals(settings.getPathBackUp())) {
            try {
                Files.move(oldSettings.getPathBackUp(), settings.getPathBackUp(), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                PopUp.error("Trying to move old save files failed.");
            }
        }

        Settings.writeData(settings);
        back();
    }

    public void standard() {
        settings.setStandardSettings();
        Settings.writeData(settings);
        back();
    }

    public void back() {
        try {
            Stage primaryStage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/MainSeries.fxml"));
            primaryStage.setTitle("Series Control Panel");
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
