package com.mocah.mindmath.server.entity.feedbackContent;

import javax.persistence.Entity;

import com.mocah.mindmath.server.entity.AbstractJsonData;

@Entity
public class ContentGlossary extends AbstractJsonData {
	
	private final String erreur_type;
	
	private final String format;
	
	private final String glossaire;
	
	private final String content_url;
	
	public ContentGlossary() {
		this.erreur_type = null;
		this.format = null;
		this.glossaire = null;
		this.content_url = null;
	}

	public String getErreur_type() {
		return erreur_type;
	}

	public String getFormat() {
		return format;
	}

	public String getGlossaire() {
		return glossaire;
	}

	public String getContent_url() {
		return content_url;
	}
}
