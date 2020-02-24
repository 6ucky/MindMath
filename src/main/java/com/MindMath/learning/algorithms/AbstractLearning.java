/**
 *
 */
package com.MindMath.learning.algorithms;

import com.MindMath.learning.policies.IPolicy;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public abstract class AbstractLearning implements ILearning {
	protected IPolicy policy;

	/**
	 * @return
	 */
	public IPolicy getPolicy() {
		return this.policy;
	}
}
