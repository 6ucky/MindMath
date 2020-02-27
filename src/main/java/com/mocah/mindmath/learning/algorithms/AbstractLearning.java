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
