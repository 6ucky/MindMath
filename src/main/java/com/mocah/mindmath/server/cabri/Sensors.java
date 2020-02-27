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
@Table(name="sensors") 
public class Sensors implements Serializable{

	private static final long serialVersionUID = 7791961059918284422L;

	@Id
	private boolean capteur_bool_RF_CO2_1;
	
	private boolean capteur_bool_RF_CO2_2;
	
	private boolean capteur_bool_RF_CO2_3;
	
	private boolean capteur_bool_RJ;
	
	private int capteur_nb_temps;
	
	private int capteur_nb_valider;
	
	private int capteur_nb_effacer;
	
	private String capteur_nb_aide;
}
