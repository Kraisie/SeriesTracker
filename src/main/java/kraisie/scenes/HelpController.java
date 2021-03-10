package kraisie.scenes;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import kraisie.dialog.BrowserControl;
import kraisie.ui.MdParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelpController {

	@FXML
	private WebView webView;

	@FXML
	private void initialize() {
		String content = readHelp();
		webView.setContextMenuEnabled(false);
		WebEngine webEngine = webView.getEngine();
		webEngine.loadContent(content);
		addHyperLinkListeners(webEngine);
	}

	private String readHelp() {
		Path path = Paths.get(System.getProperty("user.home"), "/SERIESTRACKER/README.html");
		if (path.toFile().exists()) {
			return getFileContent(path);
		}

		MdParser parser = new MdParser("README.md");
		return parser.toHtml();
	}

	private String getFileContent(Path path) {
		try {
			return Files.readString(path);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: log error can't read file
		}

		return "<h1>Error reading file content!</h1>";
	}

	private void addHyperLinkListeners(WebEngine webEngine) {
		webEngine.getLoadWorker().stateProperty().addListener((obsV, oldV, newV) -> {
			if (newV == Worker.State.SUCCEEDED) {
				EventListener listener = ev -> {
					String url = ((Element) ev.getTarget()).getAttribute("href");
					ev.preventDefault();
					openLocalBrowser(url);
				};

				addAnchorListeners(webEngine, listener);
			}
		});
	}

	private void addAnchorListeners(WebEngine webEngine, EventListener listener) {
		Document doc = webEngine.getDocument();
		NodeList nodes = doc.getElementsByTagName("a");
		for (int i = 0; i < nodes.getLength(); i++) {
			((EventTarget) nodes.item(i)).addEventListener("click", listener, false);
		}
	}

	private void openLocalBrowser(String url) {
		try {
			BrowserControl.openBrowser(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
