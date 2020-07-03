package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.ArrayList;
import java.util.List;

public class FeedbackContentList {
	
	private final List<FeedbackContent> feedbackcontents;
	
	public FeedbackContentList() {
		this.feedbackcontents = new ArrayList<>();
	}

	public List<FeedbackContent> getFeedbackcontents() {
		return feedbackcontents;
	}

}
