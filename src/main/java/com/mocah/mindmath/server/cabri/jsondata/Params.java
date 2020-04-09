package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
public class Params implements Serializable{

	private static final long serialVersionUID = -6192516976316035400L;

	@Id
	private final String id;
	
	private final long VT_2_1;
	
	private final long VT_2_2;
	
	private final boolean VT_2_3;
	
	private final boolean VT_2_4;
	
	// empty object
	public Params() {
		this.VT_2_4 = false;
		this.VT_2_2 = -1;
		this.id = "";
		this.VT_2_3 = false;
		this.VT_2_1 = -1;
	}
	
	public Params(String id, long VT_2_1, long VT_2_2, boolean VT_2_3, boolean VT_2_4) {
		this.id = id;
		this.VT_2_1 = VT_2_1;
		this.VT_2_2 = VT_2_2;
		this.VT_2_3 = VT_2_3;
		this.VT_2_4 = VT_2_4;
	}

	public long getVT_2_1() {
		return VT_2_1;
	}

	public long getVT_2_2() {
		return VT_2_2;
	}

	public boolean isVT_2_3() {
		return VT_2_3;
	}

	public boolean isVT_2_4() {
		return VT_2_4;
	}

	public String getId() {
		return id;
	}

}
