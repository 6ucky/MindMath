package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.mocah.mindmath.server.entity.AbstractJsonData;


@Entity
public class FeedbackContent extends AbstractJsonData {
	
	private final String FeedbackName;
	
	private final double Ponderation;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<Motivation> motivations;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<ContentGlossary> contents;
	
	public FeedbackContent() {
		super();
		this.FeedbackName = "";
		this.Ponderation = 0;
		this.motivations = new ArrayList<>();
		this.contents = new ArrayList<>();
	}

	public List<Motivation> getMotivations() {
		return motivations;
	}

	public double getPonderation() {
		return Ponderation;
	}

	public String getFeedbackName() {
		return FeedbackName;
	}

	public List<ContentGlossary> getContents() {
		return contents;
	}
}
