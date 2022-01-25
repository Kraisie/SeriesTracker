package kraisie.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kraisie.tvdb.Token;

import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class APIKey {

	private final String apikey;
	private final String userkey;
	private final String username;

	public APIKey(String apikey, String userkey, String username) {
		this.apikey = apikey;
		this.userkey = userkey;
		this.username = username;
	}

	public static APIKey readKey() {
		String json;
		Settings settings = Settings.readData();
		if (settings == null) {
			return new APIKey("", "", "");
		}

		try {
			json = Files.readString(settings.getPathAPIKey());
		} catch (IOException e) {
			return new APIKey("", "", "");
		}

		Gson gson = new Gson();
		return gson.fromJson(json, APIKey.class);
	}

	public static void writeKey(APIKey key) throws IOException {
		// save as pretty json as users might edit this by hand
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(key);
		Settings settings = Settings.readData();
		Files.writeString(settings.getPathAPIKey(), json, TRUNCATE_EXISTING, CREATE);
	}

	public boolean isInvalid() {
		Token token = new Token(getFormattedKey());
		return !token.exists();
	}

	public String getFormattedKey() {
		return "{\"apikey\": \"" + apikey + "\","
				+ "\"userkey\": \"" + userkey + "\","
				+ "\"username\": \"" + username + "\"}";
	}
}
