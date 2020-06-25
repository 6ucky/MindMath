package com.mocah.mindmath.repository.jxapi;
import com.google.gson.*;

public interface IStatementObject {
	public String getObjectType();
	public JsonElement serialize();
}
