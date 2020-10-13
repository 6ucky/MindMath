package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.mocah.mindmath.server.entity.AbstractJsonData;

@Entity
public class FeedbackContentList extends AbstractJsonData {

	@OneToMany(cascade = CascadeType.ALL)
	private List<FeedbackContent> feedback_content;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Glossaire> glossairelist;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Motivation> motivationlist;
	
	private final String generator;

	public FeedbackContentList() {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "");
	}
	
	public FeedbackContentList(List<FeedbackContent> feedbackcontentlist, List<Glossaire> glossairelist,
			List<Motivation> motivationlist, String generator){
		super();
		this.setFeedback_content(feedbackcontentlist);
		this.setGlossairelist(glossairelist);
		this.setMotivationlist(motivationlist);
		this.generator = generator;
	}

	public String getGenerator() {
		return generator;
	}

	public List<Glossaire> getGlossairelist() {
		return glossairelist;
	}

	public void setGlossairelist(List<Glossaire> glossairelist) {
		this.glossairelist = glossairelist;
	}

	public List<Motivation> getMotivationlist() {
		return motivationlist;
	}

	public void setMotivationlist(List<Motivation> motivationlist) {
		this.motivationlist = motivationlist;
	}

	public List<FeedbackContent> getFeedback_content() {
		return feedback_content;
	}

	public void setFeedback_content(List<FeedbackContent> feedback_content) {
		this.feedback_content = feedback_content;
	}

}
