package kraisie.dialog;

public final class BrowserControl {

	private BrowserControl() {
		throw new AssertionError("Trying to instantiate a utility class.");
	}

	public static void openBrowser(String url) throws Exception {
		String os = System.getProperty("os.name").toLowerCase();

		// Windows
		if (os.contains("win")) {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			return;
		}

		// Linux
		if (os.contains("nux") || os.contains("nix")) {
			openLinuxBrowser(url);
			return;
		}

		// Mac
		if (os.contains("mac")) {
			Runtime.getRuntime().exec("open " + url);
		}
	}

	private static void openLinuxBrowser(String url) throws Exception {
		String[] browsers = {"firefox", "google-chrome", "chromium-browser", "opera", "epiphany", "mozilla", "netscape", "konqueror"};
		String browser = null;
		for (int count = 0; count < browsers.length && browser == null; count++) {
			if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
				browser = browsers[count];
			}
		}

		if (browser == null) {
			throw new Exception("No supported browser found!");
		}

		Runtime.getRuntime().exec(new String[]{browser, url});
	}
}
