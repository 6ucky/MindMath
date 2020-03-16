package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
public class Sensors implements Serializable{

	private static final long serialVersionUID = 7791961059918284422L;

	@Id
	private final String id;
	
	private final boolean capteur_bool_RF_CO2_1;
	
	private final boolean capteur_bool_RF_CO2_2;
	
	private final boolean capteur_bool_RF_CO2_3;
	
	private final boolean capteur_bool_RJ;
	
	private final long capteur_nb_temps;
	
	private final long capteur_nb_valider;
	
	private final long capteur_nb_effacer;
	
	private final String capteur_nb_aide;
	
	public Sensors() {
		this.capteur_nb_valider = 0;
		this.id = "";
		this.capteur_bool_RF_CO2_2 = false;
		this.capteur_bool_RF_CO2_1 = false;
		this.capteur_nb_effacer = 0;
		this.capteur_nb_temps = 0;
		this.capteur_bool_RF_CO2_3 = false;
		this.capteur_nb_aide = "";
		this.capteur_bool_RJ = false;
	}
	
	public Sensors(String id, boolean capteur_bool_RF_CO2_1, boolean capteur_bool_RF_CO2_2, boolean capteur_bool_RF_CO2_3,
			boolean capteur_bool_RJ, long capteur_nb_temps, long capteur_nb_valider, long capteur_nb_effacer, String capteur_nb_aide) {
		this.id = id;
		this.capteur_bool_RF_CO2_1 = capteur_bool_RF_CO2_1;
		this.capteur_bool_RF_CO2_2 = capteur_bool_RF_CO2_2;
		this.capteur_bool_RF_CO2_3 = capteur_bool_RF_CO2_3;
		this.capteur_bool_RJ = capteur_bool_RJ;
		this.capteur_nb_temps = capteur_nb_temps;
		this.capteur_nb_valider = capteur_nb_valider;
		this.capteur_nb_effacer = capteur_nb_effacer;
		this.capteur_nb_aide = capteur_nb_aide;
	}

	public boolean isCapteur_bool_RF_CO2_1() {
		return capteur_bool_RF_CO2_1;
	}

	public boolean isCapteur_bool_RF_CO2_2() {
		return capteur_bool_RF_CO2_2;
	}

	public boolean isCapteur_bool_RF_CO2_3() {
		return capteur_bool_RF_CO2_3;
	}

	public boolean isCapteur_bool_RJ() {
		return capteur_bool_RJ;
	}

	public long getCapteur_nb_temps() {
		return capteur_nb_temps;
	}

	public long getCapteur_nb_valider() {
		return capteur_nb_valider;
	}

	public long getCapteur_nb_effacer() {
		return capteur_nb_effacer;
	}

	public String getCapteur_nb_aide() {
		return capteur_nb_aide;
	}

	public String getId() {
		return id;
	}
}
