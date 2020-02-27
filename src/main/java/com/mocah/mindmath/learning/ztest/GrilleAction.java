package com.mocah.mindmath.learning.ztest;

import com.mocah.mindmath.learning.utils.actions.IAction;

public class GrilleAction implements IAction {
	private static int COUNT = 0;
	private int id;

	private TypeAction type;

	/**
	 * Action with incremental ID
	 */
	public GrilleAction(TypeAction t) {
		this.id = ++COUNT;
		this.type = t;
	}

	@Override
	public int getId() {
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
