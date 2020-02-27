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
@Table(name="params") 
public class Params implements Serializable{

	private static final long serialVersionUID = -6192516976316035400L;

	@Id
	@JsonProperty("VT-2.1")
	private int VT_2_1;
	
	@JsonProperty("VT-2.2")
	private int VT_2_2;
	
	@JsonProperty("VT-2.3")
	private boolean VT_2_3;
	
	@JsonProperty("VT-2.4")
	private boolean VT_2_4;

}
