/**
 *
 */
package com.mocah.mindmath.learning.utils.actions;

import com.mocah.mindmath.learning.utils.states.IState;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class BasicAction extends AbstractAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 354477421231673616L;

	/**
	 * Action with custom ID
	 */
	public BasicAction(String id, IState state) {
		super(id, state);
	}
}
