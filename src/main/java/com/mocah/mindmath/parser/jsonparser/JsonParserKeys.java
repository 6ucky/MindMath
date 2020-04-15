package com.mocah.mindmath.parser.jsonparser;

/**
 * @author	Yan Wang
 * @since	09/03/2020
 */
public class JsonParserKeys {
	
	//Task id is the main key for database
	protected static String TASK_ID = "id";
	protected static String TASK_NAME = "task";
	protected static String TASK_TRIGGER = "trigger";
	
	//Parameters object
	protected static String PARAMS = "params";
	protected static String PARAMS_VT_2_1 = "VT-2.1";
	protected static String PARAMS_VT_2_2 = "VT-2.2";
	protected static String PARAMS_VT_2_3 = "VT-2.3";
	protected static String PARAMS_VT_2_4 = "VT-2.4";
	
	//Sensors object
	protected static String SENSOR = "sensors";
	protected static String SENSOR_CAPTEUR_BOOL_RF_CO2_1 = "capteur-bool-RF-CO2-1";
	protected static String SENSOR_CAPTEUR_BOOL_RF_CO2_2 = "capteur-bool-RF-CO2-2";
	protected static String SENSOR_CAPTEUR_BOOL_RF_CO2_3 = "capteur-bool-RF-CO2-3";
	protected static String SENSOR_CAPTEUR_BOOL_RJ = "capteur-bool-RJ";
	protected static String SENSOR_CAPTEUR_NB_TEMPS = "capteur-nb-temps";
	protected static String SENSOR_CAPTEUR_NB_VALIDER = "capteur-nb-valider";
	protected static String SENSOR_CAPTEUR_NB_EFFACER = "capteur-nb-effacer";
	protected static String SENSOR_CAPTEUR_NB_AIDE = "capteur-nb-aide";
	protected static String SENSOR_DOMAIN = "domain";
	protected static String SENSOR_GENERATOR = "generator";
	protected static String SENSOR_TASKFAMILY = "taskFamily";
	protected static String SENSOR_CORRECTANSWER = "correctAnswer";
	protected static String SENSOR_CODEERROR = "codeError";
	
	//Log object
	protected static String LOG = "log";
	protected static String LOG_TIME = "time";
	protected static String LOG_TYPE = "type";
	protected static String LOG_NAME = "name";
	protected static String LOG_ACTION = "action";
	
	//LRS object
	protected static String LRS_STATEMENT = "statements";

	public static String getTASK_ID() {
		return TASK_ID;
	}
}
