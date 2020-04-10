package com.mocah.mindmath.repository.learninglocker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

/**
 * @author	Yan Wang
 * @since	10/04/2020
 */

public class FeedbackforLRS implements Serializable{

	private static final long serialVersionUID = -7894086246416787110L;

	private String task_id;
	
	private Sensors sensors;

	private List<Log> log;
	
	private final String motivationalElement;
	
	private final String solutionModel;
	
	private final String glossary;
	
	// TODO set test values for the last three variables for test
	public FeedbackforLRS() {
		List<Log> emptylist = new ArrayList<Log>();
		this.log = emptylist;
		this.sensors = new Sensors();
		this.task_id = "";
		this.motivationalElement = "Bravo!";
		this.solutionModel = "url";
		this.glossary = "hypertext";
	}
	
	public FeedbackforLRS(String id, Sensors sensors, List<Log> log) {
		this();
		this.task_id = id;
		this.sensors = sensors;
		this.log = log;
	}

	public String getId() {
		return task_id;
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

	public String getSolutionModel() {
		return solutionModel;
	}

	public String getGlossary() {
		return glossary;
	}

}
