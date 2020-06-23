package com.mocah.mindmath.repository.learninglocker.jxapi;
import com.google.gson.*;

public interface IStatementObject {
	public String getObjectType();
	public JsonElement serialize();
}
