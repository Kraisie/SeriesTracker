package Kraisie.Dialog;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
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
	 * @param baseStage  the base stage that called the Alert
	 */
	public void showAlert(String header, String message, boolean contentBox, Stage baseStage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Information");
		setProperties(alert, contentBox, header, message);
		Platform.runLater(() -> setCoordinates(alert, baseStage));
		alert.showAndWait();
	}

	/**
	 * Shows a warning to the user.
	 *
	 * @param header    text for the header of the Alert
	 * @param message   text for the message of the Alert
	 * @param baseStage the base stage that called the Alert
	 */
	public void showWarning(String header, String message, Stage baseStage) {
		Alert alert = new Alert(AlertType.ERROR);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Warning!");
		setProperties(alert, false, header, message);
		Platform.runLater(() -> setCoordinates(alert, baseStage));
		alert.showAndWait();
	}

	/**
	 * Alerts a user of an error.
	 * If needed it does provide a TextArea to allow the user to easily copy errors like stack traces.
	 *
	 * @param header     text for the header of the Alert
	 * @param message    text for the message of the Alert
	 * @param contentBox shows message in TextArea if true
	 * @param baseStage  the base stage that called the Alert
	 */
	public void showError(String header, String message, boolean contentBox, Stage baseStage) {
		Alert alert = new Alert(AlertType.ERROR);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Error!");
		setProperties(alert, contentBox, header, message);
		Platform.runLater(() -> setCoordinates(alert, baseStage));
		alert.showAndWait();
	}

	/**
	 * Alerts the user of a choice he/she has to decide on.
	 *
	 * @param header    text for the header of the Alert
	 * @param message   text for the message of the Alert
	 * @param baseStage the base stage that called the Alert
	 * @return true if user selects 'Yes' Button
	 */
	public boolean showChoice(String header, String message, Stage baseStage) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("Confirmation");
		setProperties(alert, false, header, message);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		Platform.runLater(() -> setCoordinates(alert, baseStage));
		Optional<ButtonType> option = alert.showAndWait();

		return option.filter(buttonType -> buttonType == ButtonType.YES).isPresent();
	}

	/**
	 * Shows the about screen for this piece of software
	 *
	 * @param baseStage the base stage that called the Alert
	 */
	public void showAbout(Stage baseStage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		Image img = new Image(resource.toString());
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(img);
		alert.setTitle("About");
		alert.setHeaderText(null);

		DialogPane dialogPaneContent = alert.getDialogPane();
		VBox vbox = new VBox();
		Label information = new Label("\nSERIESTRACKER - 2.2\n\tby Kraisie");
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
		dialogPaneContent.getStylesheets().add(getClass().getResource("/css/myStyle.css").toExternalForm());
		dialogPaneContent.setContent(vbox);

		linkGithub.setOnAction((event -> {
			try {
				BrowserControl.openBrowser("https://github.com/Kraisie");
			} catch (Exception e) {
				showWarning("Can not open browser!", "There is no supported browser installed on your machine.", (Stage) dialogPaneContent.getScene().getWindow());
			}
		}));

		linkTelegram.setOnAction((event -> {
			try {
				BrowserControl.openBrowser("https://t.me/Kraisie");
			} catch (Exception e) {
				showWarning("Can not open browser!", "There is no supported browser installed on your machine.", (Stage) dialogPaneContent.getScene().getWindow());
			}
		}));

		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/myStyle.css").toExternalForm());

		Platform.runLater(() -> setCoordinates(alert, baseStage));
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

		alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/myStyle.css").toExternalForm());
		dialogPaneContent.setContent(textArea);
	}

	/**
	 * Sets all needed text and size properties
	 *
	 * @param alert      the Alert that is going to be shown to the user
	 * @param contentBox shows message in TextArea if true
	 * @param header     text for the header of the Alert
	 * @param message    the message which should appear
	 */
	private void setProperties(Alert alert, boolean contentBox, String header, String message) {
		alert.setHeaderText(header);

		if (contentBox) {
			showContentBox(alert, message);
		} else {
			alert.setContentText(message);
		}

		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/myStyle.css").toExternalForm());
	}

	/**
	 * Sets the coordinates of the alert to the center of the base stage
	 *
	 * @param alert     the Alert that is going to be shown to the user
	 * @param baseStage the base stage that called the Alert
	 */
	private void setCoordinates(Alert alert, Stage baseStage) {
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		double xPos = baseStage.getX() + baseStage.getWidth() / 2d;
		double yPos = baseStage.getY() + baseStage.getHeight() / 2d;
		alertStage.setX(xPos - alertStage.getWidth() / 2d);
		alertStage.setY(yPos - alertStage.getHeight() / 2d);
	}
}
