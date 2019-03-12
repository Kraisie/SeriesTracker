package TVDB;

import Data.Settings;
import Dialog.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class APIKey {

	private String apikey;
	private String userkey;
	private String username;

	public APIKey() {
		this.apikey = "";
		this.userkey = "";
		this.username = "";
	}

	public APIKey(String apikey, String userkey, String username) {
		this.apikey = apikey;
		this.userkey = userkey;
		this.username = username;
	}

	public static APIKey readKey() {
		String json;
		Settings setting = Settings.readData();

		try {
			json = new String(Files.readAllBytes(setting.getPathAPIKey()));
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("Failed while getting API Key!", "Trying to read the API Key failed. Please check the validity of you Path.", false);
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(json, APIKey.class);
	}

	public static void writeKey(APIKey key) {
		Gson gson = new Gson();
		String json = gson.toJson(key);
		Settings setting = Settings.readData();

		try {
			Files.write(setting.getPathAPIKey(), json.getBytes(), TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("Failed while saving!", "Trying to save the API Key failed. Please check the validity of you Path.", false);
		}
	}

	String getFormattedKey() {
		return "{\"apikey\": \"" + apikey + "\"," +
				"\"userkey\": \"" + userkey + "\"," +
				"\"username\": \"" + username + "\"}";
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getUserkey() {
		return userkey;
	}

	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
