/**
 *
 */
package com.mocah.mindmath.learning.utils.actions;

import java.util.Objects;

import com.mocah.mindmath.learning.utils.states.IState;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public abstract class AbstractAction implements IAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -6162069469461575570L;

	protected String id;
	protected IState state;

	/**
	 * Action with custom ID
	 */
	public AbstractAction(String id, IState state) {
		this.id = id;
		this.state = state;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public IState getState() {
		return this.state;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, state);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractAction other = (AbstractAction) obj;
		return Objects.equals(id, other.id) && Objects.equals(state, other.state);
	}

	@Override
	public String toString() {
		return "fdbck_" + this.id;
	}
}
