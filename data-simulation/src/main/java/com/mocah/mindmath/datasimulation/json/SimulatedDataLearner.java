/**
 *
 */
package com.mocah.mindmath.datasimulation.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.mocah.mindmath.datasimulation.profiles.IProfile;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class SimulatedDataLearner {
	@Expose
	private int iteration;
	@Expose
	private IProfile profile;
	@Expose
	private String learnerId;
	@Expose
	private List<SimulatedData> dataset;

	private String learnerCSV;

	public SimulatedDataLearner(int it, IProfile profile, String learnerId) {
		this.iteration = it;
		this.profile = profile;
		this.learnerId = learnerId;
		this.dataset = new ArrayList<>();
	}

	/**
	 * @return the iteration
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * @return the learner ID
	 */
	public String getLearnerId() {
		return this.learnerId;
	}

	/**
	 * @return the learner dataset
	 */
	public List<SimulatedData> getDataset() {
		return this.dataset;
	}

	/**
	 * @return the learner qvalue status as CSV
	 */
	public String getLearnerCSV() {
		return learnerCSV;
	}

	/**
	 * @param learnerCSV the learner qvalue status as CSV to define
	 */
	public void setLearnerCSV(String learnerCSV) {
		this.learnerCSV = learnerCSV;
	}
}
