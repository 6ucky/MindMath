package com.mocah.mindmath.server.entity.feedbackContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;

import com.mocah.mindmath.server.entity.AbstractJsonData;
import com.mocah.mindmath.server.entity.StringListConverter;

@Entity
public class ContentErrorType extends AbstractJsonData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4420845682552703808L;

	private final String erreurID;
	
	private final String format;
	
	@Convert(converter = StringListConverter.class)
	private final List<String> glossaire;
	
	private final String content_url;
	
	public ContentErrorType() {
		this.erreurID = "";
		this.format = "";
		this.glossaire = new ArrayList<>();
		this.content_url = "";
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

	public String getErreurID() {
		return erreurID;
	}
}
