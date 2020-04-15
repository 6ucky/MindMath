package com.mocah.mindmath.repository.learninglocker;

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
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.jsonparser.JsonParserLRS;
import com.mocah.mindmath.parser.jsonparser.LRSType;
import com.mocah.mindmath.repository.XAPIRepository;
import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

/**
 * Use Spring Rest Template to connect to Learning Locker
 * @author	Yan Wang
 * @since	09/03/2020
 */
@Service
public class LearningLockerRepository extends LearningLockerKeys implements XAPIRepository {
	
	private final RestTemplate restTemp;
	private final HttpHeaders header_entity;
	private FeedbackforLRS fbLRS;
	
	public LearningLockerRepository()
	{
		this.restTemp = InitializeResetTemplate();
		this.header_entity = InitializeHeader();
		this.fbLRS = new FeedbackforLRS();
	}
	
	public LearningLockerRepository(FeedbackforLRS fbLRS)
	{
		this();
		this.fbLRS = fbLRS;
	}
	
	//skip SSL certificate verification while using Spring Rest Template
	private static RestTemplate InitializeResetTemplate()
	{
		//override standard certificate verification process.
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
				 
		SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy)
					.build();
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
				 
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(csf)
				.build();
				 
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
				 
		requestFactory.setHttpClient(httpClient);
		RestTemplate temp = new RestTemplate(requestFactory);
		return temp;
	}
	
	// add basic authorization, version, content type to header
	private static HttpHeaders InitializeHeader()
	{
		HttpHeaders headers = new HttpHeaders();
		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_VALUE);
		headers.add(BASIC_VERSION, BASIC_VERSION_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		return headers;
	}
	
	/**
	 * get about of LRS
	 * @return message from Learning Locker
	 */
	public String getAboutfromLearningLocker()
	{
		HttpEntity<String> entity = new HttpEntity<String>(header_entity);
		ResponseEntity<String> response = this.restTemp.exchange(
				ABOUT_URL, 
				HttpMethod.GET, 
				entity, 
				String.class);
		return response.getBody();
	}
	
	/**
	 * get all the statements from LRS
	 * @return list of statements
	 */
	public String getAllStatementfromLearningLocker()
	{
		HttpEntity<String> entity = new HttpEntity<String>(header_entity);
		ResponseEntity<String> response = this.restTemp.exchange(
				STATEMENT_URL, 
				HttpMethod.GET, 
				entity, 
				String.class);
		JsonParserLRS parser = new JsonParserLRS(response.getBody(),LRSType.RESPONSE);
		return parser.getStatement();
	}
	
	public String postStatementTEST (String id, Sensors sensors, List<Log> log) {
		// TODO design statement and put fb inside
		FeedbackforLRS fbLRS = new FeedbackforLRS(id, sensors, log);
		Gson gson = new Gson();
		String test_text = "{\r\n" + 
				"  \"id\": \"18bac5d4-f6f0-4d9b-9888-ef98891cb117\",\r\n" + 
				"  \"actor\": { \"mbox\": \"mailto:test1@lrsmocah.lip6.fr\" },\r\n" + 
				"  \"verb\": { \"id\": \"http://lrsmocah.lip6.fr/verb\" },\r\n" + 
				"  \"object\": { \"id\": \"http://lrsmocah.lip6.fr/activity\" }\r\n" + 
				"}";
		
		// post test text to LRS and return its id
		HttpEntity<String> entity = new HttpEntity<>(test_text, header_entity);
		ResponseEntity<String> response1 = this.restTemp.exchange(
				STATEMENT_URL, 
				HttpMethod.POST, 
				entity, 
				String.class);
		// the id of the current statement
		String response1_id = JsonParser.parseString(response1.getBody()).getAsString();
		
		// return statement based on id
		ResponseEntity<String> response2 = this.restTemp.exchange(
				STATEMENT_URL + "?statementId=" + response1_id + "&format=exact&attachments=false", 
				HttpMethod.GET, 
				entity, 
				String.class);
		JsonParserLRS parser = new JsonParserLRS(response2.getBody(),LRSType.POST);
		return parser.getStatement();
	}
	
}
