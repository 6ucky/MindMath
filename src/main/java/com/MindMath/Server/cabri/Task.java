package com.MindMath.Server.cabri;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

	private long id;
	private String task;
	private String trigger;
	private int VT_2_1;
	private int VT_2_2;
	private boolean VT_2_3;
	private boolean VT_2_4;

    @JsonProperty("params")
    private void unpackNested(Map<String,Object> params) {
		this.VT_2_1 = (int) params.get("VT-2.1");
		this.VT_2_2 = (int) params.get("VT-2.2");
		this.VT_2_3 = (boolean) params.get("VT-2.3");
		this.VT_2_4 = (boolean) params.get("VT-2.4");
    }

	public Task(long id, String task, String trigger) {
		this.id = id;
		this.task = task;
		this.trigger = trigger;
	}
	
	public long getId() {
		return id;
	}
	
	public String getTask() {
		return task;
	}

	public String getTrigger() {
		return trigger;
	}

	public float getVT_2_1() {
		return VT_2_1;
	}

	public float getVT_2_2() {
		return VT_2_2;
	}

	public boolean isVT_2_3() {
		return VT_2_3;
	}

	public boolean isVT_2_4() {
		return VT_2_4;
	}
}
