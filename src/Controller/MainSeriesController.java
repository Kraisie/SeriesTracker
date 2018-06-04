package Controller;

import Code.PopUp;
import Data.BackUp;
import Data.Episode;
import Data.MySeries;
import Data.TVDB.TVDB_Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainSeriesController {
    @FXML
    public MenuBar menuBar;

    @FXML
    public ImageView imageBackground;

    @FXML
    public TableView<MySeries> tableContinueWatching;
    @FXML
    public TableView<MySeries> tableWaitEpisodes;
    @FXML
    public TableView<MySeries> tableStartWatching;
    @FXML
    public TableColumn<MySeries, String> columnContinueName;
    @FXML
    public TableColumn<MySeries, Integer> columnContinueSeason;
    @FXML
    public TableColumn<MySeries, Integer> columnContinueEpisode;
    @FXML
    public TableColumn<MySeries, String> columnWaitName;
    @FXML
    public TableColumn<MySeries, Integer> columnWaitSeason;
    @FXML
    public TableColumn<MySeries, Integer> columnWaitEpisode;
    @FXML
    public TableColumn<MySeries, String> columnStartName;
    @FXML
    public TableColumn<MySeries, Integer> columnStartSeasons;

    @FXML
    public Button infoButton;
    @FXML
    public Button buttonIncEpisode;
    @FXML
    public Button buttonDecEpisode;
    @FXML
    public Button buttonFinishedSeries;
    @FXML
    public Button buttonStartedSeries;
    @FXML
    public Label labelWatching;
    @FXML
    public Label labelWaiting;
    @FXML
    public Label labelStarting;

    @FXML
    public ProgressIndicator progressIndicator;

    private BufferedImage bufImg;
    public static MySeries toController;
    private boolean backgroundSet = false;
    private boolean updating = false;

    public void initialize() {
        if (!updating) {
            //Write series to TableView and set background
            if (!backgroundSet) {
                setBackground();
                backgroundSet = true;
            }

            //get Pixels of background on position of TableHeaders, choose if black or white has more contrast
            setTextfillInvColor(labelWatching);
            setTextfillInvColor(labelWaiting);
            setTextfillInvColor(labelStarting);

            toController = null;
            ObservableList<MySeries> notStartedSeries = FXCollections.observableArrayList();
            ObservableList<MySeries> watchingSeries = FXCollections.observableArrayList();
            ObservableList<MySeries> waitNewEpisode = FXCollections.observableArrayList();
            ObservableList<MySeries> listEntries = FXCollections.observableArrayList(MySeries.readData());

            if (!listEntries.isEmpty()) {
                for (MySeries listEntry : listEntries) {
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

            //Force Update
            tableContinueWatching.getColumns().get(0).setVisible(false);
            tableContinueWatching.getColumns().get(0).setVisible(true);
            tableWaitEpisodes.getColumns().get(0).setVisible(false);
            tableWaitEpisodes.getColumns().get(0).setVisible(true);
        }
    }

    private void setBackground() {
        //Images have to be 1052x632 for perfect fit
        URL resource0 = MainSeriesController.class.getResource("/resources/Pics/Background/dexter.jpg");
        Image img0 = new Image(resource0.toString());

        URL resource1 = MainSeriesController.class.getResource("/resources/Pics/Background/GoT.jpg");
        Image img1 = new Image(resource1.toString());

        URL resource2 = MainSeriesController.class.getResource("/resources/Pics/Background/Lucifer.jpg");
        Image img2 = new Image(resource2.toString());

        URL resource3 = MainSeriesController.class.getResource("/resources/Pics/Background/mrRobot.jpg");
        Image img3 = new Image(resource3.toString());

        URL resource4 = MainSeriesController.class.getResource("/resources/Pics/Background/scrubs.jpg");
        Image img4 = new Image(resource4.toString());

        URL resource5 = MainSeriesController.class.getResource("/resources/Pics/Background/better-call-saul.jpg");
        Image img5 = new Image(resource5.toString());

        URL resource6 = MainSeriesController.class.getResource("/resources/Pics/Background/Supernatural.jpg");
        Image img6 = new Image(resource6.toString());

        URL resource7 = MainSeriesController.class.getResource("/resources/Pics/Background/breakingBad.jpg");
        Image img7 = new Image(resource7.toString());

        URL resource8 = MainSeriesController.class.getResource("/resources/Pics/Background/StrangerThings.jpg");
        Image img8 = new Image(resource8.toString());

        URL resource9 = MainSeriesController.class.getResource("/resources/Pics/Background/suits.jpg");
        Image img9 = new Image(resource9.toString());

        URL resource10 = MainSeriesController.class.getResource("/resources/Pics/Background/prisonBreak.jpg");
        Image img10 = new Image(resource10.toString());

        int numberImages = 11;
        double random = Math.random() * numberImages;
        int rdm = (int) random;

        switch (rdm) {
            case 0:
                imageBackground.setImage(img0);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/dexter.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                imageBackground.setImage(img1);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/GoT.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                imageBackground.setImage(img2);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/Lucifer.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                imageBackground.setImage(img3);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/mrRobot.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                imageBackground.setImage(img4);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/scrubs.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                imageBackground.setImage(img5);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/better-call-saul.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                imageBackground.setImage(img6);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/Supernatural.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                imageBackground.setImage(img7);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/breakingBad.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                imageBackground.setImage(img8);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/StrangerThings.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                imageBackground.setImage(img9);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/suits.jpg");
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                imageBackground.setImage(img10);
                try {
                    InputStream is = MainSeriesController.class.getResourceAsStream("/resources/Pics/Background/prisonBreak.jpg");
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

    private String getContrastColor(Color color) {
        ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        float[] rgb = {color.getRed(), color.getGreen(), color.getBlue()};
        float[] sRGB = space.fromRGB(rgb);

        double hue;
        float max;
        float min;
        if (sRGB[0] >= sRGB[1] && sRGB[0] >= sRGB[2]) {
            max = sRGB[0];
            if (sRGB[1] >= sRGB[2]) {
                min = sRGB[2];
            } else {
                min = sRGB[0];
            }
        } else if (sRGB[1] >= sRGB[0] && sRGB[1] >= sRGB[2]) {
            max = sRGB[1];
            if (sRGB[0] >= sRGB[2]) {
                min = sRGB[2];
            } else {
                min = sRGB[0];
            }
        } else {
            max = sRGB[2];
            if (sRGB[0] >= sRGB[1]) {
                min = sRGB[1];
            } else {
                min = sRGB[0];
            }
        }

        if (max == sRGB[0]) {
            hue = (sRGB[1] - sRGB[2]) / (max - min);
        } else if (max == sRGB[1]) {
            hue = 2 + (sRGB[2] - sRGB[0]) / (max - min);
        } else {
            hue = 4 + (sRGB[0] - sRGB[1]) / (max - min);
        }

        hue = (60 * hue) % 360;

        Color invColor;
        if (hue > 5 && hue <= 90) {                 //normal 46-90
            invColor = Color.yellow;
        } else if (hue > 90 && hue <= 176) {        //normal 91-135
            invColor = Color.green;
        } else if (hue > 177 && hue <= 225) {       //normal 136-225
            invColor = Color.cyan;
        } else if (hue > 225 && hue <= 239) {       //normal 226-270
            invColor = Color.blue;
        } else if (hue > 239 && hue <= 315) {       //normal 271-315
            invColor = Color.magenta;
        } else {                                    //normal 316-45
            invColor = Color.red;
        }

        String hexColor = Integer.toHexString(invColor.getRGB() & 0xffffff);
        if (hexColor.length() < 6) {
            hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
        }

        return "#" + hexColor;
    }

    public void incEpisode() {
        //increase episode
        if (tableContinueWatching.getSelectionModel().getSelectedItem() != null) {
            List<MySeries> allSeries = MySeries.readData();
            for (MySeries series : allSeries) {
                if (series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
                    series.getCurrent().setWatched(true);
                    if (series.hasNext()) {
                        int index = series.getEpisodes().indexOf(series.getCurrent());
                        if (series.getEpisodes().get(index + 1).getFirstAired().equals("Not given!")) {
                            series.setUserState(2);
                            break;
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate date = LocalDate.parse(series.getEpisodes().get(index + 1).getFirstAired(), formatter);

                            if (!date.isBefore(LocalDate.now())) {
                                series.setUserState(2);
                                break;
                            }
                        }

                        series.setNewCurrent(series.getCurrent(), true);            //true = ++ ; false = --
                        break;
                    } else {
                        if (series.getStatus().equals("Ended")) {
                            series.setUserState(3);
                        } else {
                            series.setUserState(2);
                        }
                    }
                }
            }

            MySeries.writeData(allSeries);
            initialize();
        } else {
            PopUp.error("Select a series you want to increase the episode of!");
        }
    }

    public void decEpisode() {
        //decrease episode
        if (tableContinueWatching.getSelectionModel().getSelectedItem() != null) {
            List<MySeries> allSeries = MySeries.readData();
            for (MySeries series : allSeries) {
                if (series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
                    if (series.getCurrent().getSeason() != 1 || series.getCurrent().getEpNumberOfSeason() != 1) {
                        series.setNewCurrent(series.getCurrent(), false);                      //true = ++ ; false = --
                        series.getCurrent().setWatched(false);
                        break;
                    }
                }
            }

            MySeries.writeData(allSeries);
            initialize();
        } else {
            PopUp.error("Select a series you want to decrease the episode of!");
        }
    }

    public void displayInformation() {
        //Display information like seasons, episodes and current
        if (tableContinueWatching.getSelectionModel().getSelectedItem() != null) {
            try {
                toController = tableContinueWatching.getSelectionModel().getSelectedItem();
                URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
                Image img = new Image(resource.toString());

                Stage primaryStage = (Stage) menuBar.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/Information.fxml"));
                primaryStage.setTitle("Information about " + tableContinueWatching.getSelectionModel().getSelectedItem().getName());
                primaryStage.getIcons().add(img);
                primaryStage.setScene(new Scene(root));
                primaryStage.centerOnScreen();
                primaryStage.setResizable(false);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                //when switching the scene MenuBar is null
            }
        } else {
            PopUp.error("Select a series you want to get information about!");
        }
    }

    public void startSeries() {
        //set state of series to 1 ("watching")
        if (tableStartWatching.getSelectionModel().getSelectedItem() != null) {
            List<MySeries> allSeries = MySeries.readData();
            for (MySeries allSery : allSeries) {
                if (allSery.equals(tableStartWatching.getSelectionModel().getSelectedItem())) {
                    allSery.setUserState(1);
                    break;
                }
            }

            MySeries.writeData(allSeries);
            initialize();
        } else {
            PopUp.error("Select a series you want to start!");
        }
    }

    public void displayFinishedSeries() {
        //open popup to show all finished series (state=3)
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/FinishedSeries.fxml"));
            primaryStage.setTitle("All finished Series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            //when switching the scene MenuBar is null
        }
    }

    public void displayAdvancedInformation() {
        //open popup to select a series
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/AdvancedInformationSelector.fxml"));
            primaryStage.setTitle("Advanced Information");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            //when switching the scene MenuBar is null
        }
    }

    public void searchSeries() {
        //open popup to search a series
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/SearchSeries.fxml"));
            primaryStage.setTitle("Search one of your series by attributes");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            //when switching the scene MenuBar is null
        }
    }

    public void switchMode() {
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/MainMovie.fxml"));
            primaryStage.setTitle("Movies Control Panel");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            //when switching the scene MenuBar is null
        }
    }

    public void addSeries() {
        //open popup to add series
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/AddSeries.fxml"));
            primaryStage.setTitle("Add a new series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            //when switching the scene MenuBar is null
        }
    }

    public void deleteSeries() {
        //open popup to delete a series
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/DeleteSeries.fxml"));
            primaryStage.setTitle("Delete Series");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            //when switching the scene MenuBar is null
        }
    }

    public void importBackUp() {
        PopUp popUp = new PopUp();
        popUp.alert("Are you sure you want to load the BackUp?");
        if (popUp.isChoice()) {
            BackUp backup = BackUp.readBackUp();

            if (backup != null) {
                List<MySeries> series = backup.getSeries();
                MySeries.writeData(series);
            }
        }

        initialize();
    }

    public void menuUpdateAll() {
        startUpdate();
        update("Continuing");
        initialize();
    }

    public void menuUpdateEnded() {
        startUpdate();
        update("Ended");
        initialize();
    }

    private void update(String mode) {
        List<MySeries> allSeries = MySeries.readData();
        List<MySeries> updatedAllSeries = new ArrayList<>();
        updating = true;

        final Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (MySeries series : allSeries) {
                    if (series.getStatus().equals(mode)) {
                        MySeries updatedSeries = TVDB_Data.getUpdate(series.getTvdbID(), series.getUserState(), series.getCurrentSeason(), series.getCurrentEpisode());
                        Episode.sort(updatedSeries.getEpisodes());

                        updatedSeries.setCurrent(series.getCurrent());
                        updatedAllSeries.add(updatedSeries);
                        updateProgress(updatedAllSeries.size(), allSeries.size());
                    } else {
                        updatedAllSeries.add(series);
                    }
                }

                MySeries.writeData(updatedAllSeries);
                finishedUpdate();
                updating = false;
                return null;
            }
        };

        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().bind(task.progressProperty());

        final Thread thread = new Thread(task, "task-thread");
        thread.setDaemon(true);
        thread.start();
    }

    private void startUpdate() {
        progressIndicator.setVisible(true);
        buttonIncEpisode.setDisable(true);
        buttonDecEpisode.setDisable(true);
        buttonStartedSeries.setDisable(true);
        infoButton.setDisable(true);
        buttonFinishedSeries.setDisable(true);
        menuBar.setDisable(true);
    }

    private void finishedUpdate() {
        progressIndicator.setVisible(false);
        buttonIncEpisode.setDisable(false);
        buttonDecEpisode.setDisable(false);
        buttonStartedSeries.setDisable(false);
        infoButton.setDisable(false);
        buttonFinishedSeries.setDisable(false);
        menuBar.setDisable(false);
    }

    public void showSettings() {
        //open settings
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/Settings.fxml"));
            primaryStage.setTitle("Settings");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException n) {
            //when switching the scene MenuBar is null
        }
    }

    public void showHowTo() {
        String url = "https://github.com/leonfrisch/SeriesTracker/blob/master/README.md";
        String os = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();

        try {
            if (os.contains("win")) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                runtime.exec("open " + url);
            } else if (os.contains("nux") || os.contains("nix")) {
                String[] browsers = {"firefox", "google-chrome", "chromium-browser", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[]{browser, url});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }
}
