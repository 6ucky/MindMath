package com.MindMath.learning.ztest;

import java.awt.Point;
import java.util.Random;

public enum TypeAction {
	NORD("nord"), EST("est"), SUD("sud"), OUEST("ouest");

	private static double P_FOWARD = 1;
	private static double P_LEFT = 0.2;
	private static double P_RIGHT = 0.1;

	private String name;

	TypeAction(String s) {
		this.name = s;
	}

	public TypeAction getRealAction() {
		Random r = new Random();
		double d = r.nextDouble();

		if (d <= P_FOWARD)
			return this;
		else if (d <= P_FOWARD + P_LEFT)
			return TypeAction.values()[Math.abs((this.ordinal() - 1) % TypeAction.values().length)];
		else
			return TypeAction.values()[Math.abs((this.ordinal() + 1) % TypeAction.values().length)];
	}

	/**
	 * retourne un point accessible a partir du point passé en paramètre en suivant
	 * l'action en cours (de (2,2) on arrive à (2,3) en allant au Sud)
	 *
	 * @param p point d'orinine de l'action
	 * @return point destination
	 */
	Point nextPoint(Point p) {
		Point result = new Point(p);
		switch (this) {
		case NORD:
			result.y--;
			break;
		case SUD:
			result.y++;
			break;
		case OUEST:
			result.x--;
			break;
		case EST:
			result.x++;
			break;
		}
		return result;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
