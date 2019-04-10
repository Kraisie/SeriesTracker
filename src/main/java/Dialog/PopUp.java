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

	/**
	 * Shows an alert dedicated to inform the user of something.
	 * If needed it does provide a TextArea to allow the user to easily copy alert messages.
	 *
	 * @param header     text for the header of the Alert
	 * @param message    text for the message of the Alert
	 * @param contentBox shows message in TextArea if true
	 */
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

	/**
	 * Shows a warning to the user.
	 *
	 * @param header  text for the header of the Alert
	 * @param message text for the message of the Alert
	 */
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

	/**
	 * Alerts a user of an error.
	 * If needed it does provide a TextArea to allow the user to easily copy errors like stack traces.
	 *
	 * @param header     text for the header of the Alert
	 * @param message    text for the message of the Alert
	 * @param contentBox shows message in TextArea if true
	 */
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

	/**
	 * Alerts the user of a choice he/she has to decide on.
	 *
	 * @param header  text for the header of the Alert
	 * @param message text for the message of the Alert
	 * @return true if user selects 'Yes' Button
	 */
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

	/**
	 * Shows the about screen for this piece of software
	 */
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

			DialogPane dialogPaneContent = alert.getDialogPane();
			VBox vbox = new VBox();
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

			information.setId("normalLabel");
			github.setId("normalLabel");
			telegram.setId("normalLabel");
			vbox.getChildren().addAll(information, github, linkGithub, telegram, linkTelegram);
			dialogPaneContent.getStylesheets().add("/css/myStyle.css");
			dialogPaneContent.setContent(vbox);

			linkGithub.setOnAction((event -> BrowserControl.openBrowser("https://github.com/Kraisie")));
			linkTelegram.setOnAction((event -> BrowserControl.openBrowser("https://t.me/Kraisie")));
		} else {
			// started from IDE or used ./gradlew run etc.
			alert.getDialogPane().getStylesheets().add("/css/myStyle.css");
			alert.setContentText("Developer edition - No information available!");
		}

		alert.showAndWait();
	}

	/**
	 * Configures the TextArea if needed
	 *
	 * @param alert   the Alert that is going to be shown to the user
	 * @param message the message which should appear in the TextArea
	 */
	private void showContentBox(Alert alert, String message) {
		DialogPane dialogPaneContent = alert.getDialogPane();
		TextArea textArea = new TextArea();
		textArea.setText(message);
		textArea.setEditable(false);

		dialogPaneContent.getStylesheets().add("/css/myStyle.css");
		dialogPaneContent.setContent(textArea);
	}


}
