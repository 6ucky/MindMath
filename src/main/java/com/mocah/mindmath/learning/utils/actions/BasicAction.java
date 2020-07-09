/**
 *
 */
package com.mocah.mindmath.learning.utils.actions;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class BasicAction implements IAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 354477421231673616L;

	private String id;

	/**
	 * Action with custom ID
	 */
	public BasicAction(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
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
		BasicAction other = (BasicAction) obj;
		if (this.id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "fdbck_" + this.id;
	}
}
