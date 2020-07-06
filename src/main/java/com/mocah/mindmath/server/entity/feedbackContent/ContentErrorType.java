package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;

import com.mocah.mindmath.server.entity.AbstractJsonData;
import com.mocah.mindmath.server.entity.StringListConverter;

@Entity
public class ContentErrorType extends AbstractJsonData {
	
	private final String erreur_type;
	
	private final String format;
	
	@Convert(converter = StringListConverter.class)
	private final List<String> glossaire;
	
	private final String content_url;
	
	public ContentErrorType() {
		this.erreur_type = "";
		this.format = "";
		this.glossaire = new ArrayList<>();
		this.content_url = "";
	}

	public String getErreur_type() {
		return erreur_type;
	}

	public String getFormat() {
		return format;
	}

	public List<String> getGlossaire() {
		return glossaire;
	}

	public String getContent_url() {
		return content_url;
	}
}
