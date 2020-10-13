package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.mocah.mindmath.server.entity.AbstractJsonData;


@Entity
public class FeedbackContent extends AbstractJsonData {
	
	private final String feedbackID;
	
	private final double ponderation;

	private final String motivation_leaf;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<ContentErrorType> contents;
	
	public FeedbackContent() {
		this.feedbackID = "";
		this.ponderation = 0;
		this.motivation_leaf = "";
		this.contents = new ArrayList<>();
	}

	public List<ContentErrorType> getContents() {
		return contents;
	}

	public String getMotivation_leaf() {
		return motivation_leaf;
	}

	public double getPonderation() {
		return ponderation;
	}
	
	public ContentErrorType getContentErrorType(String erreur_type) {
		for(ContentErrorType content: contents)
		{
			if(content.getErreurID().equals(erreur_type))
				return content;
		}
		return null;
	}

	public String getFeedbackID() {
		return feedbackID;
	}
}
