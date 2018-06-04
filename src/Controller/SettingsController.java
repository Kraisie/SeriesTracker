package Controller;

import Data.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsController {

    @FXML
    public TextField textPathSettings;
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
        textPathSettings.setText(settings.getPathSettings().toString());
        textPathSave.setText(settings.getPathSave().toString());
        textPathBackUp.setText(settings.getPathBackUp().toString());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 14);
        TextFormatter<Integer> formatter = new TextFormatter<>(valueFactory.getConverter(), valueFactory.getValue());
        frequencySpinner.getEditor().setTextFormatter(formatter);
        valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());
        frequencySpinner.setValueFactory(valueFactory);

        //check for json modification
        if(settings.getBackUpCycle() > 0 && settings.getBackUpCycle() < 15) {
            frequencySpinner.getValueFactory().setValue(settings.getBackUpCycle());
        } else {
            settings.setBackUpCycle(1);
            frequencySpinner.getValueFactory().setValue(settings.getBackUpCycle());
            Settings.writeData(settings);
        }
    }

    public void changePathSettings() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Set path for your settings file...");
        File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());

        if(file != null) {
            try {
                Path path = Paths.get(file.getCanonicalPath());
                settings.setPathSettings(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        textPathSettings.setText(settings.getPathSettings().toString());
    }

    public void changePathSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Set path for your save file...");
        File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());

        if(file != null) {
            try {
                Path path = Paths.get(file.getCanonicalPath());
                settings.setPathSave(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        textPathSave.setText(settings.getPathSave().toString());
    }

    public void changePathBackUp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Set path for your BackUp file...");
        File file = fileChooser.showOpenDialog(backButton.getScene().getWindow());

        if(file != null) {
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
        Settings.writeData(settings);
        back();
    }

    public void standard() {
        settings.setStandardSettings();
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
