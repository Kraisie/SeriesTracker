package Controller;

import Code.PopUp;
import Data.MyMovie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddMovieController {

    @FXML
    public TextField nameMovie;
    @FXML
    public Button buttonBack1;

    public void addMovie() {
        if (!MyMovie.checkDuplicate(MyMovie.readData(), nameMovie.getText())) {
            MyMovie.addData(new MyMovie(nameMovie.getText(), 0));
            PopUp.show("New movie added!");
            back();
        } else {
            PopUp.error("The movie already exists in your list!");
        }
    }

    public void back() {
        try {
            Stage primaryStage = (Stage) buttonBack1.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/FXML/MainMovie.fxml"));
            primaryStage.setTitle("Movie Control Panel");
            primaryStage.setScene(new Scene(root));
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
