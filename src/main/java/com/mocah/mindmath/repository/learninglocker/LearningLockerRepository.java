package com.mocah.mindmath.repository.learninglocker;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.jsonparser.JsonParserLRS;
import com.mocah.mindmath.parser.jsonparser.LRSType;
import com.mocah.mindmath.repository.XAPIRepository;
import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verb;
import gov.adlnet.xapi.model.adapters.ActorAdapter;
import gov.adlnet.xapi.model.adapters.StatementObjectAdapter;

/**
 * Use Spring Rest Template to connect to Learning Locker
 *
 * @author Yan Wang
 * @since 09/03/2020
 */
@Service
public class LearningLockerRepository extends LearningLockerKeys implements XAPIRepository {

	private final RestTemplate restTemp;
	private final HttpHeaders header_entity;
	private FeedbackforLRS fbLRS;

	private HashMap<String, String> filters = null;
	private Gson gson = new Gson();

	public LearningLockerRepository() {
		this.restTemp = InitializeResetTemplate();
		this.header_entity = InitializeHeader();
		this.fbLRS = new FeedbackforLRS();
	}

	public LearningLockerRepository(FeedbackforLRS fbLRS) {
		this();
		this.fbLRS = fbLRS;
	}

	// skip SSL certificate verification while using Spring Rest Template
	private static RestTemplate InitializeResetTemplate() {
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

	// add basic authorization, version, content type to header
	private static HttpHeaders InitializeHeader() {
		HttpHeaders headers = new HttpHeaders();
		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_VALUE);
		headers.add(BASIC_VERSION, BASIC_VERSION_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	// add quary parameters in URL
	private StringBuilder InitializeQuaryURL() {
		StringBuilder query = new StringBuilder();
		if (this.filters != null && !this.filters.isEmpty()) {
			query.append("?");
			for (Entry<String, String> item : this.filters.entrySet()) {
				query.append(item.getKey());
				query.append("=");
				query.append(item.getValue());
				query.append("&");
			}
			query.deleteCharAt(query.length() - 1);
		}
		return query;
	}

	// add Generic Interface Adapter of Actor class and IStatementObject class for
	// Gson
	private Gson getDecoder() {
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

	@Override
	public String getAllStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL, HttpMethod.GET, entity, String.class);
		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getAllStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL, HttpMethod.GET, entity, String.class);
		return getDecoder().fromJson(response.getBody(), StatementResult.class);
	}

	@Override
	public String getFilteredStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL + InitializeQuaryURL().toString(),
				HttpMethod.GET, entity, String.class);

		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getFilteredStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		// set true to URI string
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(STATEMENT_URL + InitializeQuaryURL().toString());
		URI uri = builder.build(true).toUri();
		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		return getDecoder().fromJson(response.getBody(), StatementResult.class);
	}

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

	/**
	 * Add a filter to the statement request
	 *
	 * @param key   key name (ie "context.extensions.yourExtension")
	 * @param value expected value (ie "customExtensionValue")
	 */
	public LearningLockerRepository addFilter(String key, String value) {
		LearningLockerRepository client = new LearningLockerRepository();
		if (client.filters == null) {
			client.filters = new HashMap<>();
		}
		if (this.filters != null) {
			for (Entry<String, String> filter : filters.entrySet()) {
				client.filters.put(filter.getKey(), filter.getValue());
			}
		}
		client.filters.put(key, value);
		return client;
	}

	/**
	 *
	 */
	public void clearFilters() {
		this.filters.clear();
	}

	/**
	 * @param v
	 * @throws UnsupportedEncodingException
	 */
	public LearningLockerRepository filterByVerb(Verb v) throws UnsupportedEncodingException {
		return addFilter("verb", URLEncoder.encode(v.getId(), "UTF-8"));
	}

	/**
	 * @param verbId
	 */
	public LearningLockerRepository filterByVerb(String verbId) {
		return addFilter("verb", verbId);
	}

	/**
	 * @param a
	 * @throws UnsupportedEncodingException
	 */
	public LearningLockerRepository filterByActor(Actor a) throws UnsupportedEncodingException {
		return addFilter("agent", URLEncoder.encode(gson.toJson(a.serialize()), "UTF-8"));
	}

	/**
	 * @param activityId
	 */
	public LearningLockerRepository filterByActivity(String activityId) {
		return addFilter("activity", activityId);
	}

	/**
	 * @param registrationId
	 */
	public LearningLockerRepository filterByRegistration(String registrationId) {
		return addFilter("registration", registrationId);
	}

	/**
	 * @param include
	 */
	public LearningLockerRepository includeRelatedActivities(boolean include) {
		if (include)
			return addFilter("related_activities", "true");
		else
			return addFilter("related_activities", "false");
	}

	/**
	 * @param include
	 */
	public LearningLockerRepository includeRelatedAgents(boolean include) {
		if (include)
			return addFilter("related_agents", "true");
		else
			return addFilter("related_agents", "false");
	}

	/**
	 * @param timestamp
	 */
	public LearningLockerRepository filterBySince(String timestamp) {
		return addFilter("since", timestamp);
	}

	/**
	 * @param timestamp
	 */
	public LearningLockerRepository filterByUntil(String timestamp) {
		return addFilter("until", timestamp);
	}

	/**
	 * @param limit
	 */
	public LearningLockerRepository limitResults(int limit) {
		return addFilter("limit", Integer.toString(limit));
	}

	/**
	 *
	 */
	public LearningLockerRepository exact() {
		return addFilter("format", "exact");
	}

	/**
	 *
	 */
	public LearningLockerRepository ids() {
		return addFilter("format", "ids");
	}

	/**
	 *
	 */
	public LearningLockerRepository canonical() {
		return addFilter("format", "canonical");
	}

	/**
	 * @param include
	 */
	public LearningLockerRepository ascending(boolean include) {
		if (include)
			return addFilter("ascending", "true");
		else
			return addFilter("ascending", "false");
	}

	@Override
	public String postStatement(Statement statement) {
		// post statement
		HttpEntity<String> entity = new HttpEntity<>(gson.toJson(statement), header_entity);
		ResponseEntity<String> response1 = this.restTemp.exchange(STATEMENT_URL, HttpMethod.POST, entity, String.class);
		// the id of the current statement
		String response1_id = JsonParser.parseString(response1.getBody()).getAsString();
		return response1_id;
	}
}
