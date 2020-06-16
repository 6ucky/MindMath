/**
 *
 */
package com.mocah.mindmath.learning;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Params;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;
import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author tsimo
 *
 *         https://www.baeldung.com/java-method-reflection
 *
 */
public class Extractor {

	/**
	 * @param task
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static String getFromTask(Task task, String fieldName) throws NoSuchFieldException, SecurityException {
		String r = null;

		r = (String) task.getFieldValue(fieldName);

		return r;
	}

	/**
	 * @param sensors
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static String getFromSensors(Sensors sensors, String fieldName)
			throws NoSuchFieldException, SecurityException {
		String r = null;

		r = (String) sensors.getFieldValue(fieldName);

		return r;
	}

	/**
	 * @param params
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static String getFromParams(Params params, String fieldName) throws NoSuchFieldException, SecurityException {
		String r = null;

		r = (String) params.getFieldValue(fieldName);

		return r;
	}

//	public static String getFromLogs(List<Log> logs, String key) {
//		return getFromLogs(logs, key, false);
//	}
//
//	public static String getFromLogs(List<Log> logs, String key, boolean reversed) {
//		String r = null;
//
//		for (Log log : logs) {
//			if (log.getType().equals(key)) {
//				r = log.getName();
//				break;
//			}
//		}
//
//		return r;
//	}

	public static String getFromMethod(Task task, String name) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = Extractor.class.getDeclaredMethod(name, Task.class);

		Object result = method.invoke(null, task);

		return result.toString();
	}

	/**
	 * Get the trigger of the feedback request
	 *
	 * @param task
	 * @return
	 */
	protected static String getTrigger(Task task) {
		List<Log> logs = task.getLog();

		Collections.reverse(logs);

		for (Log log : logs) {
			if (log.getType().equals("button")) {
				if (log.getName().equals("bouton-valider") || log.getName().equals("bouton-aide"))
					return log.getName();
			}
		}

		return null;
	}

	/**
	 * Get from LRS the number of time the learner try to solve an exercice
	 *
	 * @param task
	 * @return
	 */
	protected static String getNbSolveTry(Task task) {
		// TODO

		return "2";
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
		else {
			// TODO check LRS
			// Si par contre, le champs codeErreur est null, on regardera les données reçues
			// des autres élèves (sur la même famille de tâches) pour extraire l'erreur la
			// plus fréquente
		}

		return "false";
	}

}
