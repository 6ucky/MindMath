package com.mocah.mindmath.learning;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.mocah.mindmath.server.entity.feedback.TaskFeedback1_1;
import com.mocah.mindmath.server.entity.task.Sensors;
import com.mocah.mindmath.server.entity.task.Task;

import gov.adlnet.xapi.model.Verbs;

public class ExtractorDerby {
	
	/**
	 * Get the trigger of the feedback request
	 *
	 * @param task
	 * @return
	 */
	protected static String getTrigger(Task task) {
		return task.getTrigger();
	}

	/**
	 * Get from Derby the number of time the learner try to solve an exercise (based
	 * on the current one -> min result is "1")
	 *
	 * @param task
	 * @return
	 */
	protected static String getNbSolveTry(Task task) {
		
		List<TaskFeedback1_1> TaskFBs = LearningProcess.getRepo().getTaskFeedback1_1(task.getSensors().getId_learner(), task.getSensors().getTaskFamily(), Verbs.answered().getId(), true);
		
		int nbTry = 1;
		for(TaskFeedback1_1 taskfb : TaskFBs)
		{
			if(taskfb.isCorrectAnswer())
				break;
			nbTry++;
		}
		return "" + nbTry;
	}

	/**
	 * Check if the error is identified
	 *
	 * @param task
	 * @return
	 */
	protected static String isErrorIdentified(Task task) {
		String codeError = task.getSensors().getCodeError();

		if (codeError != null && !codeError.equalsIgnoreCase("null"))
			return "true";

		return "false";
	}

	/**
	 * Return the actual learner error frequency for the current error in task <br>
	 * Note: only for errors of the same task familly of object {@code Task}
	 *
	 * @param task
	 * @return
	 */
	protected static String errorStabilityForLearner(Task task) {
		Sensors currentSensors = task.getSensors();
		if (currentSensors == null)
			return "0";

		String currentError = currentSensors.getCodeError();
		if (StringUtils.isEmpty(currentError))
			return "0";

		List<TaskFeedback1_1> TaskFBs = LearningProcess.getRepo().getTaskFeedback1_1(task.getSensors().getId_learner(), task.getSensors().getTaskFamily(), Verbs.answered().getId(), true, false);

		if (TaskFBs.size() == 0)
			return "0";

		ListIterator<TaskFeedback1_1> listIterator = TaskFBs.listIterator(TaskFBs.size());

		int countError = 0;
		while (listIterator.hasPrevious()) {
			TaskFeedback1_1 taskfb = listIterator.previous();

			String codeError = taskfb.getCodeError();

			if (StringUtils.isNotEmpty(codeError)) {
				// Error observed
				if (currentError.equals(codeError)) {
					// Same errors
					countError += 1;
				}
			}
		}

		double freq = (double) countError / TaskFBs.size();

		return freq + "";
	}

	/**
	 * Return the error's highest frequency for all learner <br>
	 * Note: only for errors of the same task familly of object {@code Task}
	 *
	 * @param task
	 * @return
	 */
	protected static String mostStabErrorForLearners(Task task) {
		Sensors currentSensors = task.getSensors();
		if (currentSensors == null)
			return "0";

		List<TaskFeedback1_1> TaskFBs = LearningProcess.getRepo().getTaskFeedback1_1(task.getSensors().getTaskFamily(), Verbs.answered().getId(), true, false);

		if (TaskFBs.size() == 0)
			return "0";

		Map<String, Integer> countError = new HashMap<>();
		for(TaskFeedback1_1 taskfb : TaskFBs){
			String codeError = taskfb.getCodeError();
			if (StringUtils.isNotEmpty(codeError)) {
				// Error observed
				if (countError.containsKey(codeError)) {
					countError.put(codeError, countError.get(codeError) + 1);
				} else {
					countError.put(codeError, 1);
				}
			}
		}

		int max = 0;
		for (Entry<String, Integer> error : countError.entrySet()) {
			if (error.getValue() > max) {
				max = error.getValue();
			}
		}

		double freq = (double) max / TaskFBs.size();

		return freq + "";
	}
}
