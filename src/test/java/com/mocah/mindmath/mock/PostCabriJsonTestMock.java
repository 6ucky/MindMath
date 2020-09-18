package com.mocah.mindmath.mock;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;
import com.mocah.mindmath.PostCabriJsonTest;
import com.mocah.mindmath.server.ServerApplication;
import com.mocah.mindmath.server.controller.cabri.Taskcontroller;

//Mockito test inherited from spring boot test
@WebMvcTest(PostCabriJsonTest.class)
@ContextConfiguration(classes={ServerApplication.class})
public class PostCabriJsonTestMock {
	
	//inject a bean from the context
	@Autowired
	private MockMvc mockMvc;

	//add or replace mockbean in the Spring MVC context
	@MockBean
	private Taskcontroller service;
	
	//JSON file from Cabri
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
	public void postCabriShouldReturnFeedbackMessage() throws Exception {
		//check methods were called with given arguments
		service.addtask("mocah", jsonfile);
		verify(service).addtask("mocah", jsonfile);
		
		Gson gson = new Gson();
		//specify how a mock should behave and stub method calls
		when(service.addtask("mocah", jsonfile)).thenReturn(new ResponseEntity<String>(gson.toJson(responsejson), HttpStatus.OK));
		
		HttpHeaders headers = new HttpHeaders();
		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "mocah");
		headers.add("Version-LIP6", "1.0");
		this.mockMvc.perform(post("/task").headers(headers).contentType(MediaType.APPLICATION_JSON).content(jsonfile))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(gson.toJson(responsejson))));
	}
}
