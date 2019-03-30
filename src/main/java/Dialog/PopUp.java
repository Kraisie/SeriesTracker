package Dialog;

import SceneController.MainSeriesController;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class PopUp {

	private URL resource = getClass().getResource("/Pics/Icon/series.png");

	public void showAlert(String header, String message, boolean contentBox) {
		Alert alert = new Alert(AlertType.INFORMATION);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Information");
		alert.setHeaderText(header);

		if (contentBox) {
			showContentBox(alert, message);
		} else {
			alert.setContentText(message);
		}

		alert.showAndWait();
	}

	public void showWarning(String header, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Warning!");
		alert.setHeaderText(header);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public void showError(String header, String message, boolean contentBox) {
		Alert alert = new Alert(AlertType.ERROR);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Error!");
		alert.setHeaderText(header);

		if (contentBox) {
			showContentBox(alert, message);
		} else {
			alert.setContentText(message);
		}

		alert.showAndWait();
	}

	public boolean showChoice(String header, String message) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Confirmation");
		alert.setHeaderText(header);
		alert.setContentText(message);

		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		Optional<ButtonType> option = alert.showAndWait();

		return option.filter(buttonType -> buttonType == ButtonType.YES).isPresent();
	}

	public void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("About");
		alert.setHeaderText(null);

		URL classResource = MainSeriesController.class.getResource("MainSeriesController.class");
		if (classResource.toString().startsWith("jar")) {
			// started from (release) jar
			String name = getClass().getPackage().getImplementationTitle();
			String version = getClass().getPackage().getImplementationVersion();

			VBox dialogPaneContent = new VBox();
			Label information = new Label("\n" + name + " - " + version + "\n\tby Kraisie");
			Label github = new Label("\n\nGithub: ");
			Hyperlink linkGithub = new Hyperlink("https://github.com/Kraisie");
			Label telegram = new Label("\nTelegram: ");
			Hyperlink linkTelegram = new Hyperlink("@Kraisie");
			Font labelFont = new Font(16.0);
			Font linkFont = new Font(16.0);
			information.setFont(labelFont);
			github.setFont(labelFont);
			telegram.setFont(labelFont);
			linkGithub.setFont(linkFont);
			linkTelegram.setFont(linkFont);

			dialogPaneContent.getChildren().addAll(information, github, linkGithub, telegram, linkTelegram);
			alert.getDialogPane().setContent(dialogPaneContent);

			linkGithub.setOnAction((event -> BrowserControl.openBrowser("https://github.com/Kraisie")));
			linkTelegram.setOnAction((event -> BrowserControl.openBrowser("https://t.me/Kraisie")));
		} else {
			// started from IDE or used ./gradlew run etc.
			alert.setContentText("Developer edition - No information available!");
		}

		alert.showAndWait();
	}

	private void showContentBox(Alert alert, String message) {
		VBox dialogPaneContent = new VBox();
		TextArea textArea = new TextArea();
		textArea.setText(message);
		dialogPaneContent.getChildren().addAll(textArea);
		alert.getDialogPane().setContent(dialogPaneContent);
	}


}
