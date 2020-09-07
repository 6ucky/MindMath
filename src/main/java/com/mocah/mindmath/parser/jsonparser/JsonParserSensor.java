package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonObject;
import com.mocah.mindmath.server.entity.task.Sensors;

/**
 * @author Yan Wang
 * @since 10/04/2020
 */

public class JsonParserSensor extends JsonParserFactory {
	private final JsonObject sensorsObject;

	public JsonParserSensor(String data) {
		super(data);
		JsonObject emptyobject = new JsonObject();
		this.sensorsObject = rootObject.has(JsonParserKeys.SENSOR)
				? rootObject.get(JsonParserKeys.SENSOR).getAsJsonObject()
				: emptyobject;
	}

	@Override
	public JsonObject getObject() {
		return this.sensorsObject;
	}

	public Sensors getSensor() {
		Sensors sensorsClass = new Sensors();
		if (rootObject.has(JsonParserKeys.SENSOR)) {
			sensorsClass = new Sensors(getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RF_CO2_1),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RF_CO2_2),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RF_CO2_3),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RJ),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_TEMPS),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_VALIDER),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_EFFACER),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_AIDE),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_LEARNER_ID),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_DOMAIN),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_GENERATOR),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_TASKFAMILY),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CORRECTANSWER),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CODEERROR),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_ACTIVITYMODE),
					getValueforDB(sensorsObject, JsonParserKeys.SENSOR_SCORE));
		}

		return sensorsClass;
	}
}
