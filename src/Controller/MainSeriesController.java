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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static Code.Main.checkAirDates;

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
    static MySeries toController;
    private boolean backgroundSet = false;
    private boolean updating = false;

    public void initialize() {
        if (!updating) {
            //Write series to TableView and set background
            if (!backgroundSet) {
                setBackground();
                backgroundSet = true;
            }

            //get Pixels of background on position of TableHeaders, choose background color
            setTextFillInvColor(labelWatching);
            setTextFillInvColor(labelWaiting);
            setTextFillInvColor(labelStarting);

            toController = null;
            ObservableList<MySeries> notStartedSeries = FXCollections.observableArrayList();
            ObservableList<MySeries> watchingSeries = FXCollections.observableArrayList();
            ObservableList<MySeries> waitNewEpisode = FXCollections.observableArrayList();
            List<MySeries> listEntries = MySeries.readData();

            //Fill lists of series according to their Userstate, finished ones get ignored
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
        File[] files = new File[0];
        double random;

        //get all png/jpg files of resource folder
        File folder = null;
        try {
            //TODO: Fix crash when no files in Backgrounds folder
            folder = new File(MainSeriesController.class.getResource("/resources/Pics/Background/").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        if (folder != null) {
            files = folder.listFiles((dir, name) -> (name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")));
        }

        if (files != null) {
            try {
                URL resource;
                random = Math.random() * files.length;
                resource = files[(int) random].toURI().toURL();

                Image img = new Image(resource.toString());
                imageBackground.setImage(img);

                try {
                    InputStream is = new FileInputStream(files[(int) random]);
                    bufImg = ImageIO.read(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setTextFillInvColor(Label label) {
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

        //invert Color and set text fill
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

        //get min and max of sRGB
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

        //use formula according to max value
        if (max == sRGB[0]) {
            hue = (sRGB[1] - sRGB[2]) / (max - min);
        } else if (max == sRGB[1]) {
            hue = 2 + (sRGB[2] - sRGB[0]) / (max - min);
        } else {
            hue = 4 + (sRGB[0] - sRGB[1]) / (max - min);
        }

        hue = (60 * hue) % 360;

        //Select color with highest contrast (values edited for default pictures)
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

        //rgb to hex color
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
                //selected Series found so set current episode to watched
                if (series.equals(tableContinueWatching.getSelectionModel().getSelectedItem())) {
                    series.getCurrent().setWatched(true);

                    if (series.hasNext()) {
                        //if there is an episode after the current one check if it already aired
                        int index = series.getEpisodes().indexOf(series.getCurrent());
                        if (series.getEpisodes().get(index + 1).getFirstAired().equals("Not given!")) {
                            //if next episode does not have an air date it is probably not released yet, so we need to wait for that
                            series.setUserState(2);
                            break;
                        } else {
                            //if there is a date check if it is before today so it aired already. If not we have to wait for the episode to air
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate date = LocalDate.parse(series.getEpisodes().get(index + 1).getFirstAired(), formatter);

                            if (date.isAfter(LocalDate.now())) {
                                series.setUserState(2);
                                break;
                            }
                        }

                        //set current to next episode
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
                    //we can not go below 1.1, because of obvious reasons
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
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/AdvancedInformation.fxml")));
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
            PopUp.error("Select a series you want to get information about!");
        }
    }

    public void startSeries() {
        //set state of series to 1 ("watching")
        if (tableStartWatching.getSelectionModel().getSelectedItem() != null) {
            List<MySeries> allSeries = MySeries.readData();
            for (MySeries series : allSeries) {
                if (series.equals(tableStartWatching.getSelectionModel().getSelectedItem())) {
                    series.setUserState(1);
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/FinishedSeries.fxml")));
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

    public void searchSeries() {
        //open popup to search a series
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/SearchSeries.fxml")));
            primaryStage.setTitle("Search one of your series by attributes");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchMode() {
        //switch to movie mode
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/MainMovie.fxml")));
            primaryStage.setTitle("Movies Control Panel");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSeries() {
        //open popup to add series
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/AddSeries.fxml")));
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

    public void deleteSeries() {
        //open popup to delete a series
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/DeleteSeries.fxml")));
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

    public void createBackUp() {
        BackUp backUp = new BackUp();
        BackUp.writeBackUp(backUp);

        PopUp.show("New BackUp created!");
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
            protected Void call() {
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

                //recheck dates as they may updated
                checkAirDates();

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/Settings.fxml")));
            primaryStage.setTitle("Settings");
            primaryStage.getIcons().add(img);
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHowTo() {
        String url = "https://github.com/Kraisie/SeriesTracker/blob/master/README.md";
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
