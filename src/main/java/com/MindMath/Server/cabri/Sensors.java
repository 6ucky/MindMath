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
@Table(name="sensors") 
public class Sensors {

	@Id
	@JsonProperty("capteur-bool-RF-CO2-1")
	private boolean capteur_bool_RF_CO2_1;
	
	@JsonProperty("capteur-bool-RF-CO2-2")
	private boolean capteur_bool_RF_CO2_2;
	
	@JsonProperty("capteur-bool-RF-CO2-3")
	private boolean capteur_bool_RF_CO2_3;
	
	@JsonProperty("capteur-bool-RJ")
	private boolean capteur_bool_RJ;
	
	@JsonProperty("capteur-nb-temps")
	private int capteur_nb_temps;
	
	@JsonProperty("capteur-nb-valider")
	private int capteur_nb_valider;
	
	@JsonProperty("capteur-nb-effacer")
	private int capteur_nb_effacer;
	
	@JsonProperty("capteur-nb-aide")
	private String capteur_nb_aide;
}
