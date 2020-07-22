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

	/**
	 * Action with custom ID
	 */
	public MindMathAction(String id, IState state) {
		this(id, state, null);
	}

	/**
	 * Action with custom ID
	 */
	public MindMathAction(String id, IState state, String leaf) {
		super(id, state);
		this.leaf = leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

	public String getLeaf() {
		return this.id;
	}

	@Override
	public String toString() {
		return "leaf_" + this.leaf + "_fdbck_" + this.id;
	}
}
