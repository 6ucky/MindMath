package com.MindMath.learning.ztest;

import java.awt.Point;

import com.MindMath.learning.utils.states.IState;

public class GrilleState implements IState {
	private TypeEtat type;
	private Point pt;
	private boolean isAccessible;

	public GrilleState(Point point, Grille grille) {
		this.pt = point;
		this.isAccessible = true;
	}

	public void setType(TypeEtat type) {
		this.type = type;
	}

	public TypeEtat getType() {
		return this.type;
	}

	public Point getPoint() {
		return this.pt;
	}

	public void setAccessible(boolean b) {
		this.isAccessible = b;
	}

	public boolean isAccessible() {
		return this.isAccessible;
	}

	@Override
	public String toString() {
		return "(" + this.pt.x + "," + this.pt.y + ")/" + this.type;
	}

}
