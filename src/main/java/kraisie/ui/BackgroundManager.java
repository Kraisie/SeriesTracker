package kraisie.ui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import kraisie.util.RandomUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BackgroundManager {

	private BufferedImage bufImg;

	private static final File BACKGROUND_FOLDER = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/");

	public BackgroundManager() throws IOException {
		this.bufImg = getRandomImage();
	}

	public BackgroundManager(BufferedImage image) {
		this.bufImg = image;
	}

	public Background getBackground(Pane pane) {
		int width = (int) pane.getWidth();
		int height = (int) pane.getHeight();

		return getFittingBackground(width, height);
	}

	private BufferedImage getRandomImage() throws IOException {
		// create folder if it does not exist
		Files.createDirectories(Paths.get(BACKGROUND_FOLDER.getParent()));
		boolean createdDir = BACKGROUND_FOLDER.mkdir();

		// check if there are fitting files
		File[] files = null;
		if (!createdDir) {
			files = getFiles();
		}

		if (files == null || files.length == 0) {
			getFallbackImage();
		}

		// select a random image
		if (bufImg == null && files != null) {
			InputStream is = new FileInputStream(files[RandomUtil.getIntInBound(files.length)]);
			bufImg = ImageIO.read(is);
		}

		if (bufImg == null) {
			throw new IOException("Could not resort to fallback image!");
		}

		return bufImg;
	}

	private File[] getFiles() {
		return BACKGROUND_FOLDER.listFiles((dir, name) -> (
				name.toLowerCase().endsWith(".png") ||
						name.toLowerCase().endsWith(".jpg") ||
						name.toLowerCase().endsWith(".jpeg")
		));
	}

	private void getFallbackImage() throws IOException {
		URL url = BackgroundManager.class.getResource("/Pics/fallback.jpg");
		if (url == null) {
			throw new IllegalStateException("Fallback Picture not available!");
		}

		bufImg = ImageIO.read(url);
		File file = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/fallback.jpg");
		ImageIO.write(bufImg, "jpg", file);
	}

	private Background getFittingBackground(int w, int h) {
		BackgroundSize size = new BackgroundSize(w, h, false, false, false, false);
		Image img = SwingFXUtils.toFXImage(bufImg, null);

		return new Background(
				new BackgroundImage(
						img,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						size
				)
		);
	}

	public boolean hasBackgroundChoice() {
		File[] files = getFiles();
		if (files == null) {
			return false;
		}

		return files.length > 1;
	}


}