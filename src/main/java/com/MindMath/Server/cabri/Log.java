package com.MindMath.Server.cabri;

import java.io.Serializable;

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
public class Log implements Serializable{

	private static final long serialVersionUID = 6036741035696456506L;

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
