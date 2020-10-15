package com.mocah.mindmath.server.entity.feedbackContent;

import java.io.Serializable;
import java.util.List;

public class FeedbackContentListRedis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6169195684006103362L;

	private List<FeedbackContentList> feedbackcontentlists;
	
	public FeedbackContentListRedis(List<FeedbackContentList> feedbackcontentlists) {
		this.feedbackcontentlists = feedbackcontentlists;
	}

	public List<FeedbackContentList> getFeedbackcontentlists() {
		return feedbackcontentlists;
	}

	public void setFeedbackcontentlists(List<FeedbackContentList> feedbackcontentlists) {
		this.feedbackcontentlists = feedbackcontentlists;
	}
}
