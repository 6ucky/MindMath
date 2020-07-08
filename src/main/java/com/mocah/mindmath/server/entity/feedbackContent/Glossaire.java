package com.mocah.mindmath.server.entity.feedbackContent;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.mocah.mindmath.server.entity.AbstractJsonData;

@Entity
public class Glossaire extends AbstractJsonData {
	
	private final String glossaireID;
	
	private final String glossaire_name;
	
	@Column(length = 1000)
	private final String glossaire_content;
	
	public Glossaire() {
		this.glossaireID = "";
		this.glossaire_name = "";
		this.glossaire_content = "";
	}

	public String getGlossaire_name() {
		return glossaire_name;
	}

	public String getGlossaire_content() {
		return glossaire_content;
	}

	public String getGlossaireID() {
		return glossaireID;
	}

}
