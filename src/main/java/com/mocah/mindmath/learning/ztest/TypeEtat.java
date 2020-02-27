package com.mocah.mindmath.learning.ztest;

public enum TypeEtat {
	OutBound("", -10000), Forbidden("X", -1000), BigDanger("D", -500), LittleDanger("d", -100), Normal(".", -1),
	LittleBonus("b", 100), BigBonus("B", 1000), Ralenti("r", -10), Start("S", -1), Goal("G", 1000);

	private String name;
	private double reward;

	TypeEtat(String s, double r) {
		this.name = s;
		this.reward = r;
	}

	public double getR() {
		return this.reward;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
