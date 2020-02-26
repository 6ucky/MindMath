package com.MindMath.learning.ztest;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Grille {
	public static String[] map = { "S.rDrrrrX...", ".rrddddrddX.", ".rrD....b...", ".rrD.....G..", ".rrdddddddXb",
			".rrD........", ".rrX........", ".rrdddddddXb", ".rrD........", ".rrX........", "............" };

	public static final int WIDTH = map[0].length();
	public static final int HEIGHT = map.length;
	public static final int nbMaxActions = Grille.WIDTH * Grille.HEIGHT / 2;

	private List<GrilleState> etats;
	private List<GrilleAction> actions;
	private GrilleState START;
	private GrilleState current_state;

	public Grille() {
		decodeMap();
	}

	private void decodeMap() {
		int width = map[0].length();
		int height = map.length;
		etats = new ArrayList<>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				char c = map[y].charAt(x);
				GrilleState gs = new GrilleState(new Point(x, y), this);
				switch (c) {
				case 'S':
					gs.setType(TypeEtat.Start);
					this.START = gs;
					this.current_state = gs;
					break;
				case 'X':
					gs.setType(TypeEtat.Forbidden);
					gs.setAccessible(false);
					break;
				case 'd':
					gs.setType(TypeEtat.LittleDanger);
					break;
				case 'D':
					gs.setType(TypeEtat.BigDanger);
					break;
				case 'b':
					gs.setType(TypeEtat.LittleBonus);
					break;
				case 'B':
					gs.setType(TypeEtat.BigBonus);
					break;
				case 'G':
					gs.setType(TypeEtat.Goal);
					break;
				case 'r':
					gs.setType(TypeEtat.Ralenti);
					break;
				default:
					gs.setType(TypeEtat.Normal);
				}

				etats.add(gs);
			}
		}

		actions = new ArrayList<>();
		for (TypeAction a : TypeAction.values()) {
			actions.add(new GrilleAction(a));
		}
	}

	public List<GrilleState> getStates() {
		return this.etats;
	}

	public List<GrilleAction> getActions() {
		return this.actions;
	}

	public GrilleState getCurrentState() {
		return this.current_state;
	}

	public double step(GrilleAction action) {
		double reward = 0;

		Point posResult = new Point(this.current_state.getPoint());
		TypeAction actionDone = action.getType().getRealAction();
		switch (actionDone) {
		case NORD:
			posResult.y--;
			if (posResult.y < 0) {
				posResult.y++;
				reward = TypeEtat.OutBound.getR();
			}
			break;
		case SUD:
			posResult.y++;
			if (posResult.y >= Grille.HEIGHT) {
				posResult.y--;
				reward = TypeEtat.OutBound.getR();
			}
			break;
		case OUEST:
			posResult.x--;
			if (posResult.x < 0) {
				posResult.x++;
				reward = TypeEtat.OutBound.getR();
			}
			break;
		case EST:
			posResult.x++;
			if (posResult.x >= Grille.WIDTH) {
				posResult.x--;
				reward = TypeEtat.OutBound.getR();
			}
			break;
		}

		for (GrilleState grilleState : etats) {
			if (grilleState.getPoint().equals(posResult)) {
				switch (grilleState.getType()) {
				case Forbidden:
				case BigBonus:
				case BigDanger:
				case Start:
				case Goal:
				case LittleBonus:
				case LittleDanger:
				case Normal:
				case OutBound:
				case Ralenti:
				default:
					reward += grilleState.getType().getR();

					break;
				}

				if (grilleState.isAccessible()) {
					this.current_state = grilleState;
				}

				break;
			}
		}

		return reward;
	}

	public void reset() {
		this.current_state = this.START;
	}
}
