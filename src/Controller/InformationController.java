package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class InformationController {

    @FXML
    public Label labelNameSeries;
    @FXML
    public Label labelNumberSeasons;
    @FXML
    public Label labelNumberEpisodes;
    @FXML
    public Label labelCurrentSeason;
    @FXML
    public Label labelCurrentEpisode;
    @FXML
    public Label labelPercentageCompletition;

    public void initialize(){
        labelNameSeries.setText(MainMenuController.toController.getName());
        labelNumberSeasons.setText(String.valueOf(MainMenuController.toController.getSeasons()));

        int[] episodes = MainMenuController.toController.getEpisodes();
        int sumEpisodes = 0;
        for(int epi : episodes){
            sumEpisodes += epi;
        }
        labelNumberEpisodes.setText(String.valueOf(sumEpisodes));

        labelCurrentSeason.setText(String.valueOf(MainMenuController.toController.getCurrentSeason()) + " / " + MainMenuController.toController.getSeasons());
        labelCurrentEpisode.setText(String.valueOf(MainMenuController.toController.getCurrentEpisode()) + " / " + episodes[MainMenuController.toController.getCurrentSeason() - 1]);

        int watchedEpisodes = 0;
        for(int i = 0; i+1 < MainMenuController.toController.getCurrentSeason(); i++){
            watchedEpisodes += episodes[i];
        }
        watchedEpisodes += MainMenuController.toController.getCurrentEpisode() - 1;
        double completion = (double)watchedEpisodes/sumEpisodes;
        completion *= 100;          //for percentage
        labelPercentageCompletition.setText(String.format("%.2f", completion) + "%");
    }

    public void back(){
        try {
            Stage primaryStage = (Stage) labelNameSeries.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/MainMenu.fxml"));
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
