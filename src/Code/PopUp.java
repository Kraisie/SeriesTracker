package Code;

import Controller.MainSeriesController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;

public class PopUp {

    private static boolean choice;

    public static void show(String text) {
        Stage popUp = new Stage();
        URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/ok.png");
        Image img = new Image(resource.toString());

        popUp.getIcons().add(img);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setWidth(350);
        popUp.setHeight(150);
        popUp.setResizable(false);

        Label label = new Label(text);

        Button close = new Button("Okay");
        close.setOnAction(e -> popUp.close());
        close.setPadding(new Insets(5, 5, 5, 5));

        close.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                popUp.close();
            }
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, close);

        popUp.setScene(new Scene(layout));
        popUp.showAndWait();
    }

    public static void error(String text) {
        URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/Warning.png");
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
        close.setOnAction(e -> popUp.close());
        close.setPadding(new Insets(5, 5, 5, 5));

        close.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                popUp.close();
            }
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, close);

        popUp.setScene(new Scene(layout));
        popUp.showAndWait();
    }

    public static void alert(String text) {
        Stage popUp = new Stage();
        URL resource = MainSeriesController.class.getResource("/resources/Pics/Icon/ok.png");
        Image img = new Image(resource.toString());

        popUp.getIcons().add(img);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setWidth(350);
        popUp.setHeight(150);
        popUp.setResizable(false);

        Label label = new Label(text);

        Button accept = new Button("Yes");
        Button deny = new Button("No");

        accept.setOnAction((ActionEvent e) -> {
            popUp.close();
            choiceAlert(true);
        });
        accept.setPadding(new Insets(5, 5, 5, 5));
        accept.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                popUp.close();
            }
        });

        deny.setOnAction(e -> popUp.close());
        deny.setPadding(new Insets(5, 5, 5, 5));
        deny.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                popUp.close();
                choiceAlert(false);
            }
        });

        VBox layout = new VBox(15);
        HBox choices = new HBox(15);
        choices.getChildren().addAll(accept, deny);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, choices);

        popUp.setScene(new Scene(layout));
        popUp.showAndWait();
    }

    private static void choiceAlert(boolean answer) {
        choice = answer;
    }

    public static boolean isChoice() {
        return choice;
    }
}
