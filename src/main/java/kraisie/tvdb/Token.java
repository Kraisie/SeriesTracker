package kraisie.tvdb;

import com.google.gson.Gson;

public class Token {

	private final String token;

	public Token(String credentials) {
		this.token = login(credentials);
	}

	private String login(String credentials) {
		String answer = ApiRequest.post(credentials);
		return extractToken(answer);
	}

	private String extractToken(String answer) {
		if (answer.contains("Error")) {
			return "";
		}

		Gson gson = new Gson();
		Token t = gson.fromJson(answer, Token.class);
		return t.getToken();
	}

	public String getToken() {
		return token;
	}

	public boolean exists() {
		if (token == null) {
			return false;
		}

		return !token.isEmpty();
	}
}
