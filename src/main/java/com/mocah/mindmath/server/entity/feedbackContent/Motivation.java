package com.mocah.mindmath.server.entity.feedbackContent;

import javax.persistence.Entity;

import com.mocah.mindmath.server.entity.AbstractJsonData;

@Entity
public class Motivation extends AbstractJsonData {
	
	private final String motivation_leaf;
	
	private final String activityMode;
	
	private final String motivation_data;
	
	public Motivation() {
		this.motivation_leaf = "";
		this.activityMode = "";
		this.motivation_data = "";
	}

	public String getMotivation_data() {
		return motivation_data;
	}

	public String getActivityMode() {
		return activityMode;
	}

	public String getMotivation_leaf() {
		return motivation_leaf;
	}
}
