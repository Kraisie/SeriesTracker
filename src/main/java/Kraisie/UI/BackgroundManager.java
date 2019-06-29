package Kraisie.UI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.apache.commons.lang3.ArrayUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.*;

public class BackgroundManager {

	private BufferedImage bufImg;

	public BackgroundManager(BufferedImage bufImg) {
		this.bufImg = bufImg;
	}

	/**
	 * gets a random image from the background folder
	 *
	 * @return BufferedImage from disk or null
	 */
	public static BufferedImage getRandomImage() {
		BufferedImage bufImg = null;
		File backgroundFolder;
		File[] files;

		// create folder if it does not exist
		backgroundFolder = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/");
		boolean createdDir = backgroundFolder.mkdir();

		// check if there are fitting files
		if (createdDir) {
			return null;
		}

		files = backgroundFolder.listFiles((dir, name) -> (
				name.toLowerCase().endsWith(".png") ||
						name.toLowerCase().endsWith(".jpg") ||
						name.toLowerCase().endsWith(".jpeg")
		));

		if (files == null || files.length == 0) {
			return null;
		}

		// select a random image
		try {
			Random random = new Random();
			InputStream is = new FileInputStream(files[random.nextInt(files.length)]);
			bufImg = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bufImg == null) {
			return getFallbackImage();
		}

		return bufImg;
	}

	/**
	 * uses a non local picture as background if there is no local available
	 *
	 * @return a fallback image
	 */
	private static BufferedImage getFallbackImage() {
		BufferedImage bufImg = null;

		try {
			URL url = new URL("https://i.imgur.com/iJYsAF4.jpg");
			bufImg = ImageIO.read(url);
			File file = new File(System.getProperty("user.home"), "/SERIESTRACKER/Backgrounds/fallback.jpg");
			ImageIO.write(bufImg, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bufImg;
	}

	/**
	 * sizes an image as background
	 *
	 * @param w width of the borderPane
	 * @param h height of the borderPane
	 * @return a Background Object with fitting sizes
	 */
	public Background getFittingBackground(int w, int h) {
		BackgroundSize bSize = new BackgroundSize(w, h, false, false, false, false);
		Image img = SwingFXUtils.toFXImage(bufImg, null);

		return new Background(
				new BackgroundImage(
						img,
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						bSize
				)
		);
	}

	/**
	 * gets the contrast color for a label background in hex representation
	 *
	 * @param label the label of which the average background color shall get calculated
	 * @return average background color as hex representation
	 */
	public String getContrastColor(Label label) {
		Color avgBackground = getAverageBackgroundColor(label);
		double hue = getHue(avgBackground);
		Color highestContrast = getHighestContrast(hue);

		return "#" + colorToHex(highestContrast);
	}

	/**
	 * gets the average background color for the space of a label in front of  an image
	 *
	 * @param label the label of which the average background color shall get calculated
	 * @return average background color
	 */
	private Color getAverageBackgroundColor(Label label) {
		List<Color> pixels = new ArrayList<>();

		// get far most left (x) and far most up point (y)
		for (int x = (int) label.getLayoutX(); x < (int) (label.getLayoutX() + label.getWidth()); x++) {
			for (int y = (int) label.getLayoutY(); y < (int) (label.getLayoutY() + label.getHeight()); y++) {
				pixels.add(new Color(bufImg.getRGB(x, y)));
			}
		}

		// get average RGB of the label background
		int avgRed = 0, avgGreen = 0, avgBlue = 0;
		for (Color pixel : pixels) {
			avgRed += pixel.getRed();
			avgGreen += pixel.getGreen();
			avgBlue += pixel.getBlue();
		}

		return new Color(avgRed / pixels.size(), avgGreen / pixels.size(), avgBlue / pixels.size());
	}

	/**
	 * calculates hue for the average background color
	 *
	 * @param color background color
	 * @return hue value
	 */
	private double getHue(Color color) {
		ColorSpace space = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		float[] rgb = {color.getRed(), color.getGreen(), color.getBlue()};
		float[] sRGB = space.fromRGB(rgb);

		// get min and max of sRGB
		List<Float> tempRGBList = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(sRGB)));
		float min = Collections.min(tempRGBList);
		float max = Collections.max(tempRGBList);

		// use hue formula according to max value
		double hue;
		if (max == sRGB[0]) {
			hue = (sRGB[1] - sRGB[2]) / (max - min);
		} else if (max == sRGB[1]) {
			hue = 2 + (sRGB[2] - sRGB[0]) / (max - min);
		} else {
			hue = 4 + (sRGB[0] - sRGB[1]) / (max - min);
		}

		return (60 * hue) % 360;
	}

	/**
	 * selects a color with high contrast to the calculated hue value
	 *
	 * @param hue the hue to get the best contrast color for
	 * @return color with the highest contrast
	 */
	private Color getHighestContrast(double hue) {
		// modified values
		if (hue > 46 && hue <= 90) {
			return Color.yellow;
		} else if (hue > 90 && hue <= 135) {
			return Color.green;
		} else if (hue > 135 && hue <= 225) {
			return Color.cyan;
		} else if (hue > 225 && hue <= 270) {
			return Color.blue;
		} else if (hue > 270 && hue <= 315) {
			return Color.magenta;
		} else {
			return Color.red;
		}
	}

	/**
	 * transforms a color to a hex presentation
	 *
	 * @param color a color :)
	 * @return hex representation as a String of a color
	 */
	private String colorToHex(Color color) {
		String hexColor = Integer.toHexString(color.getRGB() & 0xffffff);
		if (hexColor.length() < 6) {
			hexColor = "000000".substring(0, 6 - hexColor.length()) + hexColor;
		}

		return hexColor;
	}
}
