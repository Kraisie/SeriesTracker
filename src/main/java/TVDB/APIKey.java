package TVDB;

import Data.Settings;
import Dialog.PopUp;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

	/**
	 * @param apikey String representation of the API key that can be requested on theTVDB.com
	 * @param userkey String representation of the theTVDB.com user key
	 * @param username the theTVDB.com user name
	 */
	public APIKey(String apikey, String userkey, String username) {
		this.apikey = apikey;
		this.userkey = userkey;
		this.username = username;
	}

	/**
	 * @return API key from the json file specified in the Settings
	 * @see Settings
	 */
	public static APIKey readKey() {
		String json;
		Settings settings = Settings.readData();
		if (settings == null) {
			return null;
		}

		try {
			json = new String(Files.readAllBytes(settings.getPathAPIKey()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}

		Gson gson = new Gson();
		return gson.fromJson(json, APIKey.class);
	}

	/**
	 * @param key API Key to save in a json file
	 */
	public static void writeKey(APIKey key) {
		Gson gson = new Gson();
		String json = gson.toJson(key);
		Settings settings = Settings.readData();

		try {
			Files.write(settings.getPathAPIKey(), json.getBytes(StandardCharsets.UTF_8), TRUNCATE_EXISTING, CREATE);
		} catch (IOException e) {
			PopUp popUp = new PopUp();
			popUp.showError("Failed while saving!", "Trying to save the API Key failed. Please check the validity of you Path.", false);
		}
	}

	/**
	 * @return String that can be used to log in to theTVDB API
	 */
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
