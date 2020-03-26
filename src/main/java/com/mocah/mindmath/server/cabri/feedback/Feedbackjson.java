package com.mocah.mindmath.server.cabri.feedback;

import java.io.Serializable;

public class Feedbackjson implements Serializable{

	private static final long serialVersionUID = -9041203134380944632L;
	
	private final String id;
	private final String url;
	
	public Feedbackjson(String id, String url) {
		this.id = id;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

}
