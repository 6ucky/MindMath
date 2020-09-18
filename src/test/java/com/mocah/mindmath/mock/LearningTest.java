package com.mocah.mindmath.mock;

import org.junit.jupiter.api.Test;

import com.mocah.mindmath.server.entity.feedback.Feedbackjson;

//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.verify;

public class LearningTest {
	
	@Test
	public void checkLearning() throws Exception {
		//Initialize class
		Feedbackjson feedback = new Feedbackjson("100");
		//create mock
		feedback = mock(Feedbackjson.class);
		
		feedback.getIdFbCabri();
		//verify getId method was called
		verify(feedback).getIdFbCabri();
		
		// define return value for method
		when(feedback.getIdFbCabri()).thenReturn("100");
		assertEquals(feedback.getIdFbCabri(), "100");// Junit
//		assertThat(feedback.getId(), equalTo("100"));// Hamcrest
		assertThat(feedback.getIdFbCabri()).as("check feedback id.").isEqualTo("100").isNotEmpty().isNotEqualTo("200"); // AssertJ
	}
}
