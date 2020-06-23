package com.mocah.mindmath.learning.utils.actions;

import java.io.Serializable;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IAction extends Serializable {

	/**
	 * Get the identifier of an action
	 *
	 * @return an id
	 */
	public String getId();
}
