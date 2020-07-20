/**
 *
 */
package com.mocah.mindmath.datasimulation.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.google.gson.annotations.Expose;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class SimulatedDataLearner implements List<SimulatedData> {
	@Expose
	private int iteration;
	@Expose
	private String learnerId;
	@Expose
	private List<SimulatedData> dataset;

	private String learnerCSV;

	public SimulatedDataLearner(int it, String learnerId) {
		this.iteration = it;
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

	@Override
	public int size() {
		return dataset.size();
	}

	@Override
	public boolean isEmpty() {
		return dataset.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return dataset.contains(o);
	}

	@Override
	public Iterator<SimulatedData> iterator() {
		return dataset.iterator();
	}

	@Override
	public Object[] toArray() {
		return dataset.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return dataset.toArray(a);
	}

	@Override
	public boolean add(SimulatedData e) {
		return dataset.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return dataset.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return dataset.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends SimulatedData> c) {
		return dataset.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends SimulatedData> c) {
		return dataset.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return dataset.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return dataset.retainAll(c);
	}

	@Override
	public void clear() {
		dataset.clear();
	}

	@Override
	public SimulatedData get(int index) {
		return dataset.get(index);
	}

	@Override
	public SimulatedData set(int index, SimulatedData element) {
		return dataset.set(index, element);
	}

	@Override
	public void add(int index, SimulatedData element) {
		dataset.add(index, element);
	}

	@Override
	public SimulatedData remove(int index) {
		return dataset.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return dataset.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return dataset.lastIndexOf(o);
	}

	@Override
	public ListIterator<SimulatedData> listIterator() {
		return dataset.listIterator();
	}

	@Override
	public ListIterator<SimulatedData> listIterator(int index) {
		return dataset.listIterator(index);
	}

	@Override
	public List<SimulatedData> subList(int fromIndex, int toIndex) {
		return dataset.subList(fromIndex, toIndex);
	}
}
