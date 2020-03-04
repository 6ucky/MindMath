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
	private String id;
	
	private long VT_2_1;
	
	private long VT_2_2;
	
	private boolean VT_2_3;
	
	private boolean VT_2_4;

	public long getVT_2_1() {
		return VT_2_1;
	}

	public void setVT_2_1(long vT_2_1) {
		VT_2_1 = vT_2_1;
	}

	public long getVT_2_2() {
		return VT_2_2;
	}

	public void setVT_2_2(long vT_2_2) {
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

	public void setId(String id) {
		this.id = id;
	}

}
