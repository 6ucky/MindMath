package com.mocah.mindmath;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.mocah.mindmath.server.ServerApplication;

//Spring Boot Test for a post json file from Cabri
@SpringBootTest(classes=ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
public class PostCabriJsonTest {
	
	@LocalServerPort
	private int port;

	//inject restTemplate bean
	@Autowired
	private TestRestTemplate restTemplate;
	
	private static String jsonfile = "{\r\n" + 
			"    \"id\": \"100\",\r\n" + 
			"    \"sensors\": {\r\n" + 
			"        \"domain\": \"algebre\",\r\n" + 
			"        \"generator\": \"resoudreEquationPremierDegre\",\r\n" + 
			"        \"taskFamily\": \"ft3.1\",\r\n" + 
			"        \"correctAnswer\": true,\r\n" + 
			"        \"codeError\": \"ce_err5\"\r\n" + 
			"    },\r\n" + 
			"    \"log\": [\r\n" + 
			"        {\r\n" + 
			"            \"time\": 4015,\r\n" + 
			"            \"type\": \"tool\",\r\n" + 
			"            \"name\": \"line_tool\",\r\n" + 
			"            \"action\": \"create\"\r\n" + 
			"        },\r\n" + 
			"        {\r\n" + 
			"            \"time\": 5813,\r\n" + 
			"            \"type\": \"button\",\r\n" + 
			"            \"name\": \"bouton-effacer\",\r\n" + 
			"            \"action\": \"push\"\r\n" + 
			"        },\r\n" + 
			"        {\r\n" + 
			"            \"time\": 7689,\r\n" + 
			"            \"type\": \"button\",\r\n" + 
			"            \"name\": \"bouton-valider\",\r\n" + 
			"            \"action\": \"push\"\r\n" + 
			"        }\r\n" + 
			"    ]\r\n" + 
			"}";

	//Expected response feedback
	private static String responsejson = "{\"id\":\"100\",\"idF\":\"F1.1\",\"motivationalElement\":\"Bravo!\",\"solutionModel\":\"mindmath.lip6.fr/videos/ResolutionEquation.mp4\",\"glossary\":\"hypertext\"}";	
	
	@Test
	public void greetingShouldReturnDefaultMessage() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "mocah");
		headers.add("Version-LIP6", "1.0");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> entity = new HttpEntity<>(jsonfile, headers);
		
		ResponseEntity<String> response1 = restTemplate.exchange(
				"http://localhost:" + port + "/task", 
				HttpMethod.POST, 
				entity, 
				String.class);
		
		assertThat(response1.getBody()).contains(responsejson);
	}
}
