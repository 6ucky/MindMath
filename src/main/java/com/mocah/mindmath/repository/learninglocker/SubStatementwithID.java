package com.mocah.mindmath.repository.learninglocker;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Verb;

public class SubStatementwithID implements IStatementObject {
	public static final String SUB_STATEMENT = "SubStatement";
	private String id;
	private String timestamp;
	private Verb verb;
	private Actor actor;
	private IStatementObject object;
	private Result result;
	private Context context;
	private ArrayList<Attachment> attachments;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public IStatementObject getObject() {
		return object;
	}

	public void setObject(IStatementObject object) {
		if (object.getObjectType().toLowerCase().equals("substatement")) {
			throw new IllegalArgumentException(
					"Sub-Statements cannot be nested");
		}
		this.object = object;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getObjectType() {
		return SUB_STATEMENT;
	}

    public JsonElement serialize() {
        JsonObject obj = new JsonObject();
        if (this.id != null) {
        	obj.addProperty("id", this.id);
        }
        if (this.timestamp != null) {
            obj.addProperty("timestamp", this.timestamp);
        }
        if (this.actor != null) {
            obj.add("actor", this.actor.serialize());
        }
        if (this.verb != null) {
            obj.add("verb", verb.serialize());
        }
        if (this.object != null) {
            obj.add("object", object.serialize());
        }
        if (this.result != null) {
            obj.add("result", result.serialize());
        }
        if (this.context != null) {
            obj.add("context", this.context.serialize());
        }
        obj.addProperty("objectType", this.getObjectType());
        JsonArray jsonAttachments = new JsonArray();
        obj.add("attachments", jsonAttachments);
        for (Attachment a : this.attachments) {
            jsonAttachments.add(a.serialize());
        }
        return obj;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
