package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.ArrayList;
import java.util.List;

public class FeedbackContentList <T> {
	
	private final List<FeedbackContent> feedbackcontentlist;
	private final List<Glossaire> glossairelist;
	private final List<Motivation> motivationlist;
	
	public FeedbackContentList() {
		this.feedbackcontentlist = new ArrayList<>();
		this.glossairelist = new ArrayList<>();
		this.motivationlist = new ArrayList<>();
	}

	public List<Glossaire> getGlossairelist() {
		return glossairelist;
	}

	public List<Motivation> getMotivationlist() {
		return motivationlist;
	}

	public List<FeedbackContent> getFeedbackcontentlist() {
		return feedbackcontentlist;
	}

}
