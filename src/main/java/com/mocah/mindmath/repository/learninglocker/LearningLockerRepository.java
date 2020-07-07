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
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mocah.mindmath.repository.XAPIRepository;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.IStatementObject;
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
	protected final FeedbackforLRS fbLRS;
	protected final boolean isTestEnv;

	public LearningLockerRepository() {
		this(new FeedbackforLRS(), false);
	}

	public LearningLockerRepository(boolean isTestEnv) {
		this(new FeedbackforLRS(), isTestEnv);
	}

	public LearningLockerRepository(FeedbackforLRS fbLRS) {
		this(fbLRS, false);
	}

	public LearningLockerRepository(FeedbackforLRS fbLRS, boolean isTestEnv) {
		if (isTestEnv) {
			this.restTemp = InitializeResetTemplate();
		} else {
			this.restTemp = InitializeResetTemplate();
		}

		if (isTestEnv) {
			this.header_entity = InitializeHeader();
		} else {
			this.header_entity = InitializeTestHeader();
		}

		this.fbLRS = fbLRS;
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
	 *
	 * @return
	 */
	protected abstract HttpHeaders InitializeHeader();

	/**
	 * Add basic authorization, version, content type to header
	 *
	 * @return
	 */
	protected abstract HttpHeaders InitializeTestHeader();

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
}
