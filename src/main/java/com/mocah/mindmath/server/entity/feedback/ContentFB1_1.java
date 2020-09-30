package com.mocah.mindmath.server.entity.feedback;

public class ContentFB1_1 {
	
	private String image_url;
	private String feedbackID;
	private String leaf;
	private String error_code;
	private String id_learner;
	private String id_task;
	private String statementRef;
	
	public ContentFB1_1(String image_url, String feedbackID, String leaf, String error_code,
			String id_learner, String id_task, String statementRef)
	{
		this.setImage_url(image_url);
		this.setFeedbackID(feedbackID);
		this.setLeaf(leaf);
		this.setError_code(error_code);
		this.setId_learner(id_learner);
		this.setId_task(id_task);
		this.setStatementRef(statementRef);
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
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

	public String getId_learner() {
		return id_learner;
	}

	public void setId_learner(String id_learner) {
		this.id_learner = id_learner;
	}

	public String getId_task() {
		return id_task;
	}

	public void setId_task(String id_task) {
		this.id_task = id_task;
	}

	public String getStatementRef() {
		return statementRef;
	}

	public void setStatementRef(String statementRef) {
		this.statementRef = statementRef;
	}
}
