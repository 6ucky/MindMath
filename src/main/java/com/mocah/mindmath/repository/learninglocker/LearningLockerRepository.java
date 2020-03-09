package com.mocah.mindmath.repository.learninglocker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
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

import com.mocah.mindmath.repository.XAPIRepository;

/**
 * Use Spring Rest Template to connect to Learning Locker
 * @author	Yan Wang
 * @since	09/03/2020
 */
@Service
public class LearningLockerRepository extends LearningLockerKeys implements XAPIRepository {
	
	private final RestTemplate restTemp;
	private final HttpEntity header_entity;
	
	public LearningLockerRepository()
	{
		this.restTemp = InitializeResetTemplate();
		this.header_entity = InitializeHeader();
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
	private static HttpEntity InitializeHeader()
	{
		HttpHeaders headers = new HttpHeaders();
		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_VALUE);
		headers.add(BASIC_VERSION, BASIC_VERSION_VALUE);
		headers.add(BASIC_TYPE, BASIC_TYPE_VALUE);
		HttpEntity temp = new HttpEntity(headers);
		return temp;
	}
	
	/**
	 * send get request to Learning Locker
	 * @return message from Learning Locker
	 */
	public String getfromLearningLocker()
	{
		ResponseEntity<String> response = this.restTemp.exchange(
				BASIC_URL, 
				HttpMethod.GET, 
				header_entity, 
				String.class);
		return response.getBody();
	}
	
	public String posttoLearningLocker()
	{
		// TODO 
		return "Post to Learning Locker finished.";
	}
	
}
