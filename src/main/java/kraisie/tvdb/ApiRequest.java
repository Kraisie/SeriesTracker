package kraisie.tvdb;

import kraisie.util.LogUtil;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class ApiRequest {

	private ApiRequest() {
		throw new AssertionError("Trying to instantiate a utility class.");
	}

	public static String post(String body) {
		HttpPost request = new HttpPost("https://api.thetvdb.com/login");
		StringEntity params = new StringEntity(body);
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			return httpClient.execute(request, response -> {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
				String json = reader.readLine();
				EntityUtils.consume(entity);
				reader.close();
				return json;
			});
		} catch (IOException e) {
			LogUtil.logError("Could not request API token!", e);
			return "Error";
		}
	}
}
