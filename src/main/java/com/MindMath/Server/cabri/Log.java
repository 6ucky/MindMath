package com.MindMath.Server.cabri;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
@Table(name="logs") 
public class Log {

	@Id
	@JsonProperty("time")
	private int time;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("action")
	private String action;

}
