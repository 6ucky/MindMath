package com.mocah.mindmath.server.cabri;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
@Table(name="params") 
public class Params implements Serializable{

	private static final long serialVersionUID = -6192516976316035400L;

	@Id
	private int VT_2_1;
	
	private int VT_2_2;
	
	private boolean VT_2_3;
	
	private boolean VT_2_4;

}
