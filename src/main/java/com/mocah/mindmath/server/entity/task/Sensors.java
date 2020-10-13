package com.mocah.mindmath.server.entity.task;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.google.gson.annotations.Expose;
import com.mocah.mindmath.server.entity.AbstractJsonData;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Sensors extends AbstractJsonData implements Serializable {

	private static final long serialVersionUID = 7791961059918284422L;

	@Column(insertable = false)
	private final String capteur_bool_RF_CO2_1;

	@Column(insertable = false)
	private final String capteur_bool_RF_CO2_2;

	@Column(insertable = false)
	private final String capteur_bool_RF_CO2_3;

	@Column(insertable = false)
	private final String capteur_bool_RJ;

	@Column(insertable = false)
	private final String capteur_nb_temps;

	@Column(insertable = false)
	private final String capteur_nb_valider;

	@Column(insertable = false)
	private final String capteur_nb_effacer;

	@Column(insertable = false)
	private final String capteur_nb_aide;
	
	//TODO UID parcours
	private String id_Task;
	
	private final String id_learner;

	@Expose (serialize = true) 
	private final String domain;

	@Expose (serialize = true) 
	private final String generator;

	@Expose (serialize = true) 
	private final String taskFamily;

	@Expose (serialize = true) 
	private final String correctAnswer;

	@Expose (serialize = true) 
	private String codeError;
	
	@Expose (serialize = true) 
	private String activityMode;
	
	@Expose (serialize = true) 
	private String maxFb;

	// empty object
	public Sensors() {
		this(null, null, null, null, null, null, null, null, "", "", null, null, null, null, null, null, null);
	}
	
	// version 1.1
	public Sensors(String id_Task, String idLearner, String domain, String generator, String taskFamily, 
			String correctAnswer, String codeError, String activityMode, String maxFb) {
		this(null, null, null, null, null, null, null, null, id_Task, idLearner, domain, generator, taskFamily, correctAnswer, codeError, activityMode, maxFb);
	}

	public Sensors(String capteur_bool_RF_CO2_1, String capteur_bool_RF_CO2_2, String capteur_bool_RF_CO2_3,
			String capteur_bool_RJ, String capteur_nb_temps, String capteur_nb_valider, String capteur_nb_effacer,
			String capteur_nb_aide, String id_Task, String idLearner, String domain, String generator, String taskFamily, 
			String correctAnswer, String codeError, String activityMode, String maxFb) {
		super();
		this.capteur_bool_RF_CO2_1 = capteur_bool_RF_CO2_1;
		this.capteur_bool_RF_CO2_2 = capteur_bool_RF_CO2_2;
		this.capteur_bool_RF_CO2_3 = capteur_bool_RF_CO2_3;
		this.capteur_bool_RJ = capteur_bool_RJ;
		this.capteur_nb_temps = capteur_nb_temps;
		this.capteur_nb_valider = capteur_nb_valider;
		this.capteur_nb_effacer = capteur_nb_effacer;
		this.capteur_nb_aide = capteur_nb_aide;
		this.id_Task = id_Task;
		this.id_learner = idLearner;
		this.domain = domain;
		this.generator = generator;
		this.taskFamily = taskFamily;
		this.correctAnswer = correctAnswer;
		this.setCodeError(codeError);
		this.activityMode = activityMode;
		this.setMaxFb(maxFb);
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

	public String getActivityMode() {
		return activityMode;
	}

	public String getId_learner() {
		return id_learner;
	}

	public String getId_Task() {
		return id_Task;
	}
	
	public void setId_Task(String id_task) {
		this.id_Task = id_task;
	}
	
	public void setActivityMode(String activityMode) {
		this.activityMode = activityMode;
	}

	public String getMaxFb() {
		return maxFb;
	}

	public void setMaxFb(String maxFb) {
		this.maxFb = maxFb;
	}

	public String getCodeError() {
		return codeError;
	}

	public void setCodeError(String codeError) {
		this.codeError = codeError;
	}
}
