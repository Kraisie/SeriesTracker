package kraisie.data;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import kraisie.dialog.LogUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageCache {

	private final Settings settings;

	public ImageCache(Settings settings) {
		this.settings = settings;
	}

	public Image get(int tvdbId) {
		try {
			Path imagePath = Paths.get(settings.getPathCache().toString(), tvdbId + ".jpg");
			BufferedImage bufImg = ImageIO.read(imagePath.toFile());
			return SwingFXUtils.toFXImage(bufImg, null);
		} catch (IOException e) {
			LogUtil.logError("Could not load cached image!", e);
			return null;
		}
	}

	public boolean isCached(int tvdbId) {
		Path imagePath = Paths.get(settings.getPathCache().toString(), tvdbId + ".jpg");
		return imagePath.toFile().exists();
	}

	private void checkCacheDir() {
		if (cacheDirExists()) {
			return;
		}

		File folder = settings.getPathCache().toFile();
		boolean mkdirs = folder.mkdirs();
		if (!mkdirs) {
			String perms = "[" +
					(folder.canRead() ? "R" : "") +
					(folder.canWrite() ? "W" : "") +
					(folder.canExecute() ? "E" : "") + "]";
			LogUtil.logError("Could not create cache folder! Perms: " + perms + ".");
		}
	}

	private boolean cacheDirExists() {
		File folder = settings.getPathCache().toFile();
		return folder.exists() && folder.canRead() && folder.canWrite();
	}

	public void save(Image img, int tvdbId) {
		LogUtil.logDebug("Trying to cache poster for ID " + tvdbId + ".");
		if (isCached(tvdbId)) {
			LogUtil.logDebug("Poster for ID " + tvdbId + " is already cached.");
			return;
		}

		checkCacheDir();
		writeImg(img, tvdbId);
	}

	private void writeImg(Image img, int tvdbId) {
		try {
			BufferedImage bufImg = transformImage(img);
			File file = new File(settings.getPathCache().toString(), tvdbId + ".jpg");
			boolean success = ImageIO.write(bufImg, "jpg", file);
			if (!success) {
				throw new IOException("Could not save image!");
			}
		} catch (IOException e) {
			LogUtil.logError("Could not save image!", e);
		}
	}

	// ImageIO.write will not find the correct writer for some reason if this isn't done
	private BufferedImage transformImage(Image img) {
		BufferedImage bufImg = SwingFXUtils.fromFXImage(img, null);
		BufferedImage newBufferedImage = new BufferedImage(
				bufImg.getWidth(),
				bufImg.getHeight(),
				BufferedImage.TYPE_INT_RGB
		);
		newBufferedImage.createGraphics().drawImage(bufImg, 0, 0, Color.WHITE, null);
		return newBufferedImage;
	}
}
