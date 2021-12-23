package kraisie.dialog;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

public class PopUp {

	private final Stage baseStage;

	private final URL ICON_RESOURCE = getClass().getResource("/Pics/Icon/series.png");
	private final URL CSS_RESOURCE = getClass().getResource("/css/myStyle.css");

	private PopUp(Stage baseStage) {
		this.baseStage = baseStage;
	}

	public static PopUp forStage(Stage stage) {
		return new PopUp(stage);
	}

	public void showAlert(String header, String message, boolean contentBox) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		addIcon(alertStage);
		alert.setTitle("Information");
		setProperties(alert, contentBox, header, message);
		Platform.runLater(() -> setCoordinates(alert, baseStage));
		alert.showAndWait();
	}

	private void addIcon(Stage alertStage) {
		if (ICON_RESOURCE == null) {
			return;
		}

		Image img = new Image(ICON_RESOURCE.toString());
		alertStage.getIcons().add(img);
	}

	public void showWarning(String header, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		addIcon(alertStage);
		alert.setTitle("Warning!");
		setProperties(alert, false, header, message);
		Platform.runLater(() -> setCoordinates(alert, baseStage));
		alert.showAndWait();
	}

	public void showError(String header, String message, boolean contentBox) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		addIcon(alertStage);
		alert.setTitle("Error!");
		setProperties(alert, contentBox, header, message);
		Platform.runLater(() -> setCoordinates(alert, baseStage));
		alert.showAndWait();
	}

	public boolean showChoice(String header, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		addIcon(alertStage);
		alert.setTitle("Confirmation");
		setProperties(alert, false, header, message);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		Platform.runLater(() -> setCoordinates(alert, baseStage));
		Optional<ButtonType> option = alert.showAndWait();

		return option.filter(buttonType -> buttonType == ButtonType.YES).isPresent();
	}

	private void showContentBox(Alert alert, String message) {
		DialogPane dialogPaneContent = alert.getDialogPane();
		TextArea textArea = new TextArea();
		textArea.setText(message);
		textArea.setEditable(false);
		addCss(alert);
		dialogPaneContent.setContent(textArea);
	}

	private void addCss(Alert alert) {
		if (CSS_RESOURCE == null) {
			return;
		}

		alert.getDialogPane().getStylesheets().add(CSS_RESOURCE.toExternalForm());
	}

	private void setProperties(Alert alert, boolean contentBox, String header, String message) {
		alert.setHeaderText(header);

		if (contentBox) {
			showContentBox(alert, message);
		} else {
			alert.setContentText(message);
		}

		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
		addCss(alert);
	}

	private void setCoordinates(Alert alert, Stage baseStage) {
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		double xPos = baseStage.getX() + baseStage.getWidth() / 2d;
		double yPos = baseStage.getY() + baseStage.getHeight() / 2d;
		alertStage.setX(xPos - alertStage.getWidth() / 2d);
		alertStage.setY(yPos - alertStage.getHeight() / 2d);
	}
}
