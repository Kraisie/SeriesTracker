package com.Kraisie.Dialog;

public class BrowserControl {

	/**
	 * Opens a browser on various systems
	 *
	 * @param url URL to be shown by the browser as String
	 */
	public static void openBrowser(String url) {
		String os = System.getProperty("os.name").toLowerCase();

		try {
			// Windows
			if (os.contains("win")) {
				Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler" + url);
				return;
			}

			// Linux
			if (os.contains("nux") || os.contains("nix")) {
				String[] browsers = {"firefox", "google-chrome", "chromium-browser", "opera", "epiphany", "mozilla", "netscape", "konqueror"};
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++) {
					if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
						browser = browsers[count];
					}
				}

				if (browser == null) {
					throw new Exception();
				}

				Runtime.getRuntime().exec(new String[]{browser, url});
				return;
			}

			// Mac
			if (os.contains("mac")) {
				Runtime.getRuntime().exec("open " + url);
			}
		} catch (Exception e) {
			PopUp popUp = new PopUp();
			popUp.showWarning("Can not open browser!", "There is no supported browser installed on your machine.");
		}
	}
}
