/**
 *
 */
package com.mocah.mindmath.datasimulation;

import java.util.Date;

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

	public static final int MAX_ITERATION = 10;
	public static final int NB_DIF_LEARNERS = 1;
	private static final String learnerBase = "Simulated_";

	public static String getLearnerID() {
		Date date = new Date();
		return learnerBase + date.getTime();
	}

	public static final DomainEnum domain = DomainEnum.ALGEBRA;
	public static final GeneratorEnum generator = GeneratorEnum.RES_EQ_PERMIER_DEGRE;
	public static final TaskFamilyEnum taskFamily = TaskFamilyEnum.FT3_2_1;
	public static final ActivityModeEnum activityMode = ActivityModeEnum.A0;

	public static final boolean READ_FEEDBACK = false;
	public static final boolean WAIT_INFORMATIVE = false;

	public static final double BASE_SUCCESS_PROB = 0.5;
	private static final double DELTA = 0.01;

	public static double calcNewSuccessProb(double oldProb) {
		return calcNewSuccessProb(oldProb, 0);
	}

	public static double calcNewSuccessProb(double oldProb, int fdbkInfoWeight) {
		if (fdbkInfoWeight == 0)
			return oldProb + oldProb * DELTA;

		return .0;
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
