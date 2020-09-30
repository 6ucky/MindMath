package com.mocah.mindmath.server.entity.feedback;

public class GlossaryFB1_1 {

	private String glossary_url;
	private String glossary_name;
	private String feedbackID;
	private String leaf;
	private String error_code;
	
	public GlossaryFB1_1(String glossary_url, String glossary_name, String feedbackID, String leaf, String error_code)
	{
		this.setGlossary_url(glossary_url);
		this.setGlossary_name(glossary_name);
		this.setFeedbackID(feedbackID);
		this.setLeaf(leaf);
		this.setError_code(error_code);
	}

	public String getGlossary_url() {
		return glossary_url;
	}

	public void setGlossary_url(String glossary_url) {
		this.glossary_url = glossary_url;
	}

	public String getGlossary_name() {
		return glossary_name;
	}

	public void setGlossary_name(String glossary_name) {
		this.glossary_name = glossary_name;
	}

	public String getFeedbackID() {
		return feedbackID;
	}

	public void setFeedbackID(String feedbackID) {
		this.feedbackID = feedbackID;
	}

	public String getLeaf() {
		return leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
}
