package Dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class PopUp {

	public void showAlert(String header, String message, boolean contentBox) {
		Alert alert = new Alert(AlertType.INFORMATION);

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

		alert.setTitle("Warning!");
		alert.setHeaderText(header);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public void showError(String header, String message, boolean contentBox) {
		Alert alert = new Alert(AlertType.ERROR);

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

		alert.setTitle("Confirmation");
		alert.setHeaderText(header);
		alert.setContentText(message);

		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		Optional<ButtonType> option = alert.showAndWait();
		return option.get() == ButtonType.YES;

	}

	private void showContentBox(Alert alert, String message) {
		VBox dialogPaneContent = new VBox();
		TextArea textArea = new TextArea();
		textArea.setText(message);
		dialogPaneContent.getChildren().addAll(textArea);
		alert.getDialogPane().setContent(dialogPaneContent);
	}


}
