package SceneController;

import Data.MySeries;
import Dialog.PopUp;
import TVDB.TVDB_Data;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

public class SelectController extends Controller {

	@FXML
	private RadioButton radio1;
	@FXML
	private RadioButton radio2;
	@FXML
	private RadioButton radio3;
	@FXML
	private RadioButton radio4;
	@FXML
	private RadioButton radio5;
	@FXML
	private ToggleGroup series;

	@FXML
	private ImageView image1;
	@FXML
	private ImageView image2;
	@FXML
	private ImageView image3;
	@FXML
	private ImageView image4;
	@FXML
	private ImageView image5;

	@FXML
	private Label labelName1;
	@FXML
	private Label labelName2;
	@FXML
	private Label labelName3;
	@FXML
	private Label labelName4;
	@FXML
	private Label labelName5;
	@FXML
	private Label labelStatus1;
	@FXML
	private Label labelStatus2;
	@FXML
	private Label labelStatus3;
	@FXML
	private Label labelStatus4;
	@FXML
	private Label labelStatus5;

	@FXML
	Button backButton;

	private MySeries selectedSeries;
	private List<MySeries> foundSeries;
	private PopUp popUp = new PopUp();

	void initData(List<MySeries> foundSeries) {
		this.foundSeries = foundSeries;
		ownInitialize();
	}

	@FXML
	private void initialize() {

	}

	@FXML
	private void ownInitialize() {
		enableSelectionSlots();
	}

	/*
	 *	populate fields with possible series
	 */
	private void enableSelectionSlots() {
		URL noImage = MainSeriesController.class.getResource("/Pics/Alert/NoImageAvailable.png");
		Image noImg = new Image(noImage.toString());

		if (foundSeries.size() >= 2) {
			if (!foundSeries.get(0).getBanner().isEmpty()) {
				Image img1 = TVDB_Data.getBannerImage(foundSeries.get(0).getBanner());
				image1.setImage(img1);
			} else {
				image1.setImage(noImg);
				centerImage(image1);
			}

			if (!foundSeries.get(1).getBanner().isEmpty()) {
				Image img2 = TVDB_Data.getBannerImage(foundSeries.get(1).getBanner());
				image2.setImage(img2);
			} else {
				image2.setImage(noImg);
				centerImage(image2);
			}

			labelName1.setText(foundSeries.get(0).getName());
			labelStatus1.setText(foundSeries.get(0).getStatus());

			labelName2.setText(foundSeries.get(1).getName());
			labelStatus2.setText(foundSeries.get(1).getStatus());

			radio3.setDisable(true);
			radio4.setDisable(true);
			radio5.setDisable(true);
		}

		if (foundSeries.size() >= 3) {
			if (!foundSeries.get(2).getBanner().isEmpty()) {
				Image img3 = TVDB_Data.getBannerImage(foundSeries.get(2).getBanner());
				image3.setImage(img3);
			} else {
				image3.setImage(noImg);
				centerImage(image3);
			}

			labelName3.setText(foundSeries.get(2).getName());
			labelStatus3.setText(foundSeries.get(2).getStatus());

			radio3.setDisable(false);
		}

		if (foundSeries.size() >= 4) {
			if (!foundSeries.get(3).getBanner().isEmpty()) {
				Image img4 = TVDB_Data.getBannerImage(foundSeries.get(3).getBanner());
				image4.setImage(img4);
			} else {
				image4.setImage(noImg);
				centerImage(image4);
			}

			labelName4.setText(foundSeries.get(3).getName());
			labelStatus4.setText(foundSeries.get(3).getStatus());

			radio4.setDisable(false);
		}

		if (foundSeries.size() >= 5) {
			if (!foundSeries.get(4).getBanner().isEmpty()) {
				Image img5 = TVDB_Data.getBannerImage(foundSeries.get(4).getBanner());
				image5.setImage(img5);
			} else {
				image5.setImage(noImg);
				centerImage(image5);
			}

			labelName5.setText(foundSeries.get(4).getName());
			labelStatus5.setText(foundSeries.get(4).getStatus());

			radio5.setDisable(false);
		}
	}

	/*
	 *	choose a found series
	 */
	@FXML
	private void select() {
		if (radio1.isSelected()) {
			selectedSeries = foundSeries.get(0);
		} else if (radio2.isSelected()) {
			selectedSeries = foundSeries.get(1);
		} else if (radio3.isSelected()) {
			selectedSeries = foundSeries.get(2);
		} else if (radio4.isSelected()) {
			selectedSeries = foundSeries.get(3);
		} else if (radio5.isSelected()) {
			selectedSeries = foundSeries.get(4);
		} else {
			popUp.showWarning("Select a series", "Select a series you want to add to the tracker.");
			initialize();
		}

		if (selectedSeries != null) {
			List<MySeries> allSeries = MySeries.readData();
			TVDB_Data tvdbAPI = new TVDB_Data();
			MySeries series = tvdbAPI.getUpdate(selectedSeries.getTvdbID(), 0, 1, 1);
			allSeries.add(series);
			MySeries.writeData(allSeries);
			popUp.showAlert("Series added!", "\"" + selectedSeries.getName() + "\" has been added to your list.", false);
		} else {
			popUp.showWarning("Series not found", "Could not find your selected series.");
		}

		backToMain();
	}

	/*
	 *	get back to the main menu
	 */
	@FXML
	private void backToMain() {
		try {
			openScene((Stage) backButton.getScene().getWindow(), "/FXML/MainSeries.fxml", "Series Control Panel");
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true);
		}
	}
}
