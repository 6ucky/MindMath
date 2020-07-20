/**
 *
 */
package com.mocah.mindmath.datasimulation.json;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.annotations.Expose;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class SimulatedDataContainer implements Map<String, SimulatedDataLearner> {
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

	@Override
	public int size() {
		return datasets.size();
	}

	@Override
	public boolean isEmpty() {
		return datasets.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return datasets.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return datasets.containsValue(value);
	}

	@Override
	public SimulatedDataLearner get(Object key) {
		return datasets.get(key);
	}

	@Override
	public SimulatedDataLearner put(String key, SimulatedDataLearner value) {
		return datasets.put(key, value);
	}

	@Override
	public SimulatedDataLearner remove(Object key) {
		return datasets.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends SimulatedDataLearner> m) {
		datasets.putAll(m);
	}

	@Override
	public void clear() {
		datasets.clear();
	}

	@Override
	public Set<String> keySet() {
		return datasets.keySet();
	}

	@Override
	public Collection<SimulatedDataLearner> values() {
		return datasets.values();
	}

	@Override
	public Set<Entry<String, SimulatedDataLearner>> entrySet() {
		return datasets.entrySet();
	}

}
