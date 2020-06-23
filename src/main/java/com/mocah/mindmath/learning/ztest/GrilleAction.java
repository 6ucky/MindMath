package com.mocah.mindmath.learning.ztest;

import com.mocah.mindmath.learning.utils.actions.IAction;

public class GrilleAction implements IAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 6690600593051084876L;

	private static int COUNT = 0;
	private String id;

	private TypeAction type;

	/**
	 * Action with incremental ID
	 */
	public GrilleAction(TypeAction t) {
		this.id = "" + ++COUNT;
		this.type = t;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public TypeAction getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return type.toString();
	}

}
