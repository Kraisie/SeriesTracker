package kraisie.ui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MdParser {

	private String filePath;
	private int listLevel;
	private boolean inCodeBlock;

	public MdParser(String filePath) {
		this.filePath = filePath;
		this.listLevel = -1;
		this.inCodeBlock = false;
	}

	public String toHtml() {
		Path file = getFile();
		List<String> lines = getFileContent(file);
		String parsedLines = parse(lines);

		if (!filePath.endsWith(".html")) {
			saveParsedFile(parsedLines, file.getFileName().toString().replace(".md", ""));
		}

		return parsedLines;
	}

	private Path getFile() {
		String fileName = filePath.substring(0, filePath.lastIndexOf("."));
		String parsedFilePath = System.getProperty("user.home") + "/SERIESTRACKER/" + fileName + ".html";
		Path parsedFile = Paths.get(parsedFilePath);

		if (parsedFile.toFile().exists()) {
			filePath = parsedFilePath;
		}

		return Paths.get(filePath);
	}

	private List<String> getFileContent(Path file) {
		List<String> lines = new ArrayList<>();
		try {
			lines = Files.readAllLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines;
	}

	private String parse(List<String> lines) {
		StringBuilder sb = new StringBuilder(
				"<head></head><body style=\"background-color:rgba(100,100,100,0.9); font-size:125%; font-family:helvetica\">"
		);

		for (String line : lines) {
			if (line.startsWith("#")) {
				line = handleHeader(line);
			}

			if (line.matches(".*\\[[^]]*]\\([^)]*\\).*")) {
				line = handleHyperlink(line);
			}

			// TODO: handle images (![altText](link))

			// TODO: handle bold (**...** / __...__)

			// TODO: handle cursive (*...* / _..._)

			// TODO: handle strike trough (~~...~~)

			// TODO: handle quotes (> ...)

			// TODO: list also possible with -
			if (line.matches("(\\s)*\\*\\s.*")) {
				line = handleList(line);
			} else {
				sb.append(closeList());
			}

			// TODO: handle ordered List (1. ...)

			// TODO: handle Task List (- [ ] ... / - [x] ...)

			if (line.equals("```")) {
				sb.append(handleCodeBlock());
				continue;
			}

			if (line.matches(".*`[^`]+`.*")) {
				line = handleCodeVariable(line);
			}

			// TODO: handle Table (... | ...)
			//					  (----|----)
			//					  (... |... )

			sb.append(line);
			if (inCodeBlock)
				sb.append("\n");
		}

		sb.append(closeList());
		sb.append("</body>");
		return sb.toString();
	}

	private String handleHeader(String line) {
		StringBuilder sb = new StringBuilder();
		int signCounter = countHashtagSign(line);

		if (signCounter > 0) {
			line = line.replaceFirst("#+", "");
			sb.append("<h").append(signCounter).append(" style=\"color:#8ceb21;\">")
					.append(line.replaceFirst(" ", ""))
					.append("</h").append(signCounter).append(">");

			return sb.toString();
		}

		return line;
	}

	private int countHashtagSign(String line) {
		int length = line.length();

		line = line.replaceFirst("#+", "");
		int replacedLength = line.length();

		return length - replacedLength;
	}

	private String handleHyperlink(String line) {
		String mdLinkPattern = "\\[[^]]*\\]\\([^\\)]*\\)";
		int[] pos;

		while (!Arrays.equals((pos = getPatternPos(mdLinkPattern, line)), new int[]{-1, -1})) {
			int startIndex = pos[0];
			int endIndex = pos[1];
			if (startIndex != -1) {
				String mdHyperlink = line.substring(startIndex, endIndex);
				String description = mdHyperlink.substring(mdHyperlink.indexOf("[") + 1, mdHyperlink.lastIndexOf("]"));
				String hyperlink = mdHyperlink.substring(mdHyperlink.indexOf("(") + 1, mdHyperlink.lastIndexOf(")"));

				line = line.substring(0, startIndex) +
						"<a href=\"" + hyperlink + "\">" + description + "</a>" +
						line.substring(endIndex);
			}
		}

		return line;
	}

	private int[] getPatternPos(String regex, String text) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);

		int startIndex = -1;
		int endIndex = -1;
		if (matcher.find()) {
			startIndex = matcher.start();
			endIndex = matcher.end();
		}

		return new int[]{startIndex, endIndex};
	}

	private String handleList(String line) {
		StringBuilder sb = new StringBuilder();
		// calc new level on the leading spaces
		int currentListLevel = countLeadingSpaces(line) / 4;

		// if larger level than before add a new <ul>
		if (currentListLevel > listLevel) {
			sb.append("<ul>");
		}

		// if smaller level than before add </ul> as often as the difference
		if (currentListLevel < listLevel) {
			int difference = listLevel - currentListLevel;
			sb.append("</ul>".repeat(difference));
		}

		// put line in <li>...</li>
		sb.append("<li>").append(line.replaceFirst("\\*\\s", "")).append("</li>");

		listLevel = currentListLevel;
		return sb.toString();
	}

	private int countLeadingSpaces(String line) {
		if (!line.startsWith(" ")) {
			return 0;
		}

		int counter = 0;
		for (char c : line.toCharArray()) {
			if (c == ' ') {
				counter++;
			} else {
				break;
			}
		}

		return counter;
	}

	private String closeList() {
		String close = "";
		if (listLevel >= 0) {
			close = "</ul>".repeat(listLevel + 1);
		}
		listLevel = -1;

		return close;
	}

	private String handleCodeBlock() {
		if (inCodeBlock) {
			// close code tag
			return "</code></pre></div>";
		}

		inCodeBlock = true;
		return "<div style=\"margin-left:5vw; width:65vw; background-color:#323232; color:#96f52b; border-radius:5px\">"
				+ "<pre style=\"padding:5px 25px 5px 25px\">"
				+ "<code>";
	}

	private String handleCodeVariable(String line) {
		String mdCodeVariablePattern = "\\`[^`]*\\`";
		int[] pos;
		while (!Arrays.equals((pos = getPatternPos(mdCodeVariablePattern, line)), new int[]{-1, -1})) {
			int startIndex = pos[0];
			int endIndex = pos[1];
			if (startIndex != -1) {
				line = line.substring(0, startIndex)
						+ "<var style=\"background-color: rgba(100,100,100,0.3); color:#aac900; padding: 2px; border-radius:5px\">"
						+ line.substring(startIndex + 1, endIndex - 1)
						+ "</var>"
						+ line.substring(endIndex);
			}
		}

		return line;
	}

	private void saveParsedFile(String content, String fileName) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "/SERIESTRACKER/" + fileName + ".html"))) {
			bw.write(content);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
