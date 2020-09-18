package com.mocah.mindmath.server.repository.learninglocker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.jsonparser.JsonParserLRS;
import com.mocah.mindmath.parser.jsonparser.LRSType;
import com.mocah.mindmath.server.entity.task.Log;
import com.mocah.mindmath.server.entity.task.Sensors;
import com.mocah.mindmath.server.repository.XAPIRepository;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.adapters.ActorAdapter;
import gov.adlnet.xapi.model.adapters.StatementObjectAdapter;

/**
 * Use Spring Rest Template to connect to Learning Locker
 *
 * @author Yan Wang
 * @since 09/03/2020
 */
@Service
public abstract class LearningLockerRepository extends LearningLockerKeys implements XAPIRepository {
	protected final RestTemplate restTemp;
	protected final HttpHeaders header_entity;
	protected final boolean isTestEnv;
	protected Gson gson = new Gson();

	public LearningLockerRepository() {
		this(false);
	}

	public LearningLockerRepository(boolean isTestEnv) {
		if (isTestEnv) {
			this.restTemp = InitializeResetTemplate();
		} else {
			this.restTemp = InitializeResetTemplate();
		}

		if (isTestEnv) {
			this.header_entity = InitializeTestHeader();
		} else {
			this.header_entity = InitializeHeader();
		}

		this.isTestEnv = isTestEnv;
	}

	/**
	 * Skip SSL certificate verification while using Spring Rest Template
	 *
	 * @return
	 */
	protected RestTemplate InitializeResetTemplate() {
		// override standard certificate verification process.
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		RestTemplate temp = new RestTemplate(requestFactory);
		return temp;
	}

	/**
	 * Add basic authorization, version, content type to header
	 */
	protected HttpHeaders InitializeHeader() {
		HttpHeaders headers = new HttpHeaders();

		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_VALUE);
		headers.add(BASIC_VERSION, BASIC_VERSION_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	/**
	 * Add basic authorization, version, content type to header
	 */
	protected HttpHeaders InitializeTestHeader() {
		HttpHeaders headers = new HttpHeaders();

		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_TEST_VALUE);
		headers.add(BASIC_VERSION, BASIC_VERSION_TEST_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	/**
	 * add Generic Interface Adapter of Actor class and IStatementObject class for
	 * Gson
	 *
	 * @return
	 */
	protected Gson getDecoder() {
		Gson gson_decoder = new Gson();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Actor.class, new ActorAdapter());
		builder.registerTypeAdapter(IStatementObject.class, new StatementObjectAdapter());
		gson_decoder = builder.create();
		return gson_decoder;
	}

	/**
	 * get about of LRS
	 *
	 * @return message from Learning Locker
	 */
	@Override
	public String getAboutfromLearningLocker() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);
		ResponseEntity<String> response = this.restTemp.exchange(ABOUT_URL, HttpMethod.GET, entity, String.class);
		return response.getBody();
	}

	/**
	 * post a statement to LRS
	 *
	 * @return the id of the statement in LRS
	 */
	@Override
	public String postStatement(Statement statement) {
		// post statement
		HttpEntity<String> entity = new HttpEntity<>(gson.toJson(statement), header_entity);
		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL, HttpMethod.POST, entity, String.class);
		// the id of the current statement
		String response_id = JsonParser.parseString(response.getBody()).getAsString();
		return response_id;
	}

	/**
	 * @deprecated use jxAPI instead
	 */
	@Deprecated
	public String postStatementTEST(String id, Sensors sensors, List<Log> log) {
		// TODO design statement and put fb inside
		String test_text = "{\r\n" + "  \"id\": \"18bac5d4-f6f0-4d9b-9888-ef98891cb117\",\r\n"
				+ "  \"actor\": { \"mbox\": \"mailto:test1@lrsmocah.lip6.fr\" },\r\n"
				+ "  \"verb\": { \"id\": \"http://lrsmocah.lip6.fr/verb\" },\r\n"
				+ "  \"object\": { \"id\": \"http://lrsmocah.lip6.fr/activity\" }\r\n" + "}";

		// post test text to LRS and return its id
		HttpEntity<String> entity = new HttpEntity<>(test_text, header_entity);
		ResponseEntity<String> response1 = this.restTemp.exchange(STATEMENT_URL, HttpMethod.POST, entity, String.class);
		// the id of the current statement
		String response1_id = JsonParser.parseString(response1.getBody()).getAsString();

		// return statement based on id
		ResponseEntity<String> response2 = this.restTemp.exchange(
				STATEMENT_URL + "?statementId=" + response1_id + "&format=exact&attachments=false", HttpMethod.GET,
				entity, String.class);
		JsonParserLRS parser = new JsonParserLRS(response2.getBody(), LRSType.POST);
		return parser.getStatement();
	}

}
