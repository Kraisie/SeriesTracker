package Controller;

import Code.PopUp;
import Data.Episode;
import Data.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainMenuController {
    @FXML
    public ImageView imageBackground;

    @FXML
    public TableView<Series> tableContinueWatching;
    @FXML
    public TableView<Series> tableWaitEpisodes;
    @FXML
    public TableView<Series> tableStartWatching;
    @FXML
    public TableColumn<Series, String> columnContinueName;
    @FXML
    public TableColumn<Series, Integer> columnContinueSeason;
    @FXML
    public TableColumn<Series, Integer> columnContinueEpisode;
    @FXML
    public TableColumn<Series, String> columnWaitName;
    @FXML
    public TableColumn<Series, Integer> columnWaitSeason;
    @FXML
    public TableColumn<Series, Integer> columnWaitEpisode;
    @FXML
    public TableColumn<Series, String> columnStartName;
    @FXML
    public TableColumn<Series, Integer> columnStartSeasons;

    @FXML
    public Button buttonIncEpisode;
    @FXML
    public Button buttonDecEpisode;
    @FXML
    public Button buttonNewEpisodes;
    @FXML
    public Button buttonSeriesDiscontinued;
    @FXML
    public Button buttonCheckFinished;
    @FXML
    public Button buttonAddSeries;
    @FXML
    public Button buttonEditSeries;
    @FXML
    public Button buttonStartedSeries;
    @FXML
    public Button buttonDelSeries;

    @FXML
    public Label labelWatching;
    @FXML
    public Label labelWaiting;
    @FXML
    public Label labelStarting;

    private BufferedImage bufImg;
    public static Series toController;
    private boolean backgroundSet = false;

    public void initialize() {
        //Write series to TableView and set background
        if (!backgroundSet) {
            setBackground();
            backgroundSet = true;
        }

        //get Pixels of background on position of TableHeaders, invert Color and set Labelcolor
        setTextfillInvColor(labelWatching);
        setTextfillInvColor(labelWaiting);
        setTextfillInvColor(labelStarting);

        toController = null;
        ObservableList<Series> notStartedSeries = FXCollections.observableArrayList();
        ObservableList<Series> watchingSeries = FXCollections.observableArrayList();
        ObservableList<Series> waitNewEpisode = FXCollections.observableArrayList();

        ObservableList<Series> listEntries = FXCollections.observableArrayList(Series.readData());

        //sort by name
        listEntries.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        if (!listEntries.isEmpty()) {
            for (Series listEntry : listEntries) {
                switch (listEntry.getUserState()) {
                    case 0:
                        notStartedSeries.add(listEntry);
                        break;
                    case 1:
                        watchingSeries.add(listEntry);
                        break;
                    case 2:
                        waitNewEpisode.add(listEntry);
                        break;
                }
            }
        }

        columnContinueName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnContinueSeason.setCellValueFactory(new PropertyValueFactory<>("currentSeason"));
        columnContinueEpisode.setCellValueFactory(new PropertyValueFactory<>("currentEpisode"));

        columnWaitName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnWaitSeason.setCellValueFactory(new PropertyValueFactory<>("currentSeason"));
        columnWaitEpisode.setCellValueFactory(new PropertyValueFactory<>("currentEpisode"));

        columnStartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnStartSeasons.setCellValueFactory(new PropertyValueFactory<>("numberOfSeasons"));

        tableContinueWatching.setItems(watchingSeries);
        tableWaitEpisodes.setItems(waitNewEpisode);
        tableStartWatching.setItems(notStartedSeries);

        tableContinueWatching.getColumns().get(0).setVisible(false);
        tableContinueWatching.getColumns().get(0).setVisible(true);
        tableWaitEpisodes.getColumns().get(0).setVisible(false);
        tableWaitEpisodes.getColumns().get(0).setVisible(true);
    }

    private void setBackground() {
        //Images have to be 1052x632 for perfect fit
        URL resource0 = MainMenuController.class.getResource("/resources/Pics/dexter.jpg");
        Image img0 = new Image(resource0.toString());

        URL resource1 = MainMenuController.class.getResource("/resources/Pics/GoT.jpg");
        Image img1 = new Image(resource1.toString());

        URL resource2 = MainMenuController.class.getResource("/resources/Pics/Lucifer.jpg");
        Image img2 = new Image(resource2.toString());

        URL resource3 = MainMenuController.class.getResource("/resources/Pics/mrRobot.jpg");
        Image img3 = new Image(resource3.toString());

        URL resource4 = MainMenuController.class.getResource("/resources/Pics/scrubs.jpg");
        Image img4 = new Image(resource4.toString());

        URL resource5 = MainMenuController.class.getResource("/resources/Pics/better-call-saul.jpg");
        Image img5 = new Image(resource5.toString());

        URL resource6 = MainMenuController.class.getResource("/resources/Pics/Supernatural.jpg");
        Image img6 = new Image(resource6.toString());

        URL resource7 = MainMenuController.class.getResource("/resources/Pics/breakingBad.jpg");
        Image img7 = new Image(resource7.toString());

        URL resource8 = MainMenuController.class.getResource("/resources/Pics/StrangerThings.jpg");
        Image img8 = new Image(resource8.toString());

        URL resource9 = MainMenuController.class.getResource("/resources/Pics/suits.jpg");
        Image img9 = new Image(resource9.toString());

        URL resource10 = MainMenuController.class.getResource("/resources/Pics/prisonBreak.jpg");
        Image img10 = new Image(resource10.toString());

        int numberImages = 11;
        double random = Math.random() * numberImages;
        int rdm = (int) random;

        switch (rdm) {
            case 0:
                imageBackground.setImage(img0);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/dexter.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                imageBackground.setImage(img1);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/GoT.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                imageBackground.setImage(img2);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/Lucifer.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                imageBackground.setImage(img3);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/mrRobot.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                imageBackground.setImage(img4);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/scrubs.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                imageBackground.setImage(img5);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/better-call-saul.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                imageBackground.setImage(img6);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/Supernatural.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                imageBackground.setImage(img7);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/breakingBad.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                imageBackground.setImage(img8);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/StrangerThings.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                imageBackground.setImage(img9);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/suits.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                imageBackground.setImage(img10);
                try {
                    InputStream is = MainMenuController.class.getResourceAsStream("/resources/Pics/prisonBreak.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setTextfillInvColor(Label label) {
        //get Pos (x is the far most left point; y the most far up point; not middle)
        List<Color> pixels = new ArrayList<>();
        for (int x = (int) label.getLayoutX(); x < (int) (label.getLayoutX() + label.getPrefWidth()); x++) {
            for (int y = (int) label.getLayoutY(); y < (int) (label.getLayoutY() + label.getPrefHeight()); y++) {
                pixels.add(new Color(bufImg.getRGB(x, y)));
            }
        }

        //get average RGB
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Color pixel : pixels) {
            red += pixel.getRed();
            green += pixel.getGreen();
            blue += pixel.getBlue();
        }

        int avgRed = red / pixels.size();
        int avgGreen = green / pixels.size();
        int avgBlue = blue / pixels.size();

        //invert Color and set Textfill
        Color color = new Color(avgRed, avgGreen, avgBlue);
        //String hexColor = getContrastBlackOrWhiteHex(color);
        String hexColor = getContrastColor(color);
        label.setTextFill(javafx.scene.paint.Color.web(hexColor));
    }

//    private String getContrastColor(Color color) {
//        //kinda gud method:
//        //avg R/G/B to binary                                                   --> 22 = 0001 0110
//        //1,2 & 3,4 & 5,6 & 7,8 with XOR (00 = 0; 01 = 1; 10 = 1; 11 = 0)       --> 0111
//        //4-digit xor reverse and append right order                            --> 1110 0111
//        //new binary to hex                                                     --> 1110 0111 = E7
//        //NVM WORKS FOR PIXEL VS PIXEL BUT SHIT FOR AREAS -______-
//        //AND GREY IS STILL SHIT
//        //FFFAAAAACCCCCKKKKKKK!!!!!!!!
//
//        int red = colorChannelToBinary(color.getRed());
//        int green = colorChannelToBinary(color.getGreen());
//        int blue = colorChannelToBinary(color.getBlue());
//
//        Color invColor = new Color(red, green, blue);
//
//        String hexColor = Integer.toHexString(invColor.getRGB() & 0xffffff);
//        if (hexColor.length() < 6) {
//            hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
//        }
//
//        return "#" + hexColor;
//    }

//    private int colorChannelToBinary(int channelValue) {
//        //binary of value
//        String binaryString = String.format("%8s", Integer.toBinaryString(channelValue)).replace(' ', '0');
//
//        //xor
//        char[] xor = new char[4];
//        char[] binaryChars = new char[8];
//        binaryString.getChars(0, 8, binaryChars, 0);        //8 is the end and won't get copied anymore
//        for(int i = 0; i < binaryChars.length; i+=2){
//            if(binaryChars[i] != binaryChars[i+1]){
//                xor[i/2] = '1';
//            }else{
//                xor[i/2] = '0';
//            }
//        }
//
//        //reverse and not reverse back in binary
//        char[] newBinaryChars = new char[8];
//        for(int i = 0; i < xor.length; i++){
//            newBinaryChars[i] = xor[3-i];
//        }
//        for(int i = 0; i < xor.length; i++){
//            newBinaryChars[i+4] = xor[i];
//        }
//
//        //binary to int
//        String newBinary = new String(newBinaryChars);
//
//        return Integer.parseInt(newBinary, 2);
//    }

    private String getContrastColor(Color color) {
        //get black or white regarding the background for most contrast (not another color)
        double yiq = ((color.getRed() * 299) + (color.getGreen() * 587) + (color.getBlue() * 114)) / 1000;
        Color invColor = new Color(255, 255, 255);
        if (yiq >= 128) {
            invColor = new Color(0, 0, 0);
        }

        //to hex
        String hexColor = Integer.toHexString(invColor.getRGB() & 0xffffff);
        if (hexColor.length() < 6) {
            hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
        }

        return "#" + hexColor;
    }

    public void incEpisode() {
        //add episode, if end of season add season
        if (tableContinueWatching.getSelectionModel().getSelectedItem() != null) {
            List<Series> allSeries = Series.readData();
            for (Series series : allSeries) {
                if (series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
                    series.getCurrent().setWatched(true);
                    series.setNewCurrent(series.getCurrent(), true);            //true = ++ ; false = --
                    series.getCurrent().setCurrent(false);

                }
            }

            Series.writeData(allSeries);
            initialize();
        } else {
            PopUp pop = new PopUp();
            pop.error("Select a series you want to increase the episode of!");
        }
    }

    public void decEpisode() {
        //decrease number of episode if start of season decrease season
        if (tableContinueWatching.getSelectionModel().getSelectedItem() != null) {
            List<Series> allSeries = Series.readData();
            for (Series series : allSeries) {
                if (series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
                    List<Episode> episodes = series.getEpisodes();
                    series.getCurrent().setWatched(true);
                    series.setNewCurrent(series.getCurrent(), false);            //true = ++ ; false = --
                    series.getCurrent().setCurrent(false);
                }
            }

            Series.writeData(allSeries);
            initialize();
        } else {
            PopUp pop = new PopUp();
            pop.error("Select a series you want to decrease the episode of!");
        }
    }

    public void displayInformation() {
        //Display information like seasons, episodes and current
        if (tableContinueWatching.getSelectionModel().getSelectedItem() != null) {
            try {
                toController = tableContinueWatching.getSelectionModel().getSelectedItem();
                URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
                Image img = new Image(resource.toString());

                Stage primaryStage = (Stage) buttonAddSeries.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/Information.fxml"));
                primaryStage.setTitle("Information about " + tableContinueWatching.getSelectionModel().getSelectedItem().getName());
                primaryStage.getIcons().add(img);
                primaryStage.setScene(new Scene(root));
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            PopUp pop = new PopUp();
            pop.error("Select a series you want to get information about!");
        }

    }

    public void waitGotNewEpisodes() {
        if (tableWaitEpisodes.getSelectionModel().getSelectedItem() != null) {
            //open window to add episodes-number of new season
            try {
                toController = tableWaitEpisodes.getSelectionModel().getSelectedItem();
                URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
                Image img = new Image(resource.toString());

                Stage primaryStage = (Stage) buttonAddSeries.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/Continue.fxml"));
                primaryStage.setTitle("Continue '" + tableWaitEpisodes.getSelectionModel().getSelectedItem().getName() + "'");
                primaryStage.getIcons().add(img);
                primaryStage.setScene(new Scene(root));
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            PopUp pop = new PopUp();
            pop.error("Select a series that got continued!");
        }
    }

    public void finishedSeries() {
        //mark series as finished if it gets disontinued, or it is finished
        if (tableWaitEpisodes.getSelectionModel().getSelectedItem() != null) {
            List<Series> allSeries = Series.readData();
            for (Series series : allSeries) {
                if (series.equals(tableWaitEpisodes.getSelectionModel().getSelectedItem())) {
                    series.setUserState(3);       //3 = finished
                }
            }

            Series.writeData(allSeries);
            initialize();
        } else {
            PopUp pop = new PopUp();
            pop.error("Select a series which status you want to change to \"finished\"!");
        }
    }

    public void addSeries() {
        //open popup to add series
        try {
            URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) buttonAddSeries.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/AddSeries.fxml"));
            primaryStage.setTitle("Add a new series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSeries() {
        //set state of series to 1 ("watching")
        if (tableStartWatching.getSelectionModel().getSelectedItem() != null) {
            List<Series> allSeries = Series.readData();
            for (Series allSery : allSeries) {
                if (allSery.equals(tableStartWatching.getSelectionModel().getSelectedItem())) {
                    allSery.setUserState(1);
                    break;
                }
            }

            Series.writeData(allSeries);
            initialize();
        } else {
            PopUp pop = new PopUp();
            pop.error("Select a series you want to start!");
        }
    }

    public void displayFinishedSeries() {
        //open popup to show all finished series (state=3)
        try {
            URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) buttonAddSeries.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/FinishedSeries.fxml"));
            primaryStage.setTitle("All finished Series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteSeries() {
        //open popup to delete a series
        try {
            URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) buttonDelSeries.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/DeleteSeries.fxml"));
            primaryStage.setTitle("Delete Series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editSeries() {
        //open popup to edit all series
        try {
            URL resource = MainMenuController.class.getResource("/resources/Pics/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) buttonAddSeries.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/EditSeries.fxml"));
            primaryStage.setTitle("Edit Series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
