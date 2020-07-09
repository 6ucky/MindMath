/**
 *
 */
package com.mocah.mindmath.learning.utils.actions;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class MindMathAction implements IAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -4240823337990480436L;

	private String id;
	private String leaf;

	/**
	 * Action with custom ID
	 */
	public MindMathAction(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

	public String getLeaf() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MindMathAction other = (MindMathAction) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "leaf_" + this.leaf + "_fdbck_" + this.id;
	}
}
