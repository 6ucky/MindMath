/**
 *
 */
package com.mocah.mindmath.datasimulation.json;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class SimulatedDataContainer {
	@Expose
	private Date date;
	@Expose
	private Map<String, SimulatedDataLearner> datasets;

	private String finalCSV;

	public SimulatedDataContainer() {
		this.date = new Date();
		this.datasets = new HashMap<>();
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the datasets
	 */
	public Map<String, SimulatedDataLearner> getDatasets() {
		return datasets;
	}

	/**
	 * @return the final qtable as CSV
	 */
	public String getFinalCSV() {
		return finalCSV;
	}

	/**
	 * @param finalCSV the final qtable as CSV to define
	 */
	public void setFinalCSV(String finalCSV) {
		this.finalCSV = finalCSV;
	}
}
