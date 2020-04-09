package com.mocah.mindmath.repository.learninglocker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

public class FeedbackforLRS implements Serializable{

	private static final long serialVersionUID = -7894086246416787110L;

	private final String id;
	
	private final Sensors sensors;

	private final List<Log> log;
	
	private String motivationalElement;
	
	private String solutionModel;
	
	private String glossary;
	
	public FeedbackforLRS() {
		List<Log> emptylist = new ArrayList<Log>();
		this.log = emptylist;
		this.sensors = new Sensors();
		this.id = "";
	}
	
	public FeedbackforLRS(String id, Sensors sensors, List<Log> log) {
		this.id = id;
		this.sensors = sensors;
		this.log = log;
	}

	public String getId() {
		return id;
	}

	public Sensors getSensors() {
		return sensors;
	}

	public List<Log> getLog() {
		return log;
	}

	public String getMotivationalElement() {
		return motivationalElement;
	}

	public void setMotivationalElement(String motivationalElement) {
		this.motivationalElement = motivationalElement;
	}

	public String getSolutionModel() {
		return solutionModel;
	}

	public void setSolutionModel(String solutionModel) {
		this.solutionModel = solutionModel;
	}

	public String getGlossary() {
		return glossary;
	}

	public void setGlossary(String glossary) {
		this.glossary = glossary;
	}
}
