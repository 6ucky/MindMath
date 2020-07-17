package com.mocah.mindmath.datasimulation.json;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mocah.mindmath.datasimulation.CabriData;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class CabriDataSerializer implements JsonSerializer<CabriData>, JsonDeserializer<CabriData> {

	@Override
	public JsonElement serialize(CabriData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = new JsonObject();

		root.addProperty("id", src.getLearnerId());

		JsonObject sensors = new JsonObject();
		root.add("sensors", sensors);
		sensors.addProperty("idLearner", src.getLearnerId());
		sensors.addProperty("domain", src.getDomain().getValue());
		sensors.addProperty("generator", src.getGenerator().getValue());
		sensors.addProperty("taskFamily", src.getTaskFamily().getValue());
		sensors.addProperty("correctAnswer", src.getCorrectAnswer().getValue());
		// TODO intermediaryResponse?
		sensors.addProperty("codeError", src.getErrorCode().getValue());
		sensors.addProperty("activityMode", src.getActivityMode().getValue());

		JsonArray logs = new JsonArray();
		root.add("logs", logs);
		JsonObject triggerLog = new JsonObject();
		logs.add(triggerLog);
		triggerLog.addProperty("time", 4000);
		triggerLog.addProperty("type", "button");
		triggerLog.addProperty("name", src.getTrigger().getValue());
		triggerLog.addProperty("action", "push");

		return root;
	}

	@Override
	public CabriData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}
}
