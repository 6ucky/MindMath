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
@Table(name="logs") 
public class Log implements Serializable{

	private static final long serialVersionUID = 6036741035696456506L;

	@Id
	private int time;
	
	private String type;
	
	private String name;
	
	private String action;

}
