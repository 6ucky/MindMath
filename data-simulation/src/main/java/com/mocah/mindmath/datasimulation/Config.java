/**
 *
 */
package com.mocah.mindmath.datasimulation;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ActivityModeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.GeneratorEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TaskFamilyEnum;
import com.mocah.mindmath.datasimulation.json.CabriDataSerializer;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Config {
	/**
	 * Use the production server or not Will use {@code ServerConn.SERVER_URL} or
	 * {@code ServerConn.TEST_SERVER_URL}
	 */
	public static final boolean USE_PROD_SERV = false;

	/**
	 * Number of different learner id used
	 */
	public static final int NB_DIF_LEARNERS = 2;
	/**
	 * Number of iteration each learner id will be used
	 */
	public static final int MAX_ITERATION = 10;
	private static final String learnerBase = "Simulated_";

	/**
	 * Generate a learner ID: {@code Config.learnerBase+timestamp}
	 *
	 * @return a learner ID
	 */
	public static String getLearnerID() {
		Date date = new Date();
		return learnerBase + date.getTime();
	}

	public static final DomainEnum domain = DomainEnum.ALGEBRA;
	public static final GeneratorEnum generator = GeneratorEnum.RES_EQ_PERMIER_DEGRE;
	public static final TaskFamilyEnum taskFamily = TaskFamilyEnum.FT3_2_1;
	public static final ActivityModeEnum activityMode = ActivityModeEnum.A0;

	/**
	 * Does the learner car about informations in feedbacks ?
	 */
	public static final boolean READ_FEEDBACK = false;
	/**
	 * Does the learner wants only informative feedbacks ?
	 */
	public static final boolean WAIT_INFORMATIVE = false;
	/**
	 * Minimal wheight for a feedback considered as "informative"
	 */
	public static final int MIN_WHEIGT_INFO = 0;

	public static final double BASE_SUCCESS_PROB = 0.5;

	/**
	 * Map of modification deltas for each feedback info weight <br>
	 * <br>
	 * {@code prob = oldProb + oldProb * DELTAS.getOrDefault(fdbkInfoWeight, DEFAULT_DELTA)}
	 */
	private static final Map<Integer, Double> DELTAS = ImmutableMap.of(0, 0.01, 1, 0.02, 2, 0.04, 3, 0.08, 4, 0.16);
	private static final double DEFAULT_DELTA = 0.01;

	public static int getWeightInfo(String feedbackId) {
		// temp
		switch (feedbackId) {
		default:
			return 0;

		case "0.0.0.0":
			return 1;

		case "1.0.0.0":
		case "1.1.GC.0":
		case "1.1.GNC.0":
		case "2.0.0.XE":
		case "2.0.0.XFT":
		case "2.1.GNC.XE":
			return 2;

		case "1.2.IC.0":
		case "1.2.INC.0":
		case "3.0.0.XE":
		case "3.0.0.XFT":
			return 3;

		case "3.2.IC.0":
		case "3.2.INC.0":
		case "3.2.IC.XE":
		case "3.2.IC.XFT":
		case "3.2.INC.XE":
		case "3.2.INC.XFT":
			return 4;
		}
	}

	public static double calcNewSuccessProb(double oldProb) {
		return calcNewSuccessProb(oldProb, 0);
	}

	public static double calcNewSuccessProb(double oldProb, int fdbkInfoWeight) {
		double prob = oldProb + oldProb * DELTAS.getOrDefault(fdbkInfoWeight, DEFAULT_DELTA);

		if (prob > 1) {
			prob = 1;
		} else if (prob < 0) {
			prob = 0;
		}

		return prob;
	}

	public static final double ASK_HELP_PROB = 0;

	/**
	 * Probability if learner error was recognize
	 */
	public static final double RECON_ERROR_PROB = 0.95;

	private static Gson gson;

	public static Gson getGson() {
		if (gson != null)
			return gson;

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CabriData.class, new CabriDataSerializer());
		gson = gsonBuilder.create();

		return gson;
	}
}
