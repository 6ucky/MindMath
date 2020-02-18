package com.MindMath.Server.cabri;

public class Task {

	private final long id;
	private final String name;
	private final String trigger;

	public Task(long id, String name, String trigger) {
		this.id = id;
		this.name = name;
		this.trigger = trigger;
	}
	
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getTrigger() {
		return trigger;
	}
	
}
