package com.mocah.mindmath.learning.utils.states;

import java.io.Serializable;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IState extends Serializable {

	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);
}
