package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Sensors extends AbstractJsonData implements Serializable {

	private static final long serialVersionUID = 7791961059918284422L;

	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE,generator="sensors_id")  
	@SequenceGenerator(name="sensors_id", sequenceName="sensors", initialValue = 1, allocationSize = 1)
	private long id;

	private final String capteur_bool_RF_CO2_1;

	private final String capteur_bool_RF_CO2_2;

	private final String capteur_bool_RF_CO2_3;

	private final String capteur_bool_RJ;

	private final String capteur_nb_temps;

	private final String capteur_nb_valider;

	private final String capteur_nb_effacer;

	private final String capteur_nb_aide;

	private final String domain;

	private final String generator;

	private final String taskFamily;

	private final String correctAnswer;

	private final String codeError;
	
	private final String activityMode;

	// empty object
	public Sensors() {
		super();
		this.capteur_bool_RF_CO2_2 = null;
		this.capteur_bool_RF_CO2_1 = null;
		this.capteur_nb_effacer = null;
		this.capteur_nb_temps = null;
		this.capteur_bool_RF_CO2_3 = null;
		this.capteur_nb_aide = null;
		this.capteur_bool_RJ = null;
		this.domain = null;
		this.generator = null;
		this.taskFamily = null;
		this.correctAnswer = null;
		this.codeError = null;
		this.capteur_nb_valider = null;
		this.activityMode = null;
	}

	public Sensors(String capteur_bool_RF_CO2_1, String capteur_bool_RF_CO2_2, String capteur_bool_RF_CO2_3,
			String capteur_bool_RJ, String capteur_nb_temps, String capteur_nb_valider, String capteur_nb_effacer,
			String capteur_nb_aide, String domain, String generator, String taskFamily, String correctAnswer,
			String codeError, String activityMode) {
		super();
		this.capteur_bool_RF_CO2_1 = capteur_bool_RF_CO2_1;
		this.capteur_bool_RF_CO2_2 = capteur_bool_RF_CO2_2;
		this.capteur_bool_RF_CO2_3 = capteur_bool_RF_CO2_3;
		this.capteur_bool_RJ = capteur_bool_RJ;
		this.capteur_nb_temps = capteur_nb_temps;
		this.capteur_nb_valider = capteur_nb_valider;
		this.capteur_nb_effacer = capteur_nb_effacer;
		this.capteur_nb_aide = capteur_nb_aide;
		this.domain = domain;
		this.generator = generator;
		this.taskFamily = taskFamily;
		this.correctAnswer = correctAnswer;
		this.codeError = codeError;
		this.activityMode = activityMode;
	}

	public String isCapteur_bool_RF_CO2_1() {
		return capteur_bool_RF_CO2_1;
	}

	public String isCapteur_bool_RF_CO2_2() {
		return capteur_bool_RF_CO2_2;
	}

	public String isCapteur_bool_RF_CO2_3() {
		return capteur_bool_RF_CO2_3;
	}

	public String isCapteur_bool_RJ() {
		return capteur_bool_RJ;
	}

	public String getCapteur_nb_temps() {
		return capteur_nb_temps;
	}

	public String getCapteur_nb_valider() {
		return capteur_nb_valider;
	}

	public String getCapteur_nb_effacer() {
		return capteur_nb_effacer;
	}

	public String getCapteur_nb_aide() {
		return capteur_nb_aide;
	}

	public long getId() {
		return id;
	}

	public String getDomain() {
		return domain;
	}

	public String getGenerator() {
		return generator;
	}

	public String getTaskFamily() {
		return taskFamily;
	}

	public String isCorrectAnswer() {
		return correctAnswer;
	}

	public String getCodeError() {
		return codeError;
	}

	public String getActivityMode() {
		return activityMode;
	}
}
