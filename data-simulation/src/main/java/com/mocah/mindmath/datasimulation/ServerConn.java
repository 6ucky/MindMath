/**
 *
 */
package com.mocah.mindmath.datasimulation;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import com.google.common.base.Strings;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class ServerConn {
	private static final String SERVER_URL = "";
	private static final String TEST_SERVER_URL = "http://localhost:8080/";

	private static final String TASK_POST_PATH = "task";
	private static final String QVAL_GET_PATH = "task/qvalues";

	private static final String HEADER_AUTH = "mocah";

	private static HttpRequest createRequest(String body, boolean toProd) {
		String base_uri;
		if (toProd) {
			base_uri = SERVER_URL;
		} else {
			base_uri = TEST_SERVER_URL;
		}

		Builder builder = HttpRequest.newBuilder().timeout(Duration.of(10, ChronoUnit.SECONDS)).header("Authorization",
				HEADER_AUTH);

		URI uri;
		if (!Strings.isNullOrEmpty(body)) {
			uri = URI.create(base_uri + TASK_POST_PATH);
			builder = builder.header("Content-Type", "application/json").POST(BodyPublishers.ofString(body));
		} else {
			uri = URI.create(base_uri + QVAL_GET_PATH);
			builder = builder.GET();
		}

		builder = builder.uri(uri);

		return builder.build();
	}

	private static HttpResponse<String> sendRequest(HttpRequest request) {
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newBuilder().proxy(ProxySelector.getDefault()).build().send(request,
					BodyHandlers.ofString());
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		return response;
	}

	public static String postData(CabriData data) {
		String dataJson = Config.getGson().toJson(data);
		return postData(dataJson);
	}

	public static String postData(String dataJson) {
		HttpRequest request = createRequest(dataJson, Config.USE_PROD_SERV);

		HttpResponse<String> response = sendRequest(request);

		return (response != null) ? response.body() : null;
	}

	public static String getQtable() {
		HttpRequest request = createRequest(null, Config.USE_PROD_SERV);

		HttpResponse<String> response = sendRequest(request);

		return (response != null) ? response.body() : null;
	}
}
