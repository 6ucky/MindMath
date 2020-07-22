package com.mocah.mindmath.learning.ztest;

import com.mocah.mindmath.learning.utils.actions.AbstractAction;

public class GrilleAction extends AbstractAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 6690600593051084876L;

	private static int COUNT = 0;

	private TypeAction type;

	/**
	 * Action with incremental ID
	 */
	public GrilleAction(TypeAction t) {
		super("" + ++COUNT, null);
		this.type = t;
	}

	public TypeAction getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return type.toString();
	}
}
