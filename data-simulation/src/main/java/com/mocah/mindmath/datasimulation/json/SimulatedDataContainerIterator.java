/**
 *
 */
package com.mocah.mindmath.datasimulation.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class SimulatedDataContainerIterator implements Iterator<SimulatedDataLearner> {
	private Map<String, SimulatedDataLearner> dataset;
	private String nextKey;
	private SimulatedDataLearner previous;

	public SimulatedDataContainerIterator(SimulatedDataContainer container) {
		this.dataset = new HashMap<>(container.getDatasets());
	}

	@Override
	public boolean hasNext() {
		if (dataset.isEmpty())
			return false;

		String next = null;
		int iteration = 0;
		if (previous != null) {
			iteration = previous.getIteration();
		}

		int lastI = Integer.MAX_VALUE;
		for (Entry<String, SimulatedDataLearner> entries : dataset.entrySet()) {
			int i = entries.getValue().getIteration();

			if (i > iteration && i < lastI) {
				next = entries.getKey();
				lastI = i;
			}
		}
		this.nextKey = next;

		return this.nextKey != null;
	}

	@Override
	public SimulatedDataLearner next() {
		if (nextKey != null || hasNext()) {
			this.previous = dataset.remove(nextKey);
			return previous;
		} else
			return null;
	}
}
