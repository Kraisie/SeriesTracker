package Kraisie.SceneController;

import Kraisie.Data.MySeries;
import Kraisie.Dialog.PopUp;
import Kraisie.TVDB.APIKey;
import Kraisie.TVDB.SearchData;
import Kraisie.TVDB.TVDB_Data;
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
	Button backButton;
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
	private Label labelYear1;
	@FXML
	private Label labelYear2;
	@FXML
	private Label labelYear3;
	@FXML
	private Label labelYear4;
	@FXML
	private Label labelYear5;
	@FXML
	private Label labelID1;
	@FXML
	private Label labelID2;
	@FXML
	private Label labelID3;
	@FXML
	private Label labelID4;
	@FXML
	private Label labelID5;
	@FXML
	private Label labelNetwork1;
	@FXML
	private Label labelNetwork2;
	@FXML
	private Label labelNetwork3;
	@FXML
	private Label labelNetwork4;
	@FXML
	private Label labelNetwork5;
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
	private SearchData selectedSeries;
	private List<SearchData> foundSeries;
	private PopUp popUp = new PopUp();

	/**
	 * used to receive data from another controller via Dependency Injection
	 *
	 * @param foundSeries a list of max. 5 series to select from
	 */
	void initData(List<SearchData> foundSeries) {
		this.foundSeries = foundSeries;
		ownInitialize();
	}

	/**
	 * This method is not needed as it would run as soon as the FXMLLoader loads the fxml file
	 * as such the parameters didn't already get passed as the initData can only be called after
	 * we initialized the Controller. Thus we later have to call an own InitializeFunction, but can
	 * also not just remove this function as it initializes all scene content.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * own function to initialize the scene due to a non-usable initialize function
	 */
	@FXML
	private void ownInitialize() {
		enableSelectionSlots();
	}

	/**
	 * populate fields with possible series
	 */
	private void enableSelectionSlots() {
		URL noImage = MainSeriesController.class.getResource("/Pics/Alert/NoImageAvailable.png");
		if(noImage == null) {
			throw new IllegalStateException("Fallback image is null");
		}

		Image noImg = new Image(noImage.toString());
		// replace non existant information
		for (SearchData data : foundSeries) {
			if (data.getFirstAired().isEmpty()) {
				data.setFirstAired("----");
			}

			if (data.getNetwork().isEmpty()) {
				data.setNetwork("Not given!");
			}
		}

		if (foundSeries.size() >= 2) {
			SearchData series1 = foundSeries.get(0);
			setImage(series1, image1, noImg);

			SearchData series2 = foundSeries.get(1);
			setImage(series2, image2, noImg);

			labelName1.setText("Name: " + foundSeries.get(0).getSeriesName());
			labelYear1.setText("Year: " + foundSeries.get(0).getFirstAired().substring(0, 4));
			labelID1.setText("TVDB ID: " + foundSeries.get(0).getId());
			labelNetwork1.setText("Network: " + foundSeries.get(0).getNetwork());
			labelStatus1.setText("Status: " + foundSeries.get(0).getStatus());

			labelName2.setText("Name: " + foundSeries.get(1).getSeriesName());
			labelYear2.setText("Year: " + foundSeries.get(1).getFirstAired().substring(0, 4));
			labelID2.setText("TVDB ID: " + foundSeries.get(1).getId());
			labelNetwork2.setText("Network: " + foundSeries.get(1).getNetwork());
			labelStatus2.setText("Status: " + foundSeries.get(1).getStatus());

			radio3.setDisable(true);
			radio4.setDisable(true);
			radio5.setDisable(true);
		}

		if (foundSeries.size() >= 3) {
			SearchData series3 = foundSeries.get(2);
			setImage(series3, image3, noImg);

			labelName3.setText("Name: " + foundSeries.get(2).getSeriesName());
			labelYear3.setText("Year: " + foundSeries.get(2).getFirstAired().substring(0, 4));
			labelID3.setText("TVDB ID: " + foundSeries.get(2).getId());
			labelNetwork3.setText("Network: " + foundSeries.get(2).getNetwork());
			labelStatus3.setText("Status: " + foundSeries.get(2).getStatus());

			radio3.setDisable(false);
		}

		if (foundSeries.size() >= 4) {
			SearchData series4 = foundSeries.get(3);
			setImage(series4, image4, noImg);

			labelName4.setText("Name: " + foundSeries.get(3).getSeriesName());
			labelYear4.setText("Year: " + foundSeries.get(3).getFirstAired().substring(0, 4));
			labelID4.setText("TVDB ID: " + foundSeries.get(3).getId());
			labelNetwork4.setText("Network: " + foundSeries.get(3).getNetwork());
			labelStatus4.setText("Status: " + foundSeries.get(3).getStatus());

			radio4.setDisable(false);
		}

		if (foundSeries.size() >= 5) {
			SearchData series5 = foundSeries.get(4);
			setImage(series5, image5, noImg);

			labelName5.setText("Name: " + foundSeries.get(4).getSeriesName());
			labelYear5.setText("Year: " + foundSeries.get(4).getFirstAired().substring(0, 4));
			labelID5.setText("TVDB ID: " + foundSeries.get(4).getId());
			labelNetwork5.setText("Network: " + foundSeries.get(4).getNetwork());
			labelStatus5.setText("Status: " + foundSeries.get(4).getStatus());

			radio5.setDisable(false);
		}
	}

	private void setImage(SearchData data, ImageView imageView, Image fallback) {
		if(data == null) {
			return;
		}

		if (data.getBanner() == null) {
			imageView.setImage(fallback);
			centerImage(imageView);
			return;
		}

		if (data.getBanner().isEmpty()) {
			imageView.setImage(fallback);
			centerImage(imageView);
			return;
		}

		Image img = TVDB_Data.getBannerImage(data.getBanner());
		imageView.setImage(img);
	}

	/**
	 * get the series which the user selected and save it
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
			popUp.showWarning("Select a series", "Select a series you want to add to the tracker.", (Stage) radio1.getScene().getWindow());
			initialize();
		}

		if (selectedSeries != null) {
			List<MySeries> allSeries = MySeries.readData();
			if (MySeries.checkDuplicate(allSeries, selectedSeries.getId())) {
				popUp.showAlert("Duplicate found!", "That series is already in your list.", false, (Stage) radio1.getScene().getWindow());
				return;
			}

			TVDB_Data tvdbAPI = new TVDB_Data(APIKey.readKey());
			MySeries series = tvdbAPI.getUpdate(String.valueOf(selectedSeries.getId()), 0, -1, -1);

			try {
				allSeries.add(series);
				MySeries.writeData(allSeries);
			} catch (IOException e) {
				popUp.showError("Failed while saving!", "Trying to save data failed. Please check the validity of you Path.", false, (Stage) radio1.getScene().getWindow());
				return;
			}
			popUp.showAlert("Series added!", "\"" + selectedSeries.getSeriesName() + "\" has been added to your list.", false, (Stage) radio1.getScene().getWindow());
		} else {
			popUp.showWarning("Series not found", "Could not find your selected series.", (Stage) radio1.getScene().getWindow());
		}

		backToMain();
	}

	/**
	 * opens main menu scene
	 *
	 * @see Controller
	 * @see MainSeriesController
	 */
	@FXML
	private void backToMain() {
		try {
			openMain((Stage) backButton.getScene().getWindow());
		} catch (IOException e) {
			popUp.showError("Failed to open the scene!", getStackTrace(e), true, (Stage) backButton.getScene().getWindow());
		}
	}
}
