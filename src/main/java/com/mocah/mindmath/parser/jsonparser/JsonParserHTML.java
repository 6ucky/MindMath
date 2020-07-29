package com.mocah.mindmath.parser.jsonparser;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.ParserFactory;

/**
 *
 * @author Yan Wang
 * @deprecated HTML parser is currently not used.
 *
 */
@Deprecated
public class JsonParserHTML implements ParserFactory<String> {

	private String html;

	public JsonParserHTML() {
		html = "";
	}

	public static boolean isGoodJson(String json) {
		try {
			JsonParser.parseString(json);
			return true;
		} catch (JsonParseException e) {
			return false;
		}
	}

	public void json2html(JsonElement json) {
		if (json.isJsonArray()) {
			JsonArray jArray = json.getAsJsonArray();
			Iterator<JsonElement> it = jArray.iterator();
			html += "<table class='tableList'>";
			int f = 0;
			while (it.hasNext()) {
				JsonElement jsonElement = it.next();
				if (f == 0) {
					html += "<thead>";
					jsonGetHead(jsonElement);
					html += "</thead><tbody>";
				}
				html += "<tr>";
				jsonGetBody(jsonElement);
				html += "</tr>";
				f++;
			}
			html += "</tbody>";
			html += "</table>";
		} else if (json.isJsonObject()) {
			JsonObject jObject = json.getAsJsonObject();
			Set<Entry<String, JsonElement>> entrySet = jObject.entrySet();
			Iterator<Entry<String, JsonElement>> iter = entrySet.iterator();
			while (iter.hasNext()) {
//		    	htmlBegin += "<td>";
//		    	htmlEnd = "</td>" + htmlEnd;
				Entry<String, JsonElement> entry = iter.next();
				String key = entry.getKey();
				html += key;
				html += "=";
				JsonElement value = entry.getValue();
				json2html(value);
			}
		} else if (json.isJsonPrimitive()) {
			String finals = json.getAsString();
			html += finals;
		} else if (json.isJsonNull()) {
		}
	}

	private void jsonGetHead(JsonElement json) {
		JsonObject jObject = json.getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = jObject.entrySet();
		Iterator<Entry<String, JsonElement>> iter = entrySet.iterator();
		while (iter.hasNext()) {
			Entry<String, JsonElement> entry = iter.next();
			String key = entry.getKey();
			html += "<td>" + key + "</td>";
		}
	}

	private void jsonGetBody(JsonElement json) {
		JsonObject jObject = json.getAsJsonObject();
		Set<Entry<String, JsonElement>> entrySet = jObject.entrySet();
		Iterator<Entry<String, JsonElement>> iter = entrySet.iterator();
		while (iter.hasNext()) {
			Entry<String, JsonElement> entry = iter.next();
			html += "<td>";
			JsonElement value = entry.getValue();
			json2html(value);
			html += "</td>";
		}
	}

	@Override
	public String parse(String data, CabriVersion version) throws JsonParserCustomException {
		if (isGoodJson(data)) {
			json2html(JsonParser.parseString(data).getAsJsonObject());
		} else {
			html += "<br>";
			html += data;
			html += "</br>";
		}
		return html;
	}
}
