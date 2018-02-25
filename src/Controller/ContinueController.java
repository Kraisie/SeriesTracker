package Controller;

import Data.Episode;
import Data.Series;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ContinueController {
    @FXML
    public Label labelSeason;
    @FXML
    public Label labelEpisode;
    @FXML
    public Spinner<Integer> numberEpisodes;

    public void initialize(){
        labelSeason.setAlignment(Pos.CENTER);
        labelEpisode.setAlignment(Pos.CENTER);
        labelSeason.setText("How many episodes does season " + (MainMenuController.toController.getCurrent().getSeason() + 1));
        labelEpisode.setText(" of '" + MainMenuController.toController.getName() + "' have?");

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter = new TextFormatter(valueFactory.getConverter(), valueFactory.getValue());
        numberEpisodes.getEditor().setTextFormatter(formatter);
        valueFactory.valueProperty().bindBidirectional(formatter.valueProperty());
        numberEpisodes.setValueFactory(valueFactory);
    }

    public void save(){
        //edit series accordingly
        List<Series> allSeries = Series.readData();
        for (Series series : allSeries) {
            if (series.equals(MainMenuController.toController)) {
                //get new number of seasons (now + 1)
                List<Episode> episodes = series.getEpisodes();
                for(int i = 0; i < numberEpisodes.getValue(); i++){
                    episodes.add(new Episode(0,0,"",""));
                }

                //set state to watching and set current to current Season+1 and episode 1
                series.setUserState(1);
                series.setEpisodes(episodes);
            }
        }

        Series.writeData(allSeries);
        cancel();
    }

    public void cancel(){
        try {
            Stage primaryStage = (Stage) numberEpisodes.getScene().getWindow();
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
