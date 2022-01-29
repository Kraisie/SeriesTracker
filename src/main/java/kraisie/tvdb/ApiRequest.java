package kraisie.tvdb;

import kraisie.util.LogUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ApiRequest {

	public static String post(String body) {
		String answer = "";
		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			HttpPost request = new HttpPost("https://api.thetvdb.com/login");
			StringEntity params = new StringEntity(body);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);

			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
				answer = reader.readLine();
				reader.close();
			}
		} catch (UnknownHostException e) {
			LogUtil.logError("Unknown host! Could not request API token.", e);
			return "Error";
		} catch (IOException e) {
			LogUtil.logError("Could not request API token!", e);
		}

		return answer;
	}
}
