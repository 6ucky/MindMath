package com.mocah.mindmath.server.entity.feedback;

public class GlossaryFBHTML {

	private String content_propriete;
	private String content_preservation;
	
	public GlossaryFBHTML(String content_propriete, String content_preservation) {
		this.setContent_propriete(content_propriete);
		this.setContent_preservation(content_preservation);
	}

	public String getContent_propriete() {
		return content_propriete;
	}

	public void setContent_propriete(String content_propriete) {
		this.content_propriete = content_propriete;
	}

	public String getContent_preservation() {
		return content_preservation;
	}

	public void setContent_preservation(String content_preservation) {
		this.content_preservation = content_preservation;
	}
}
