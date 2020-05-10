package com.mocah.mindmath.mock;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
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
import com.mocah.mindmath.server.cabri.Taskcontroller;

//mockito test
@WebMvcTest(PostCabriJsonTest.class)
@ContextConfiguration(classes={ServerApplication.class})
public class PostCabriJsonTestMock {
	
	@Autowired
	private MockMvc mockMvc;

	//mockbean test
	@MockBean
	private Taskcontroller service;
	
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
	
	private static String responsejson = "{\"id\":\"100\",\"idF\":\"F1.1\",\"motivationalElement\":\"Bravo!\",\"solutionModel\":\"mindmath.lip6.fr/videos/ResolutionEquation.mp4\",\"glossary\":\"hypertext\"}";
	
	@Test
	public void postCabriShouldReturnFeedbackMessage() throws Exception {
		Gson gson = new Gson();
		when(service.addtask("1.0", "mocah", jsonfile)).thenReturn(new ResponseEntity<String>(gson.toJson(responsejson), HttpStatus.FOUND));
		
		HttpHeaders headers = new HttpHeaders();
		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "mocah");
		headers.add("Version-LIP6", "1.0");
		this.mockMvc.perform(post("/task").headers(headers).contentType(MediaType.APPLICATION_JSON).content(jsonfile)).andDo(print()).andExpect(status().isFound())
				.andExpect(content().string(containsString(gson.toJson(responsejson))));
	}
}
