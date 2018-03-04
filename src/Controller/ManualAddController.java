package Controller;

import Data.Episode;
import Data.Series;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManualAddController {

    @FXML public Label labelNameSeries;
    @FXML public TextField nameSeries;
    @FXML public Label labelDiscontinued;
    @FXML public ChoiceBox<String> choiceDiscontinued;
    @FXML public Label labelAvgTime;
    @FXML public Label labelInMinutes;
    @FXML public Spinner<Integer> spinnerAvgTimeEpisode;
    @FXML public Label labelRating;
    @FXML public Label labelPoints;
    @FXML public Spinner<Double> spinnerRating;
    @FXML public Label labelDescription;
    @FXML public TextArea textAreaDescription;

    @FXML public Label labelNumberSeasons;
    @FXML public Label labelEpisodesSeason1;
    @FXML public Label labelEpisodesSeason2;
    @FXML public Label labelEpisodesSeason3;
    @FXML public Label labelEpisodesSeason4;
    @FXML public Label labelEpisodesSeason5;
    @FXML public Label labelEpisodesSeason6;
    @FXML public Label labelEpisodesSeason7;
    @FXML public Label labelEpisodesSeason8;
    @FXML public Label labelEpisodesSeason9;
    @FXML public Label labelEpisodesSeason10;
    @FXML public Label labelEpisodesSeason11;
    @FXML public Label labelEpisodesSeason12;
    @FXML public Label labelEpisodesSeason13;
    @FXML public Label labelEpisodesSeason14;
    @FXML public Label labelEpisodesSeason15;
    @FXML public Label labelEpisodesSeason16;
    @FXML public Label labelEpisodesSeason17;
    @FXML public Label labelEpisodesSeason18;
    @FXML public Spinner<Integer> spinnerNumberSeasons;
    @FXML public Spinner<Integer> numberEpisodesSeason1;
    @FXML public Spinner<Integer> numberEpisodesSeason2;
    @FXML public Spinner<Integer> numberEpisodesSeason3;
    @FXML public Spinner<Integer> numberEpisodesSeason4;
    @FXML public Spinner<Integer> numberEpisodesSeason5;
    @FXML public Spinner<Integer> numberEpisodesSeason6;
    @FXML public Spinner<Integer> numberEpisodesSeason7;
    @FXML public Spinner<Integer> numberEpisodesSeason8;
    @FXML public Spinner<Integer> numberEpisodesSeason9;
    @FXML public Spinner<Integer> numberEpisodesSeason10;
    @FXML public Spinner<Integer> numberEpisodesSeason11;
    @FXML public Spinner<Integer> numberEpisodesSeason12;
    @FXML public Spinner<Integer> numberEpisodesSeason13;
    @FXML public Spinner<Integer> numberEpisodesSeason14;
    @FXML public Spinner<Integer> numberEpisodesSeason15;
    @FXML public Spinner<Integer> numberEpisodesSeason16;
    @FXML public Spinner<Integer> numberEpisodesSeason17;
    @FXML public Spinner<Integer> numberEpisodesSeason18;

    @FXML public Button nextButton;

    private int page = 0;

    public void initialize(){
        setSpinnerFactory();
        choiceDiscontinued.getItems().addAll("Continuing", "Discontinued");
        choiceDiscontinued.getSelectionModel().select(0);
        page++;

        switch(page){
            case 1: page1();
                    break;
            case 2: page2();
                    break;
        }
    }

    private void page1(){
        nextButton.setText("Next");
        //VIS
        labelNameSeries.setVisible(true);
        nameSeries.setVisible(true);
        nameSeries.setDisable(false);
        labelDiscontinued.setVisible(true);
        choiceDiscontinued.setVisible(true);
        choiceDiscontinued.setDisable(false);
        labelAvgTime.setVisible(true);
        spinnerAvgTimeEpisode.setVisible(true);
        spinnerAvgTimeEpisode.setDisable(false);
        labelInMinutes.setVisible(true);
        spinnerAvgTimeEpisode.setVisible(true);
        spinnerAvgTimeEpisode.setDisable(false);
        labelRating.setVisible(true);
        labelPoints.setVisible(true);
        spinnerRating.setVisible(true);
        spinnerRating.setDisable(false);
        labelDescription.setVisible(true);
        textAreaDescription.setVisible(true);
        textAreaDescription.setDisable(false);
        //------------------------------------//
        //INVIS
        labelNumberSeasons.setVisible(false);
        spinnerNumberSeasons.setVisible(false);
        spinnerNumberSeasons.setDisable(true);
        labelEpisodesSeason1.setVisible(false);
        numberEpisodesSeason1.setVisible(false);
        numberEpisodesSeason1.setDisable(true);
        labelEpisodesSeason2.setVisible(false);
        numberEpisodesSeason2.setVisible(false);
        numberEpisodesSeason2.setDisable(true);
        labelEpisodesSeason3.setVisible(false);
        numberEpisodesSeason3.setVisible(false);
        numberEpisodesSeason3.setDisable(true);
        labelEpisodesSeason4.setVisible(false);
        numberEpisodesSeason4.setVisible(false);
        numberEpisodesSeason4.setDisable(true);
        labelEpisodesSeason5.setVisible(false);
        numberEpisodesSeason5.setVisible(false);
        numberEpisodesSeason5.setDisable(true);
        labelEpisodesSeason6.setVisible(false);
        numberEpisodesSeason6.setVisible(false);
        numberEpisodesSeason6.setDisable(true);
        labelEpisodesSeason7.setVisible(false);
        numberEpisodesSeason7.setVisible(false);
        numberEpisodesSeason7.setDisable(true);
        labelEpisodesSeason8.setVisible(false);
        numberEpisodesSeason8.setVisible(false);
        numberEpisodesSeason8.setDisable(true);
        labelEpisodesSeason9.setVisible(false);
        numberEpisodesSeason9.setVisible(false);
        numberEpisodesSeason9.setDisable(true);
        labelEpisodesSeason10.setVisible(false);
        numberEpisodesSeason10.setVisible(false);
        numberEpisodesSeason10.setDisable(true);
        labelEpisodesSeason11.setVisible(false);
        numberEpisodesSeason11.setVisible(false);
        numberEpisodesSeason11.setDisable(true);
        labelEpisodesSeason12.setVisible(false);
        numberEpisodesSeason12.setVisible(false);
        numberEpisodesSeason12.setDisable(true);
        labelEpisodesSeason13.setVisible(false);
        numberEpisodesSeason13.setVisible(false);
        numberEpisodesSeason13.setDisable(true);
        labelEpisodesSeason14.setVisible(false);
        numberEpisodesSeason14.setVisible(false);
        numberEpisodesSeason14.setDisable(true);
        labelEpisodesSeason15.setVisible(false);
        numberEpisodesSeason14.setVisible(false);
        numberEpisodesSeason15.setDisable(true);
        labelEpisodesSeason16.setVisible(false);
        numberEpisodesSeason16.setVisible(false);
        numberEpisodesSeason16.setDisable(true);
        labelEpisodesSeason17.setVisible(false);
        numberEpisodesSeason17.setVisible(false);
        numberEpisodesSeason17.setDisable(true);
        labelEpisodesSeason18.setVisible(false);
        numberEpisodesSeason18.setVisible(false);
        numberEpisodesSeason18.setDisable(true);
    }

    private void page2(){
        nextButton.setText("Add");
        //VIS
        labelNumberSeasons.setVisible(true);
        spinnerNumberSeasons.setVisible(true);
        spinnerNumberSeasons.setDisable(false);
        labelEpisodesSeason1.setVisible(true);
        numberEpisodesSeason1.setVisible(true);
        numberEpisodesSeason1.setDisable(false);
        labelEpisodesSeason2.setVisible(true);
        numberEpisodesSeason2.setVisible(true);
        numberEpisodesSeason2.setDisable(false);
        labelEpisodesSeason3.setVisible(true);
        numberEpisodesSeason3.setVisible(true);
        numberEpisodesSeason3.setDisable(false);
        labelEpisodesSeason4.setVisible(true);
        numberEpisodesSeason4.setVisible(true);
        numberEpisodesSeason4.setDisable(false);
        labelEpisodesSeason5.setVisible(true);
        numberEpisodesSeason5.setVisible(true);
        numberEpisodesSeason5.setDisable(false);
        labelEpisodesSeason6.setVisible(true);
        numberEpisodesSeason6.setVisible(true);
        numberEpisodesSeason6.setDisable(false);
        labelEpisodesSeason7.setVisible(true);
        numberEpisodesSeason7.setVisible(true);
        numberEpisodesSeason7.setDisable(false);
        labelEpisodesSeason8.setVisible(true);
        numberEpisodesSeason8.setVisible(true);
        numberEpisodesSeason8.setDisable(false);
        labelEpisodesSeason9.setVisible(true);
        numberEpisodesSeason9.setVisible(true);
        numberEpisodesSeason9.setDisable(false);
        labelEpisodesSeason10.setVisible(true);
        numberEpisodesSeason10.setVisible(true);
        numberEpisodesSeason10.setDisable(false);
        labelEpisodesSeason11.setVisible(true);
        numberEpisodesSeason11.setVisible(true);
        numberEpisodesSeason11.setDisable(false);
        labelEpisodesSeason12.setVisible(true);
        numberEpisodesSeason12.setVisible(true);
        numberEpisodesSeason12.setDisable(false);
        labelEpisodesSeason13.setVisible(true);
        numberEpisodesSeason13.setVisible(true);
        numberEpisodesSeason13.setDisable(false);
        labelEpisodesSeason14.setVisible(true);
        numberEpisodesSeason14.setVisible(true);
        numberEpisodesSeason14.setDisable(false);
        labelEpisodesSeason15.setVisible(true);
        numberEpisodesSeason14.setVisible(true);
        numberEpisodesSeason15.setDisable(false);
        labelEpisodesSeason16.setVisible(true);
        numberEpisodesSeason16.setVisible(true);
        numberEpisodesSeason16.setDisable(false);
        labelEpisodesSeason17.setVisible(true);
        numberEpisodesSeason17.setVisible(true);
        numberEpisodesSeason17.setDisable(false);
        labelEpisodesSeason18.setVisible(true);
        numberEpisodesSeason18.setVisible(true);
        numberEpisodesSeason18.setDisable(false);
        //------------------------------------//
        //INVIS
        labelNameSeries.setVisible(false);
        nameSeries.setVisible(false);
        nameSeries.setDisable(true);
        labelDiscontinued.setVisible(false);
        choiceDiscontinued.setVisible(false);
        choiceDiscontinued.setDisable(true);
        labelAvgTime.setVisible(false);
        spinnerAvgTimeEpisode.setVisible(false);
        spinnerAvgTimeEpisode.setDisable(true);
        labelInMinutes.setVisible(false);
        spinnerAvgTimeEpisode.setVisible(false);
        spinnerAvgTimeEpisode.setDisable(true);
        labelRating.setVisible(false);
        labelPoints.setVisible(false);
        spinnerRating.setVisible(false);
        spinnerRating.setDisable(true);
        labelDescription.setVisible(false);
        textAreaDescription.setVisible(false);
        textAreaDescription.setDisable(true);

    }

    private void setSpinnerFactory(){
        //avg Time
        SpinnerValueFactory<Integer> valueFactory0 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000);
        TextFormatter formatter0 = new TextFormatter(valueFactory0.getConverter(), valueFactory0.getValue());
        spinnerAvgTimeEpisode.getEditor().setTextFormatter(formatter0);
        valueFactory0.valueProperty().bindBidirectional(formatter0.valueProperty());
        //rating
        SpinnerValueFactory<Double> valueFactoryRating = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10);
        TextFormatter formatterRating = new TextFormatter(valueFactoryRating.getConverter(), valueFactoryRating.getValue());
        spinnerRating.getEditor().setTextFormatter(formatterRating);
        valueFactoryRating.valueProperty().bindBidirectional(formatterRating.valueProperty());
        //1
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter1 = new TextFormatter(valueFactory1.getConverter(), valueFactory1.getValue());
        spinnerNumberSeasons.getEditor().setTextFormatter(formatter1);
        valueFactory1.valueProperty().bindBidirectional(formatter1.valueProperty());
        //2
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter2 = new TextFormatter(valueFactory2.getConverter(), valueFactory2.getValue());
        numberEpisodesSeason1.getEditor().setTextFormatter(formatter2);
        valueFactory2.valueProperty().bindBidirectional(formatter2.valueProperty());
        //3
        SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter3 = new TextFormatter(valueFactory3.getConverter(), valueFactory3.getValue());
        numberEpisodesSeason2.getEditor().setTextFormatter(formatter3);
        valueFactory3.valueProperty().bindBidirectional(formatter3.valueProperty());
        //4
        SpinnerValueFactory<Integer> valueFactory4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter4 = new TextFormatter(valueFactory4.getConverter(), valueFactory4.getValue());
        numberEpisodesSeason3.getEditor().setTextFormatter(formatter4);
        valueFactory4.valueProperty().bindBidirectional(formatter4.valueProperty());
        //5
        SpinnerValueFactory<Integer> valueFactory5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter5 = new TextFormatter(valueFactory5.getConverter(), valueFactory5.getValue());
        numberEpisodesSeason4.getEditor().setTextFormatter(formatter5);
        valueFactory5.valueProperty().bindBidirectional(formatter5.valueProperty());
        //6
        SpinnerValueFactory<Integer> valueFactory6 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter6 = new TextFormatter(valueFactory6.getConverter(), valueFactory6.getValue());
        numberEpisodesSeason5.getEditor().setTextFormatter(formatter6);
        valueFactory6.valueProperty().bindBidirectional(formatter6.valueProperty());
        //7
        SpinnerValueFactory<Integer> valueFactory7 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter7 = new TextFormatter(valueFactory7.getConverter(), valueFactory7.getValue());
        numberEpisodesSeason6.getEditor().setTextFormatter(formatter7);
        valueFactory7.valueProperty().bindBidirectional(formatter7.valueProperty());
        //8
        SpinnerValueFactory<Integer> valueFactory8 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter8 = new TextFormatter(valueFactory8.getConverter(), valueFactory8.getValue());
        numberEpisodesSeason7.getEditor().setTextFormatter(formatter8);
        valueFactory8.valueProperty().bindBidirectional(formatter8.valueProperty());
        //9
        SpinnerValueFactory<Integer> valueFactory9 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter9 = new TextFormatter(valueFactory9.getConverter(), valueFactory9.getValue());
        numberEpisodesSeason8.getEditor().setTextFormatter(formatter9);
        valueFactory9.valueProperty().bindBidirectional(formatter9.valueProperty());
        //10
        SpinnerValueFactory<Integer> valueFactory10 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter10 = new TextFormatter(valueFactory10.getConverter(), valueFactory10.getValue());
        numberEpisodesSeason9.getEditor().setTextFormatter(formatter10);
        valueFactory10.valueProperty().bindBidirectional(formatter10.valueProperty());
        //11
        SpinnerValueFactory<Integer> valueFactory11 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter11 = new TextFormatter(valueFactory11.getConverter(), valueFactory11.getValue());
        numberEpisodesSeason10.getEditor().setTextFormatter(formatter11);
        valueFactory11.valueProperty().bindBidirectional(formatter11.valueProperty());
        //12
        SpinnerValueFactory<Integer> valueFactory12 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter12 = new TextFormatter(valueFactory12.getConverter(), valueFactory12.getValue());
        numberEpisodesSeason11.getEditor().setTextFormatter(formatter12);
        valueFactory12.valueProperty().bindBidirectional(formatter12.valueProperty());
        //13
        SpinnerValueFactory<Integer> valueFactory13 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter13 = new TextFormatter(valueFactory13.getConverter(), valueFactory13.getValue());
        numberEpisodesSeason12.getEditor().setTextFormatter(formatter13);
        valueFactory13.valueProperty().bindBidirectional(formatter13.valueProperty());
        //14
        SpinnerValueFactory<Integer> valueFactory14 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter14 = new TextFormatter(valueFactory14.getConverter(), valueFactory14.getValue());
        numberEpisodesSeason13.getEditor().setTextFormatter(formatter14);
        valueFactory14.valueProperty().bindBidirectional(formatter14.valueProperty());
        //15
        SpinnerValueFactory<Integer> valueFactory15 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter15 = new TextFormatter(valueFactory15.getConverter(), valueFactory15.getValue());
        numberEpisodesSeason14.getEditor().setTextFormatter(formatter15);
        valueFactory15.valueProperty().bindBidirectional(formatter15.valueProperty());
        //16
        SpinnerValueFactory<Integer> valueFactory16 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter16 = new TextFormatter(valueFactory16.getConverter(), valueFactory16.getValue());
        numberEpisodesSeason15.getEditor().setTextFormatter(formatter16);
        valueFactory16.valueProperty().bindBidirectional(formatter16.valueProperty());
        //17
        SpinnerValueFactory<Integer> valueFactory17 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter17 = new TextFormatter(valueFactory17.getConverter(), valueFactory17.getValue());
        numberEpisodesSeason16.getEditor().setTextFormatter(formatter17);
        valueFactory17.valueProperty().bindBidirectional(formatter17.valueProperty());
        //18
        SpinnerValueFactory<Integer> valueFactory18 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter18 = new TextFormatter(valueFactory18.getConverter(), valueFactory18.getValue());
        numberEpisodesSeason17.getEditor().setTextFormatter(formatter18);
        valueFactory18.valueProperty().bindBidirectional(formatter18.valueProperty());
        //19
        SpinnerValueFactory<Integer> valueFactory19 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter19 = new TextFormatter(valueFactory19.getConverter(), valueFactory19.getValue());
        numberEpisodesSeason18.getEditor().setTextFormatter(formatter19);
        valueFactory19.valueProperty().bindBidirectional(formatter19.valueProperty());

        spinnerNumberSeasons.setValueFactory(valueFactory1);
        numberEpisodesSeason1.setValueFactory(valueFactory2);
        numberEpisodesSeason2.setValueFactory(valueFactory3);
        numberEpisodesSeason3.setValueFactory(valueFactory4);
        numberEpisodesSeason4.setValueFactory(valueFactory5);
        numberEpisodesSeason5.setValueFactory(valueFactory6);
        numberEpisodesSeason6.setValueFactory(valueFactory7);
        numberEpisodesSeason7.setValueFactory(valueFactory8);
        numberEpisodesSeason8.setValueFactory(valueFactory9);
        numberEpisodesSeason9.setValueFactory(valueFactory10);
        numberEpisodesSeason10.setValueFactory(valueFactory11);
        numberEpisodesSeason11.setValueFactory(valueFactory12);
        numberEpisodesSeason12.setValueFactory(valueFactory13);
        numberEpisodesSeason13.setValueFactory(valueFactory14);
        numberEpisodesSeason14.setValueFactory(valueFactory15);
        numberEpisodesSeason15.setValueFactory(valueFactory16);
        numberEpisodesSeason16.setValueFactory(valueFactory17);
        numberEpisodesSeason17.setValueFactory(valueFactory18);
        numberEpisodesSeason18.setValueFactory(valueFactory19);
    }

    public void nextOrAdd(){
        if(page == 0)
            initialize();
        if(page == 1)
            addSeries();
    }

    private void addSeries(){
//        List<Series> allSeries = Series.readData();
//
//        if(Series.checkDuplicate(allSeries, nameSeries.getText())){
//            List<Episode> episodes = new ArrayList<>();
//            for(int i = 0; i < spinnerNumberSeasons.getValue(); i++){
//                episodes.add(new Episode(0,0,"-", "-"));
//            }
//            //name, episodes, 0,  status, runtime, description, rating, 1, 1
//            Series newSeries = new Series(nameSeries.getText(), episodes, 0, nameSeries.getSelectedText(), spinnerAvgTimeEpisode.getValue(), textAreaDescription.getText(), spinnerRating.getValue());
//            allSeries.add(newSeries);
//            allSeries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));     //Ignore case since Z is in front of a (regarding cases)
//            Series.writeData(allSeries);
//        }
//
//        back();
    }

    public void back(){
        try {
            Stage primaryStage = (Stage) nextButton.getScene().getWindow();
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
