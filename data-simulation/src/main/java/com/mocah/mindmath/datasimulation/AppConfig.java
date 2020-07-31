/**
 *
 */
package com.mocah.mindmath.datasimulation;

import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mocah.mindmath.datasimulation.json.CabriDataSerializer;
import com.mocah.mindmath.datasimulation.profiles.IProfile;
import com.mocah.mindmath.datasimulation.profiles.ProfileA;
import com.mocah.mindmath.datasimulation.profiles.ProfileB;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class AppConfig {
	/**
	 * Use the production server or not Will use {@code ServerConn.SERVER_URL} or
	 * {@code ServerConn.TEST_SERVER_URL}
	 */
	public static final boolean USE_PROD_SERV = false;

	/**
	 * Number of learners per profile
	 */
	public static final Map<Class<? extends IProfile>, Integer> learners = ImmutableMap.of(ProfileA.class, 1,
			ProfileB.class, 1);
	public static final boolean useRandomProfileOrder = false;
	/**
	 * Number of iteration each learner id will be used. ie: Number of json
	 * simulated and sent
	 */
	public static final int MAX_ITERATION = 10;
	public static final String learnerBase = "Simulated_";

	public static int getWeightInfo(String feedbackId) {
		// temp
		switch (Strings.nullToEmpty(feedbackId)) {
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

	/**
	 * Probability if learner error was recognize
	 */
	public static final double RECON_ERROR_PROB = 0.85;

	private static Gson gson;
	private static final boolean USE_PRETTY_JSON = true;

	public static Gson getGson() {
		if (gson != null)
			return gson;

		GsonBuilder gsonBuilder = new GsonBuilder();

		if (USE_PRETTY_JSON) {
			gsonBuilder.setPrettyPrinting();
		}

		gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		gsonBuilder.registerTypeAdapter(CabriData.class, new CabriDataSerializer());
		gson = gsonBuilder.create();

		return gson;
	}
}
