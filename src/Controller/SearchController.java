package Controller;

import Code.PopUp;
import Data.MySeries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    @FXML
    public Spinner<Integer> durationSpinner;
    @FXML
    public Spinner<Integer> derivationSpinner;
    @FXML
    public ComboBox<String> ratingChoice;
    @FXML
    public Spinner<Integer> seasonsSpinner;
    @FXML
    public CheckBox checkStarted;
    @FXML
    public CheckBox checkWatching;
    @FXML
    public CheckBox checkWaiting;
    @FXML
    public CheckBox checkFinished;
    @FXML
    public RadioButton radioContinuing;
    @FXML
    public RadioButton radioEnded;
    @FXML
    public Button infoButton;
    @FXML
    public Button closeButton;
    @FXML
    public Button researchButton;
    @FXML
    public ListView<String> foundMatches;

    private boolean mode = false;
    public static MySeries toController;
    public static List<MySeries> tmpMatches;

    public void initialize() {
        if (InformationController.tmpMatches != null) {
            tmpMatches = InformationController.tmpMatches;

            for (MySeries series : tmpMatches) {
                foundMatches.getItems().add(series.getName());
                mode = true;
            }
        }

        if (!mode) {
            invert(true);

            //0 to 500 for duration, derivation and seasons
            SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500);
            SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500);
            SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500);

            TextFormatter<Integer> formatter1 = new TextFormatter<>(valueFactory1.getConverter(), valueFactory1.getValue());
            TextFormatter<Integer> formatter2 = new TextFormatter<>(valueFactory2.getConverter(), valueFactory2.getValue());
            TextFormatter<Integer> formatter3 = new TextFormatter<>(valueFactory3.getConverter(), valueFactory3.getValue());

            durationSpinner.getEditor().setTextFormatter(formatter1);
            derivationSpinner.getEditor().setTextFormatter(formatter2);
            seasonsSpinner.getEditor().setTextFormatter(formatter3);

            valueFactory1.valueProperty().bindBidirectional(formatter1.valueProperty());
            valueFactory2.valueProperty().bindBidirectional(formatter2.valueProperty());
            valueFactory3.valueProperty().bindBidirectional(formatter3.valueProperty());

            durationSpinner.setValueFactory(valueFactory1);
            derivationSpinner.setValueFactory(valueFactory2);
            seasonsSpinner.setValueFactory(valueFactory3);

            //choices for rating
            ObservableList<String> choices = FXCollections.observableArrayList();
            choices.addAll("0", ">1", ">2", ">3", ">4", ">5", ">6", ">7", ">8", ">9");
            ratingChoice.setItems(choices);
            ratingChoice.getSelectionModel().select(0);

            radioContinuing.setSelected(false);
            radioEnded.setSelected(false);
        } else {
            invert(false);
        }
    }

    private void invert(boolean mode) {
        durationSpinner.setVisible(mode);
        derivationSpinner.setVisible(mode);
        ratingChoice.setVisible(mode);
        seasonsSpinner.setVisible(mode);
        checkStarted.setVisible(mode);
        checkWatching.setVisible(mode);
        checkWaiting.setVisible(mode);
        checkFinished.setVisible(mode);
        radioContinuing.setVisible(mode);
        radioEnded.setVisible(mode);

        durationSpinner.setDisable(!mode);
        derivationSpinner.setDisable(!mode);
        ratingChoice.setDisable(!mode);
        seasonsSpinner.setDisable(!mode);
        checkStarted.setDisable(!mode);
        checkWatching.setDisable(!mode);
        checkWaiting.setDisable(!mode);
        checkFinished.setDisable(!mode);
        radioContinuing.setDisable(!mode);
        radioEnded.setDisable(!mode);

        foundMatches.setVisible(!mode);
        foundMatches.setDisable(mode);
        researchButton.setVisible(!mode);
        researchButton.setDisable(mode);
        infoButton.setVisible(!mode);
        infoButton.setDisable(mode);
    }

    public void search() {
        List<MySeries> allSeries = MySeries.readData();
        List<MySeries> matches = new ArrayList<>();

        int rating;
        if (ratingChoice.getValue().contains(">")) {
            rating = Integer.valueOf(ratingChoice.getValue().replace(">", ""));
        } else {
            rating = Integer.valueOf(ratingChoice.getValue());
        }

        int duration = durationSpinner.getValue();
        int derivation = derivationSpinner.getValue();
        int seasons = seasonsSpinner.getValue();

        for (MySeries series : allSeries) {
            if (duration != 0 && (series.getRuntime() < (duration - derivation) || series.getRuntime() > (duration + derivation))) {
                continue;
            }

            if (rating != 0 && series.getRating() < rating) {
                continue;
            }

            if (seasons != 0 && series.getNumberOfSeasons() != seasons) {
                continue;
            }

            switch (series.getUserState()) {
                case 0:
                    if (!checkStarted.isSelected()) {
                        continue;
                    }
                    break;
                case 1:
                    if (!checkWatching.isSelected()) {
                        continue;
                    }
                    break;
                case 2:
                    if (!checkWaiting.isSelected()) {
                        continue;
                    }
                    break;
                case 3:
                    if (!checkFinished.isSelected()) {
                        continue;
                    }
                    break;
            }

            if (radioContinuing.isSelected() && !series.getStatus().equals("Continuing")) {
                continue;
            }

            if (radioEnded.isSelected() && !series.getStatus().equals("Ended")) {
                continue;
            }

            matches.add(series);
        }

        if (matches.size() != 0) {
            mode = true;
            for (MySeries series : matches) {
                foundMatches.getItems().add(series.getName());
            }
            tmpMatches = matches;
        } else {
            PopUp.error("No matches found for your search!");
        }

        initialize();
    }

    public void reSearch() {
        foundMatches.getItems().clear();
        mode = false;
        initialize();
    }

    public void showInformation() {
        //Display information like seasons, episodes and current
        if (foundMatches.getSelectionModel().getSelectedItem() != null) {
            try {
                List<MySeries> allSeries = MySeries.readData();
                for (MySeries series : allSeries) {
                    if (series.getName().equals(foundMatches.getSelectionModel().getSelectedItem())) {
                        toController = series;
                        break;
                    }
                }

                URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
                Image img = new Image(resource.toString());

                Stage primaryStage = (Stage) infoButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/Information.fxml"));
                primaryStage.setTitle("Information about " + foundMatches.getSelectionModel().getSelectedItem());
                primaryStage.getIcons().add(img);
                primaryStage.setScene(new Scene(root));
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            PopUp.error("Select a series you want to get information about!");
        }
    }

    public void close() {
        try {
            Stage primaryStage = (Stage) closeButton.getScene().getWindow();
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
