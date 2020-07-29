/**
 *
 */
package com.mocah.mindmath.learning.utils.actions;

import com.mocah.mindmath.learning.utils.states.IState;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class MindMathAction extends AbstractAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -4240823337990480436L;

	private String leaf;
	private double initalWeight;

	/**
	 * Action with custom ID
	 */
	public MindMathAction(String id, IState state) {
		this(id, state, null, 0.0);
	}

	/**
	 * Action with custom ID
	 */
	public MindMathAction(String id, IState state, String leaf, double initalWeight) {
		super(id, state);
		this.leaf = leaf;
		this.initalWeight = initalWeight;
	}

	/**
	 * @return the decision leaf name
	 */
	public String getLeaf() {
		return this.id;
	}

	/**
	 * @param leaf the decision leaf name
	 */
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

	/**
	 * @return the initial action weight
	 */
	public double getInitalWeight() {
		return initalWeight;
	}

	/**
	 * @param initalWeight the initial action weight to define
	 */
	public void setInitalWeight(double initalWeight) {
		this.initalWeight = initalWeight;
	}

	@Override
	public String toString() {
		return "leaf_" + this.leaf + "_fdbck_" + this.id;
	}
}
