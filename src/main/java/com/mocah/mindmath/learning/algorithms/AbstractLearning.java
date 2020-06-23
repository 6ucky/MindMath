/**
 *
 */
package com.mocah.mindmath.learning.algorithms;

import com.mocah.mindmath.learning.policies.IPolicy;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public abstract class AbstractLearning implements ILearning {
	/**
	 *
	 */
	private static final long serialVersionUID = 7120091210326629179L;
	protected IPolicy policy;

	public AbstractLearning(IPolicy policy) {
		this.policy = policy;
	}

	/**
	 * @return
	 */
	public IPolicy getPolicy() {
		return this.policy;
	}
}
