package kraisie.data;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import kraisie.data.definitions.CacheSize;
import kraisie.util.LogUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public class ImageCache {

	private final Settings settings;

	public ImageCache(Settings settings) {
		this.settings = settings;
	}

	public Image get(int tvdbId) {
		if (!settings.shouldCacheBanners()) {
			LogUtil.logDebug("Cache is disabled!");
			return null;
		}

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
		if (!settings.shouldCacheBanners()) {
			LogUtil.logDebug("Cache is disabled!");
			return false;
		}

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
					(folder.canRead() ? "Read," : "") +
					(folder.canWrite() ? "Write," : "") +
					(folder.canExecute() ? "Execute" : "") + "]";
			LogUtil.logError("Could not create cache folder! Perms: " + perms + ".");
			return;
		}

		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			windowsMarkAsHidden(folder);
		}
	}

	private boolean cacheDirExists() {
		File folder = settings.getPathCache().toFile();
		return folder.exists() && folder.canRead() && folder.canWrite();
	}

	private void windowsMarkAsHidden(File cacheFolder) {
		try {
			Files.setAttribute(cacheFolder.toPath(), "dos:hidden", true);
		} catch (IOException e) {
			LogUtil.logError("Could not mark cache folder as hidden!", e);
		}
	}

	public void save(Image img, int tvdbId) {
		if (!settings.shouldCacheBanners()) {
			LogUtil.logDebug("Not saving image due to disabled cache setting.");
			return;
		}

		LogUtil.logDebug("Trying to cache poster for ID " + tvdbId + ".");
		if (isCached(tvdbId)) {
			LogUtil.logDebug("Poster for ID " + tvdbId + " is already cached.");
			return;
		}

		checkCacheDir();
		writeImg(img, tvdbId);
		if (settings.getMaxCacheSize() != CacheSize.UNLIMITED) {
			purgeToMaxCacheSize();
		}
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

	public boolean clear() {
		Path cachePath = settings.getPathCache();
		File[] files = cachePath.toFile().listFiles(pathname -> pathname.getName().endsWith("jpg"));
		if (files == null) {
			LogUtil.logDebug("Could not check files in " + cachePath);
			return false;
		}

		if (files.length == 0) {
			LogUtil.logDebug("There are no cached files to delete in " + cachePath);
			return true;
		}

		long deletedSize = 0;
		int failures = 0;
		boolean success = true;
		for (File file : files) {
			long fileSize = getFileSize(file);
			if (!file.delete()) {
				failures++;
				success = false;
				LogUtil.logWarning("Could not delete " + file.getAbsolutePath());
			} else {
				deletedSize += fileSize;
			}
		}

		LogUtil.logInfo("Deleted " + buildFileSizeText(deletedSize) + " of cached images with " + failures + " failures.");
		return success;
	}

	private long getFileSize(File file) {
		try {
			return Files.size(file.toPath());
		} catch (IOException e) {
			return 0;
		}
	}

	private String buildFileSizeText(long size) {
		if (size >= 1000000000) {
			double gb = (double) size / 1000000000;
			return String.format("%.2f GB", gb);
		}

		if (size >= 1000000) {
			double mb = (double) size / 1000000;
			return String.format("%.2f MB", mb);
		}

		if (size >= 1000) {
			double kb = (double) size / 1000;
			return String.format("%.2f KB", kb);
		}

		return size + " B";
	}

	public String getCurrentSizeText() {
		Path cachePath = settings.getPathCache();
		File[] files = cachePath.toFile().listFiles(pathname -> pathname.getName().endsWith("jpg"));
		if (files == null) {
			LogUtil.logDebug("Could not check files in " + cachePath);
			return "Unknown";
		}

		if (files.length == 0) {
			return "0 B";
		}

		long bytes = Arrays.stream(files)
				.mapToLong(this::getFileSize)
				.sum();

		return buildFileSizeText(bytes);
	}

	private long getCacheSizeInBytes(File[] files) {
		return Arrays.stream(files)
				.mapToLong(this::getFileSize)
				.sum();
	}

	public void purgeToMaxCacheSize() {
		Path cachePath = settings.getPathCache();
		File[] files = cachePath.toFile().listFiles(pathname -> pathname.getName().endsWith("jpg"));
		if (files == null) {
			LogUtil.logDebug("Could not check files in " + cachePath);
			return;
		}

		if (files.length == 0) {
			return;
		}

		long maxSize = settings.getMaxCacheSize().getSizeInBytes();
		if (maxSize <= 0) {
			return;
		}

		long currSize = getCacheSizeInBytes(files);
		if (currSize <= maxSize) {
			return;
		}

		// sort files by last modified timestamp in descending order (-> oldest files get deleted first)
		Arrays.sort(files, Comparator.comparing(File::lastModified));
		int pos = 0;
		while (currSize > maxSize && pos < files.length - 1) {
			File file = files[pos];
			long fileSize = getFileSize(file);
			if (file.delete()) {
				currSize -= fileSize;
			}

			pos++;
		}
	}
}
