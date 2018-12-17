package Controller;

import Code.PopUp;
import Data.MySeries;
import Data.TVDB.TVDB_Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class SelectController {

    @FXML
    public RadioButton radio1;
    @FXML
    public RadioButton radio2;
    @FXML
    public RadioButton radio3;
    @FXML
    public RadioButton radio4;
    @FXML
    public RadioButton radio5;
    @FXML
    public ToggleGroup series;

    @FXML
    public ImageView image1;
    @FXML
    public ImageView image2;
    @FXML
    public ImageView image3;
    @FXML
    public ImageView image4;
    @FXML
    public ImageView image5;

    @FXML
    public Label labelName1;
    @FXML
    public Label labelName2;
    @FXML
    public Label labelName3;
    @FXML
    public Label labelName4;
    @FXML
    public Label labelName5;
    @FXML
    public Label labelStatus1;
    @FXML
    public Label labelStatus2;
    @FXML
    public Label labelStatus3;
    @FXML
    public Label labelStatus4;
    @FXML
    public Label labelStatus5;

    private MySeries selectedSeries;

    public void initialize() {
        URL noImage = MainSeriesController.class.getResource("/resources/Pics/Alert/NoImageAvailable.png");
        Image noImg = new Image(noImage.toString());

        if (AddSeriesController.foundSeries.size() >= 2) {
            if (!AddSeriesController.foundSeries.get(0).getBanner().isEmpty()) {
                Image img1 = TVDB_Data.getBannerImage(AddSeriesController.foundSeries.get(0).getBanner());
                image1.setImage(img1);
            } else {
                image1.setImage(noImg);
                centerImage(image1);
            }

            if (!AddSeriesController.foundSeries.get(1).getBanner().isEmpty()) {
                Image img2 = TVDB_Data.getBannerImage(AddSeriesController.foundSeries.get(1).getBanner());
                image2.setImage(img2);
            } else {
                image2.setImage(noImg);
                centerImage(image2);
            }

            labelName1.setText(AddSeriesController.foundSeries.get(0).getName());
            labelStatus1.setText(AddSeriesController.foundSeries.get(0).getStatus());

            labelName2.setText(AddSeriesController.foundSeries.get(1).getName());
            labelStatus2.setText(AddSeriesController.foundSeries.get(1).getStatus());

            radio3.setDisable(true);
            radio4.setDisable(true);
            radio5.setDisable(true);
        }

        if (AddSeriesController.foundSeries.size() >= 3) {
            if (!AddSeriesController.foundSeries.get(2).getBanner().isEmpty()) {
                Image img3 = TVDB_Data.getBannerImage(AddSeriesController.foundSeries.get(2).getBanner());
                image3.setImage(img3);
            } else {
                image3.setImage(noImg);
                centerImage(image3);
            }

            labelName3.setText(AddSeriesController.foundSeries.get(2).getName());
            labelStatus3.setText(AddSeriesController.foundSeries.get(2).getStatus());

            radio3.setDisable(false);
        }

        if (AddSeriesController.foundSeries.size() >= 4) {
            if (!AddSeriesController.foundSeries.get(3).getBanner().isEmpty()) {
                Image img4 = TVDB_Data.getBannerImage(AddSeriesController.foundSeries.get(3).getBanner());
                image4.setImage(img4);
            } else {
                image4.setImage(noImg);
                centerImage(image4);
            }

            labelName4.setText(AddSeriesController.foundSeries.get(3).getName());
            labelStatus4.setText(AddSeriesController.foundSeries.get(3).getStatus());

            radio4.setDisable(false);
        }

        if (AddSeriesController.foundSeries.size() >= 5) {
            if (!AddSeriesController.foundSeries.get(4).getBanner().isEmpty()) {
                Image img5 = TVDB_Data.getBannerImage(AddSeriesController.foundSeries.get(4).getBanner());
                image5.setImage(img5);
            } else {
                image5.setImage(noImg);
                centerImage(image5);
            }

            labelName5.setText(AddSeriesController.foundSeries.get(4).getName());
            labelStatus5.setText(AddSeriesController.foundSeries.get(4).getStatus());

            radio5.setDisable(false);
        }
    }

    public void select() {
        if (radio1.isSelected()) {
            selectedSeries = AddSeriesController.foundSeries.get(0);
        } else if (radio2.isSelected()) {
            selectedSeries = AddSeriesController.foundSeries.get(1);
        } else if (radio3.isSelected()) {
            selectedSeries = AddSeriesController.foundSeries.get(2);
        } else if (radio4.isSelected()) {
            selectedSeries = AddSeriesController.foundSeries.get(3);
        } else if (radio5.isSelected()) {
            selectedSeries = AddSeriesController.foundSeries.get(4);
        } else {
            PopUp.error("Select a series to add!");
            initialize();
        }

        if (selectedSeries != null) {
            List<MySeries> allSeries = MySeries.readData();
            MySeries series = TVDB_Data.getUpdate(selectedSeries.getTvdbID(), 0, 1, 1);
            allSeries.add(series);
            MySeries.writeData(allSeries);
            PopUp.show(selectedSeries.getName() + " added.");
        } else {
            PopUp.error("Could not find your series!");
        }

        backToMain();
    }

    private void centerImage(ImageView imageView) {
        Image img = imageView.getImage();

        if (img != null) {
            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if (ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            double width = img.getWidth() * reducCoeff;
            double height = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - width) / 2);
            imageView.setY((imageView.getFitHeight() - height) / 2);
        }
    }

    public void backToMain() {
        try {
            Stage primaryStage = (Stage) labelStatus1.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/FXML/MainSeries.fxml")));
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
