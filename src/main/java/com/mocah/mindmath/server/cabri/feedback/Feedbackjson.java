package com.mocah.mindmath.server.cabri.feedback;

import java.io.Serializable;

public class Feedbackjson implements Serializable{

	private static final long serialVersionUID = -9041203134380944632L;
	
	private final String id;
	private final String idF;
	private final String motivationalElement;
	private final String solutionModel;
	private final String glossary;
	
	//Test Feedback response
	public Feedbackjson(String id) {
		this.id = id;
		this.idF = "F1.1";
		this.motivationalElement = "Bravo!";
		this.solutionModel = "mindmath.lip6.fr/videos/ResolutionEquation.mp4";
		this.glossary = "hypertext";
	}
	
	public Feedbackjson(String id, String idF, String url, String motivationalElement, String solutionModel, String glossary) {
		this.id = id;
		this.idF = idF;
		this.motivationalElement = motivationalElement;
		this.solutionModel = solutionModel;
		this.glossary = glossary;
	}

	public String getId() {
		return id;
	}

	public String getMotivationalElement() {
		return motivationalElement;
	}

	public String getSolutionModel() {
		return solutionModel;
	}

	public String getGlossary() {
		return glossary;
	}

	public String getIdF() {
		return idF;
	}

}
