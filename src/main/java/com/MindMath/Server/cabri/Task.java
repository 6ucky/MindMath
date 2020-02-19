package com.MindMath.Server.cabri;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Task {

	@Id
	private String id;
	private String task;
	private String trigger;
	private int VT_2_1;
	private int VT_2_2;
	private boolean VT_2_3;
	private boolean VT_2_4;
	
	protected Task() {}

    @JsonProperty("params")
    private void unpackNested(Map<String,Object> params) {
		this.setVT_2_1((int) params.get("VT-2.1"));
		this.setVT_2_2((int) params.get("VT-2.2"));
		this.setVT_2_3((boolean) params.get("VT-2.3"));
		this.setVT_2_4((boolean) params.get("VT-2.4"));
    }

	public Task(String task, String trigger, int VT_2_1, int VT_2_2, boolean VT_2_3, boolean VT_2_4) {
		this.setTask(task);
		this.setTrigger(trigger);
		this.setVT_2_1(VT_2_1);
		this.setVT_2_2(VT_2_2);
		this.setVT_2_3(VT_2_3);
		this.setVT_2_4(VT_2_4);
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public int getVT_2_1() {
		return VT_2_1;
	}

	public void setVT_2_1(int vT_2_1) {
		VT_2_1 = vT_2_1;
	}

	public int getVT_2_2() {
		return VT_2_2;
	}

	public void setVT_2_2(int vT_2_2) {
		VT_2_2 = vT_2_2;
	}

	public boolean isVT_2_3() {
		return VT_2_3;
	}

	public void setVT_2_3(boolean vT_2_3) {
		VT_2_3 = vT_2_3;
	}

	public boolean isVT_2_4() {
		return VT_2_4;
	}

	public void setVT_2_4(boolean vT_2_4) {
		VT_2_4 = vT_2_4;
	}

	public String getId() {
		return id;
	}
}
