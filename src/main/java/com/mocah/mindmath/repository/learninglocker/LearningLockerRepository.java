package com.mocah.mindmath.repository.learninglocker;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mocah.mindmath.repository.XAPIRepository;

@Service
public class LearningLockerRepository extends LearningLockerKeys implements XAPIRepository {
	
	private final RestTemplate restTemp;
	
	public LearningLockerRepository() 
	{
		RestTemplateBuilder builder = new RestTemplateBuilder();
		this.restTemp = builder.build();
	}
	
	public LearningLockerRepository(RestTemplateBuilder builder)
	{
		this.restTemp = builder.build();
	}
	
	public String getfromLearningLocker() throws Exception
	{
		HttpHeaders headers = new HttpHeaders();
		headers.set(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_VALUE);
		headers.set(BASIC_VERSION, BASIC_VERSION_VALUE);
		headers.set(BASIC_TYPE, BASIC_TYPE_VALUE);
		
		HttpEntity entity = new HttpEntity(headers);
//		ResponseEntity<String> response = this.restTemp.exchange(
//				BASIC_URL, 
//				HttpMethod.GET, 
//				entity, 
//				String.class);
		return restTemp.getForObject(BASIC_URL, String.class);
	}
	
}
