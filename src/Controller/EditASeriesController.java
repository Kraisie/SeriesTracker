package Controller;

import Data.Series;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EditASeriesController {

    @FXML
    public TextField seriesName;
    @FXML
    public Spinner<Integer> numberSeasons;
    @FXML
    public Spinner<Integer> numberEpisodesSeason1;
    @FXML
    public Spinner<Integer> numberEpisodesSeason2;
    @FXML
    public Spinner<Integer> numberEpisodesSeason3;
    @FXML
    public Spinner<Integer> numberEpisodesSeason4;
    @FXML
    public Spinner<Integer> numberEpisodesSeason5;
    @FXML
    public Spinner<Integer> numberEpisodesSeason6;
    @FXML
    public Spinner<Integer> numberEpisodesSeason7;
    @FXML
    public Spinner<Integer> numberEpisodesSeason8;
    @FXML
    public Spinner<Integer> numberEpisodesSeason9;
    @FXML
    public Spinner<Integer> numberEpisodesSeason10;
    @FXML
    public Spinner<Integer> numberEpisodesSeason11;
    @FXML
    public Spinner<Integer> numberEpisodesSeason12;
    @FXML
    public Spinner<Integer> numberEpisodesSeason13;
    @FXML
    public Spinner<Integer> numberEpisodesSeason14;
    @FXML
    public Spinner<Integer> numberEpisodesSeason15;
    @FXML
    public Spinner<Integer> numberEpisodesSeason16;
    @FXML
    public Spinner<Integer> numberEpisodesSeason17;
    @FXML
    public Spinner<Integer> numberEpisodesSeason18;
    @FXML
    public Spinner<Integer> spinnerStateSeries;
    @FXML
    public Button buttonBack;

    public void initialize() {
        setValueFactory();
        seriesName.setText(EditController.toController.getName());
        numberSeasons.getValueFactory().setValue(EditController.toController.getSeasons());

        int[] episodes = EditController.toController.getEpisodes();
        switch (EditController.toController.getSeasons()) {
            case 18:
                numberEpisodesSeason18.getValueFactory().setValue(episodes[17]);
            case 17:
                numberEpisodesSeason17.getValueFactory().setValue(episodes[16]);
            case 16:
                numberEpisodesSeason16.getValueFactory().setValue(episodes[15]);
            case 15:
                numberEpisodesSeason15.getValueFactory().setValue(episodes[14]);
            case 14:
                numberEpisodesSeason14.getValueFactory().setValue(episodes[13]);
            case 13:
                numberEpisodesSeason13.getValueFactory().setValue(episodes[12]);
            case 12:
                numberEpisodesSeason12.getValueFactory().setValue(episodes[11]);
            case 11:
                numberEpisodesSeason11.getValueFactory().setValue(episodes[10]);
            case 10:
                numberEpisodesSeason10.getValueFactory().setValue(episodes[9]);
            case 9:
                numberEpisodesSeason9.getValueFactory().setValue(episodes[8]);
            case 8:
                numberEpisodesSeason8.getValueFactory().setValue(episodes[7]);
            case 7:
                numberEpisodesSeason7.getValueFactory().setValue(episodes[6]);
            case 6:
                numberEpisodesSeason6.getValueFactory().setValue(episodes[5]);
            case 5:
                numberEpisodesSeason5.getValueFactory().setValue(episodes[4]);
            case 4:
                numberEpisodesSeason4.getValueFactory().setValue(episodes[3]);
            case 3:
                numberEpisodesSeason3.getValueFactory().setValue(episodes[2]);
            case 2:
                numberEpisodesSeason2.getValueFactory().setValue(episodes[1]);
            case 1:
                numberEpisodesSeason1.getValueFactory().setValue(episodes[0]);
        }

        spinnerStateSeries.getValueFactory().setValue(EditController.toController.getState());
    }

    private void setValueFactory() {
        //1
        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
        TextFormatter formatter1 = new TextFormatter(valueFactory1.getConverter(), valueFactory1.getValue());
        numberSeasons.getEditor().setTextFormatter(formatter1);
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
        //state
        SpinnerValueFactory<Integer> valueFactory20 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3);
        TextFormatter formatter20 = new TextFormatter(valueFactory20.getConverter(), valueFactory20.getValue());
        spinnerStateSeries.getEditor().setTextFormatter(formatter20);
        valueFactory20.valueProperty().bindBidirectional(formatter20.valueProperty());

        numberSeasons.setValueFactory(valueFactory1);
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
        spinnerStateSeries.setValueFactory(valueFactory20);
    }


    public void editSeries() {
        List<Series> allSeries = Series.readData();
        for (Series allSery : allSeries) {
            if (EditController.toController.getName().equals(allSery.getName())) {
                allSery.setName(seriesName.getText());
                allSery.setSeasons(numberSeasons.getValue());

                int[] episodes = new int[numberSeasons.getValue()];
                for(int i = 0; i < episodes.length; i++){
                    switch(i){
                        case 0: episodes[0] = numberEpisodesSeason1.getValue();
                            break;
                        case 1: episodes[1] = numberEpisodesSeason2.getValue();
                            break;
                        case 2: episodes[2] = numberEpisodesSeason3.getValue();
                            break;
                        case 3: episodes[3] = numberEpisodesSeason4.getValue();
                            break;
                        case 4: episodes[4] = numberEpisodesSeason5.getValue();
                            break;
                        case 5: episodes[5] = numberEpisodesSeason6.getValue();
                            break;
                        case 6: episodes[6] = numberEpisodesSeason7.getValue();
                            break;
                        case 7: episodes[7] = numberEpisodesSeason8.getValue();
                            break;
                        case 8: episodes[8] = numberEpisodesSeason9.getValue();
                            break;
                        case 9: episodes[9] = numberEpisodesSeason10.getValue();
                            break;
                        case 10: episodes[10] = numberEpisodesSeason11.getValue();
                            break;
                        case 11: episodes[11] = numberEpisodesSeason12.getValue();
                            break;
                        case 12: episodes[12] = numberEpisodesSeason13.getValue();
                            break;
                        case 13: episodes[13] = numberEpisodesSeason14.getValue();
                            break;
                        case 14: episodes[14] = numberEpisodesSeason15.getValue();
                            break;
                        case 15: episodes[15] = numberEpisodesSeason16.getValue();
                            break;
                        case 16: episodes[16] = numberEpisodesSeason17.getValue();
                            break;
                        case 17: episodes[17] = numberEpisodesSeason18.getValue();
                            break;
                    }
                }
                allSery.setEpisodes(episodes);
                allSery.setState(spinnerStateSeries.getValue());
                break;
            }
        }

        allSeries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        Series.writeData(allSeries);
        backMainMenu();
    }

    public void back() {
        try {
            Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/EditSeries.fxml"));
            primaryStage.setTitle("Edit Series");
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backMainMenu(){
        try {
            Stage primaryStage = (Stage) buttonBack.getScene().getWindow();
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
