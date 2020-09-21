package com.mocah.mindmath.server.entity.feedback;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.mocah.mindmath.server.entity.AbstractJsonData;
import com.mocah.mindmath.server.entity.task.Log;

/**
 * @author Yan Wang
 * @since 09/2020
 */

@Entity
public class TaskFeedback1_1 extends AbstractJsonData{
	
	private final String id_learner;
	
	private final String domain;

	private final String generator;

	private final String taskFamily;

	private final boolean correctAnswer;

	private final String codeError;

	private final String activityMode;
	
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Log> logs;
	
	private final String feedback_id;
	
	private final String leaf;
	
	private final String error_type;
	
	@Column(length=1024)
	private final String motivationalElementFb;
	
	@Column(length=1024)
	private final String contentFb;
	
	@Column(length=10000)
	private final String glossaryFb;
	
	private final String trigger;
	
	private final boolean success;
	
	private final boolean completion;
	
	public TaskFeedback1_1() {
		this(null, null, null, null, false, null, null, new ArrayList<Log>(), null, null, null, null, null, null, null, false, false);
	}

	public TaskFeedback1_1(String id_learner, String domain, String generator, String taskFamily,
			boolean correctAnswer, String codeError, String activityMode, List<Log> logs,
			String feedback_id, String leaf, String error_type, String motivationalElementFb,
			String contentFb, String glossaryFb, String trigger, boolean success, boolean completion) {
		super();
		this.id_learner = id_learner;
		this.domain = domain;
		this.generator = generator;
		this.taskFamily = taskFamily;
		this.correctAnswer = correctAnswer;
		this.codeError = codeError;
		this.activityMode = activityMode;
		this.logs = logs;
		this.feedback_id = feedback_id;
		this.leaf = leaf;
		this.error_type = error_type;
		this.motivationalElementFb = motivationalElementFb;
		this.contentFb = contentFb;
		this.glossaryFb = glossaryFb;
		this.trigger = trigger;
		this.success = success;
		this.completion = completion;
	}

	public String getId_learner() {
		return id_learner;
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

	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	public String getCodeError() {
		return codeError;
	}

	public String getActivityMode() {
		return activityMode;
	}

	public String getFeedback_id() {
		return feedback_id;
	}

	public String getLeaf() {
		return leaf;
	}

	public String getError_type() {
		return error_type;
	}

	public String getMotivationalElementFb() {
		return motivationalElementFb;
	}

	public String getContentFb() {
		return contentFb;
	}

	public String getGlossaryFb() {
		return glossaryFb;
	}

	public String getTrigger() {
		return trigger;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isCompletion() {
		return completion;
	}
}
