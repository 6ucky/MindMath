package com.mocah.mindmath.repository.learninglocker;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mocah.mindmath.parser.jsonparser.JsonParserLRS;
import com.mocah.mindmath.parser.jsonparser.LRSType;
import com.mocah.mindmath.repository.XAPIRepository;
import com.mocah.mindmath.repository.jxapi.Actor;
import com.mocah.mindmath.repository.jxapi.Statement;
import com.mocah.mindmath.repository.jxapi.StatementResult;
import com.mocah.mindmath.repository.jxapi.Verb;
import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

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
	public String getAllStatementsfromLearningLockerAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL, HttpMethod.GET, entity, String.class);
		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getAllStatementsfromLearningLocker() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL, HttpMethod.GET, entity, String.class);
		return gson.fromJson(response.getBody(), StatementResult.class);
	}
	
	@Override
	public String getFilteredStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);
		
		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL + InitializeQuaryURL().toString(), HttpMethod.GET,
				entity, String.class);

		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getFilteredStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);
		
		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL + InitializeQuaryURL().toString(), HttpMethod.GET,
				entity, String.class);
		return gson.fromJson(response.getBody(), StatementResult.class);
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
	public void addFilter(String key, String value) {
		if (this.filters == null) {
			this.filters = new HashMap<>();
		}

		this.filters.put(key, value);
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
	public void filterByVerb(Verb v) throws UnsupportedEncodingException {
		addFilter("verb", URLEncoder.encode(v.getId(), "UTF-8"));
	}

	/**
	 * @param verbId
	 */
	public void filterByVerb(String verbId) {
		addFilter("verb", verbId);
	}

	/**
	 * @param a
	 * @throws UnsupportedEncodingException
	 */
	public void filterByActor(Actor a) throws UnsupportedEncodingException {
		addFilter("agent", URLEncoder.encode(gson.toJson(a.serialize()), "UTF-8"));
	}

	/**
	 * @param activityId
	 */
	public void filterByActivity(String activityId) {
		addFilter("activity", activityId);
	}

	/**
	 * @param registrationId
	 */
	public void filterByRegistration(String registrationId) {
		addFilter("registration", registrationId);
	}

	/**
	 * @param include
	 */
	public void includeRelatedActivities(boolean include) {
		if (include) {
			addFilter("related_activities", "true");
		} else {
			addFilter("related_activities", "false");
		}
	}

	/**
	 * @param include
	 */
	public void includeRelatedAgents(boolean include) {
		if (include) {
			addFilter("related_agents", "true");
		} else {
			addFilter("related_agents", "false");
		}
	}

	/**
	 * @param timestamp
	 */
	public void filterBySince(String timestamp) {
		addFilter("since", timestamp);
	}

	/**
	 * @param timestamp
	 */
	public void filterByUntil(String timestamp) {
		addFilter("until", timestamp);
	}

	/**
	 * @param limit
	 */
	public void limitResults(int limit) {
		addFilter("limit", Integer.toString(limit));
	}

	/**
	 *
	 */
	public void exact() {
		addFilter("format", "exact");
	}

	/**
	 *
	 */
	public void ids() {
		addFilter("format", "ids");
	}

	/**
	 *
	 */
	public void canonical() {
		addFilter("format", "canonical");
	}

	/**
	 * @param include
	 */
	public void ascending(boolean include) {
		if (include) {
			addFilter("ascending", "true");
		} else {
			addFilter("ascending", "false");
		}
	}

	@Override
	public String postStatement(Statement statement) {
		//post statement
		HttpEntity<String> entity = new HttpEntity<>(gson.toJson(statement), header_entity);
		ResponseEntity<String> response1 = this.restTemp.exchange(STATEMENT_URL, HttpMethod.POST, entity, String.class);
		// the id of the current statement
		String response1_id = JsonParser.parseString(response1.getBody()).getAsString();
		return response1_id;
	}
}
