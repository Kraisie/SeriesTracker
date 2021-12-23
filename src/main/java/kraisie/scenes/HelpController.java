package kraisie.scenes;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import kraisie.dialog.BrowserControl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class HelpController {

	@FXML
	private WebView webView;

	@FXML
	private void initialize() {
		webView.setContextMenuEnabled(false);
		WebEngine webEngine = webView.getEngine();
		webEngine.load("https://github.com/Kraisie/SeriesTracker/blob/master/README.md");
		addHyperLinkListeners(webEngine);
	}

	private void addHyperLinkListeners(WebEngine webEngine) {
		webEngine.getLoadWorker().stateProperty().addListener((obsV, oldV, newV) -> {
			if (newV == Worker.State.SUCCEEDED) {
				EventListener listener = ev -> {
					Element e = (Element) ev.getTarget();
					Node anchor = findParentAnchor(e.getParentNode());
					if (anchor == null) {
						return;
					}

					String url = ((Element) anchor).getAttribute("href");
					ev.preventDefault();
					openLocalBrowser(url);
				};

				addAnchorListeners(webEngine, listener);
			}
		});
	}

	private Node findParentAnchor(Node current) {
		while (current != null) {
			if (current.getNodeName().equalsIgnoreCase("a")) {
				break;
			}

			current = current.getParentNode();
		}

		return current;
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
