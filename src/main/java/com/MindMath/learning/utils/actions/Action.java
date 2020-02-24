/**
 *
 */
package com.MindMath.learning.utils.actions;

/**
 * @author tsimonfine
 *
 */
public class Action implements IAction {
	private static int COUNT = 0;
	private int id;

	public Action() {
		this.id = ++COUNT;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Action other = (Action) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
