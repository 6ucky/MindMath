package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
public class Params implements Serializable{

	private static final long serialVersionUID = -6192516976316035400L;

	@Id
	private String id;
	
	private final String VT_2_1;
	
	private final String VT_2_2;
	
	private final String VT_2_3;
	
	private final String VT_2_4;
	
	// empty object
	public Params() {
		this.id = null;
		this.VT_2_4 = null;
		this.VT_2_2 = null;
		this.VT_2_3 = null;
		this.VT_2_1 = null;
	}
	
	public Params(String id) {
		this();
		this.id = id;
	}
	
	public Params(String id, String VT_2_1, String VT_2_2, String VT_2_3, String VT_2_4) {
		this.id = id;
		this.VT_2_1 = VT_2_1;
		this.VT_2_2 = VT_2_2;
		this.VT_2_3 = VT_2_3;
		this.VT_2_4 = VT_2_4;
	}

	public String getVT_2_1() {
		return VT_2_1;
	}

	public String getVT_2_2() {
		return VT_2_2;
	}

	public String isVT_2_3() {
		return VT_2_3;
	}

	public String isVT_2_4() {
		return VT_2_4;
	}

	public String getId() {
		return id;
	}

}
