package com.mocah.mindmath.parser.jsonparser;

/**
 * @author	Yan Wang
 * @since	09/03/2020
 */
public class JsonParserKeys {
	
	//Task id is the main key for database
	public static String TASK_ID = "id";
	public static String TASK_NAME = "task";
	public static String TASK_TRIGGER = "trigger";
	public static String TASK_FEEDBACK_ID = "feedback_id";
	
	//Parameters object
	public static String PARAMS = "params";
	public static String PARAMS_VT_2_1 = "VT-2.1";
	public static String PARAMS_VT_2_2 = "VT-2.2";
	public static String PARAMS_VT_2_3 = "VT-2.3";
	public static String PARAMS_VT_2_4 = "VT-2.4";
	
	//Sensors object
	public static String SENSOR = "sensors";
	public static String SENSOR_CAPTEUR_BOOL_RF_CO2_1 = "capteur-bool-RF-CO2-1";
	public static String SENSOR_CAPTEUR_BOOL_RF_CO2_2 = "capteur-bool-RF-CO2-2";
	public static String SENSOR_CAPTEUR_BOOL_RF_CO2_3 = "capteur-bool-RF-CO2-3";
	public static String SENSOR_CAPTEUR_BOOL_RJ = "capteur-bool-RJ";
	public static String SENSOR_CAPTEUR_NB_TEMPS = "capteur-nb-temps";
	public static String SENSOR_CAPTEUR_NB_VALIDER = "capteur-nb-valider";
	public static String SENSOR_CAPTEUR_NB_EFFACER = "capteur-nb-effacer";
	public static String SENSOR_CAPTEUR_NB_AIDE = "capteur-nb-aide";
	public static String SENSOR_DOMAIN = "domain";
	public static String SENSOR_GENERATOR = "generator";
	public static String SENSOR_TASKFAMILY = "taskFamily";
	public static String SENSOR_CORRECTANSWER = "correctAnswer";
	public static String SENSOR_CODEERROR = "codeError";
	public static String SENSOR_ACTIVITYMODE = "activityMode";
	
	//Log object
	public static String LOG = "log";
	public static String LOG_TIME = "time";
	public static String LOG_TYPE = "type";
	public static String LOG_NAME = "name";
	public static String LOG_ACTION = "action";
	
	//LRS object
	public static String LRS_STATEMENT = "statements";
}
