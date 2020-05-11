package com.mocah.mindmath.mock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.verify;

import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;

public class LearningTest {
	
	@Test
	public void checkLearning() throws Exception {
		//Initialize class
		Feedbackjson feedback = new Feedbackjson("100");
		//create mock
		feedback = mock(Feedbackjson.class);
		
		feedback.getId();
		//verify getId method was called
		verify(feedback).getId();
		
		// define return value for method
		when(feedback.getId()).thenReturn("100");
		assertEquals(feedback.getId(), "100");
	}
}
