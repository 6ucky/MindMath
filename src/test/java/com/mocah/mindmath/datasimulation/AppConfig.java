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
import com.mocah.mindmath.datasimulation.profiles.ProfileC;
import com.mocah.mindmath.datasimulation.profiles.ProfileI;
import com.mocah.mindmath.datasimulation.profiles.ProfileJ;
import com.mocah.mindmath.server.controller.cabri.CabriVersion;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class AppConfig {
	/*************************Manuel Configuration for Simulator****************************/
	/**
	 * The version (post address) of generating feedback
	 */
	public static final CabriVersion version = CabriVersion.v1_1;
	/**
	 * The expert mode for version 1.0
	 */
	public static final boolean isExpertMode = true;
	/**
	 * Use the production server or not Will use {@code ServerConn.SERVER_URL} or
	 * {@code ServerConn.TEST_SERVER_URL}
	 */
	public static final boolean USE_PROD_SERV = false;
	/**
	 * Number of learners per profile
	 */
	public static final Map<Class<? extends IProfile>, Integer> learners = ImmutableMap.of(ProfileA.class, 3,
			ProfileB.class, 3, ProfileC.class, 3, ProfileI.class, 3, ProfileJ.class, 3);
	/**
	 * random order of profiles
	 */
	public static final boolean useRandomProfileOrder = false;
	/**
	 * Number of iteration each learner id will be used. ie: Number of json
	 * simulated and sent
	 */
	public static final int MAX_ITERATION = 20;
	/**
	 * The head name for each learner
	 */
	public static final String learnerBase = "sim_";
	/****************************************************************************************/

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
