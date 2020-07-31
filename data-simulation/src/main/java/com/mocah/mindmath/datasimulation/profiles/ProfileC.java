/**
 *
 */
package com.mocah.mindmath.datasimulation.profiles;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class ProfileC extends AbstractProfile {
	private Map<Integer, Double> originDeltas;
	private int iteration;
	private double k = 1.0; // carrying capacity
	private Map<Integer, Double> a = ImmutableMap.of(0, 0.05, 1, 0.075, 2, 0.1, 3, 0.1, 4, 0.0);
	private double defaultA = 0.05;

	/**
	 *
	 */
	public ProfileC() {
	}

	/**
	 * @param learnerID
	 */
	public ProfileC(String learnerID) {
		super(learnerID);
	}

	@Override
	public void initLearner() {
		super.initLearner();

		// Feedback consideration
		this.readingFeedback = true;
//		this.waitingInformative = true;
//		this.minWheightInfo = 3;

		// Exercise Success
		this.baseSuccessProb = 0.0;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.0, 1, 0.0, 2, 0.0, 3, 0.9, 4, 1.0));
		this.originDeltas = ImmutableMap.copyOf(this.deltas);
		this.firstIncreaseProb = 1.0;
		this.defaultDelta = 0.1;

		this.iteration = 0;

		this.askHelpProb = 0.0;
	}

	@Override
	public void calcNewSuccessProb(int fdbkInfoWeight) {
		super.calcNewSuccessProb(fdbkInfoWeight);

		// Apply Verhulst's logistic function
		// https://fr.wikipedia.org/wiki/Mod%C3%A8le_de_Verhulst#R%C3%A9solution_en_temps_continu
		this.iteration += 1;
		for (Entry<Integer, Double> entry : this.deltas.entrySet()) {
			double newDelta;

			double originDelta = this.originDeltas.getOrDefault(entry.getKey(), 0.0);
			if (originDelta <= 0) {
				originDelta = 0.001;
			}

			newDelta = k / (1 + ((k / (originDelta + 0.001)) - 1)
					* Math.exp(-a.getOrDefault(entry.getKey(), defaultA) * iteration));

			this.deltas.put(entry.getKey(), newDelta);
		}
	}
}
