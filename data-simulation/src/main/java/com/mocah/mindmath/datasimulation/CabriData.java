/**
 *
 */
package com.mocah.mindmath.datasimulation;

import com.mocah.mindmath.datasimulation.attributes.ActivityMode;
import com.mocah.mindmath.datasimulation.attributes.Answer;
import com.mocah.mindmath.datasimulation.attributes.Domain;
import com.mocah.mindmath.datasimulation.attributes.ErrorCode;
import com.mocah.mindmath.datasimulation.attributes.TaskFamily;
import com.mocah.mindmath.datasimulation.attributes.Trigger;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ActivityModeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.AnswerEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ErrorCodeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TaskFamilyEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TriggerEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class CabriData {
	private String learnerId;
	private Domain domain;
	private String generator;
	private TaskFamily taskFamily;
	private Answer correctAnswer;
	private ErrorCode errorCode;
	private ActivityMode activityMode;
	private Trigger trigger;

	/**
	 *
	 */
	public CabriData() {
		// TODO Stub du constructeur généré automatiquement
	}

	/**
	 * @return the learnerId
	 */
	public String getLearnerId() {
		return learnerId;
	}

	/**
	 * @param learnerId the learnerId to define
	 */
	public void setLearnerId(String learnerId) {
		this.learnerId = learnerId;
	}

	/**
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to define
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @param domain the domain to define
	 */
	public void setDomain(DomainEnum domain) {
		this.domain = new Domain(domain);
	}

	/**
	 * @return the generator
	 */
	public String getGenerator() {
		return generator;
	}

	/**
	 * @param generator the generator to define
	 */
	public void setGenerator(String generator) {
		this.generator = generator;
	}

	/**
	 * @return the taskFamily
	 */
	public TaskFamily getTaskFamily() {
		return taskFamily;
	}

	/**
	 * @param taskFamily the taskFamily to define
	 */
	public void setTaskFamily(TaskFamily taskFamily) {
		this.taskFamily = taskFamily;
	}

	/**
	 * @param taskFamily the taskFamily to define
	 */
	public void setTaskFamily(TaskFamilyEnum taskFamily) {
		this.taskFamily = new TaskFamily(taskFamily);
	}

	/**
	 * @return the correctAnswer
	 */
	public Answer getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * @param correctAnswer the correctAnswer to define
	 */
	public void setCorrectAnswer(Answer correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	/**
	 * @param correctAnswer the correctAnswer to define
	 */
	public void setCorrectAnswer(AnswerEnum correctAnswer) {
		this.correctAnswer = new Answer(correctAnswer);
	}

	/**
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to define
	 */
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @param errorCode the errorCode to define
	 */
	public void setErrorCode(ErrorCodeEnum errorCode) {
		this.errorCode = new ErrorCode(errorCode);
	}

	/**
	 * @return the activityMode
	 */
	public ActivityMode getActivityMode() {
		return activityMode;
	}

	/**
	 * @param activityMode the activityMode to define
	 */
	public void setActivityMode(ActivityMode activityMode) {
		this.activityMode = activityMode;
	}

	/**
	 * @param activityMode the activityMode to define
	 */
	public void setActivityMode(ActivityModeEnum activityMode) {
		this.activityMode = new ActivityMode(activityMode);
	}

	/**
	 * @return the trigger
	 */
	public Trigger getTrigger() {
		return trigger;
	}

	/**
	 * @param trigger the trigger to define
	 */
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	/**
	 * @param trigger the trigger to define
	 */
	public void setTrigger(TriggerEnum trigger) {
		this.trigger = new Trigger(trigger);
	}
}
