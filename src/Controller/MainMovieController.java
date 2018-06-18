package Controller;

import Code.PopUp;
import Data.MyMovie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MainMovieController {

    @FXML
    MenuBar menuBar;
    @FXML
    public ImageView imageBackground;
    @FXML
    public TableView<MyMovie> tableAlreadySeen;
    @FXML
    public TableColumn<Object, Object> columnSeenName;
    @FXML
    public TableView<MyMovie> tableToWatch;
    @FXML
    public TableColumn<Object, Object> columnListName;

    @FXML
    public Button buttonSeenMovie;

    public void initialize() {
        ObservableList<MyMovie> listEntries = FXCollections.observableArrayList(MyMovie.readData());
        ObservableList<MyMovie> toWatch = FXCollections.observableArrayList();
        ObservableList<MyMovie> watched = FXCollections.observableArrayList();

        if (!listEntries.isEmpty()) {
            for (MyMovie listEntry : listEntries) {
                switch (listEntry.getUserState()) {
                    case 0:
                        toWatch.add(listEntry);
                        break;
                    case 1:
                        watched.add(listEntry);
                        break;
                }
            }
        }

        columnSeenName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnListName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableAlreadySeen.setItems(watched);
        tableToWatch.setItems(toWatch);
    }

    public void seenMovie() {
        //set state of series to 1 ("watching")
        if (tableToWatch.getSelectionModel().getSelectedItem() != null) {
            List<MyMovie> allEntries = MyMovie.readData();
            for (MyMovie entry : allEntries) {
                if (entry.equals(tableToWatch.getSelectionModel().getSelectedItem())) {
                    entry.setUserState(1);
                    break;
                }
            }

            MyMovie.writeData(allEntries);
            initialize();
        } else {
            PopUp.error("Select a series you want to start!");
        }
    }

    public void displayInformation() {
        //There is no information except the name until now
    }

    public void displayAdvancedInformation() {
        //No advanced info existing
    }

    public void switchMode() {
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/MainSeries.fxml")));
            primaryStage.setTitle("Series Control Panel");
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

    public void addMovie() {
        //not nice, not pretty
        try {
            URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/series.png");
            Image img = new Image(resource.toString());

            Stage primaryStage = (Stage) menuBar.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/AddMovie.fxml")));
            primaryStage.setTitle("Add a new movie...");
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

    public void editMovie() {
        //wtf r u going to edit at this state?
    }

    public void deleteMovie() {

    }

    public void close() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }


}
