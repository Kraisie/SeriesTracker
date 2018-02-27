package Code;

import Controller.MainMenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class PopUp {

    public void show(String text) {
        Stage popUp = new Stage();
        URL resource = MainMenuController.class.getResource("/resources/Warning.png");
        Image img = new Image(resource.toString());

        popUp.getIcons().add(img);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setWidth(350);
        popUp.setHeight(150);
        popUp.setResizable(false);

        Label label = new Label(text);

        Button close = new Button("Okay");
        close.setOnAction(e -> {
            popUp.close();
        });
        close.setPadding(new Insets(5, 5, 5, 5));

        close.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER){
                popUp.close();
            }
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, close);

        popUp.setScene(new Scene(layout));
        popUp.showAndWait();
    }

    public void error(String text){
        URL resource = MainMenuController.class.getResource("/resources/Pics/Warning.png");
        Image img = new Image(resource.toString());

        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("ERROR!");
        popUp.getIcons().add(img);
        popUp.setWidth(350);
        popUp.setHeight(150);
        popUp.setResizable(false);

        Label label = new Label(text);

        Button close = new Button("Okay");
        close.setOnAction(e -> {
            popUp.close();
        });
        close.setPadding(new Insets(5, 5, 5, 5));

        close.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER){
                popUp.close();
            }
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, close);

        popUp.setScene(new Scene(layout));
        popUp.showAndWait();
    }
}