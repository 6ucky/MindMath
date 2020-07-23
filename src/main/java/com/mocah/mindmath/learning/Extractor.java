/**
 *
 */
package com.mocah.mindmath.learning;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepositoryAggregation;
import com.mocah.mindmath.server.entity.task.Params;
import com.mocah.mindmath.server.entity.task.Sensors;
import com.mocah.mindmath.server.entity.task.Task;

import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verbs;

/**
 * @author Thibaut SIMON-FINE
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
	public static String getFromTask(Task task, String fieldName) throws NoSuchFieldException {
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
	public static String getFromSensors(Sensors sensors, String fieldName) throws NoSuchFieldException {
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
	public static String getFromParams(Params params, String fieldName) throws NoSuchFieldException {
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

	public static String getFromMethod(Task task, String methodName)
			throws NoSuchMethodException, InvocationTargetException {
		Object result = null;

		try {
			Method method = Extractor.class.getDeclaredMethod(methodName, Task.class);
			result = method.invoke(null, task);
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException("Try to execute inexisting method '" + methodName + "' in class '"
					+ Extractor.class
					+ "'. Please write the method and also note that method require to use have Task object as parameter.");
		} catch (InvocationTargetException e) {
			throw new InvocationTargetException(e.getCause(),
					"Error in execution of method '" + methodName + "' in class '" + Extractor.class + "'.");
		} catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
			// TODO Bloc catch généré automatiquement
			// Note:
			// IllegalArgumentException & IllegalAccessException & SecurityException
			// shouldn't be thrown since we always access to an existing class and field
			// owned by the class itself
			e.printStackTrace();
		}

		if (result != null)
			return result.toString();

		System.out.println(
				"WARNING : Null return from " + methodName + " for Task " + (new GsonBuilder()).create().toJson(task));
		return "";
	}

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
	 * Get from LRS the number of time the learner try to solve an exercice (based
	 * on the current one -> min result is "1")
	 *
	 * @param task
	 * @return
	 */
	protected static String getNbSolveTry(Task task) {
		LearningLockerRepositoryAggregation lrs = new LearningLockerRepositoryAggregation(task.useTestLRS());

		// Filter applied : same learner and same task family -> only answers (not help)
		HashMap<String, Object> scopes = new HashMap<>();
		scopes.put("learner_id", task.getSensors().getId_learner());
		scopes.put("verb_id", Verbs.answered().getId());
		scopes.put("family_task", task.getSensors().getTaskFamily());
		scopes.put("no_gaming", "true");

		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache_template/queryAVFt.mustache");
		try {
			mustache.execute(writer, scopes).flush();
			String query = writer.toString();
			lrs = lrs.filterByMatcher(query);
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		StatementResult results = lrs.getFilteredStatements();
		List<Statement> statements = results.getStatements();

		// Note: in default result, most recent is first
		ListIterator<Statement> listIterator = statements.listIterator();

		int nbTry = 1;
		while (listIterator.hasNext()) {
			Statement statement = listIterator.next();

			Context c = statement.getContext();
			HashMap<String, JsonElement> extensions = c.getExtensions();
			String answer = extensions.get("https://mindmath.lip6.fr/sensors").getAsJsonObject().get("correctAnswer")
					.getAsString();

			Boolean b = BooleanUtils.toBooleanObject(answer);

			if (b) {
				// Correct answer (we speculate it's the end of a previous exercice)
				// Don't count and break
				break;
			} else {
				// False answer (or none)
			}

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

		// "Stabilité de l'erreur-type élève: (Nbre de l'erreur-type) / (Nbre de fois où
		// l'erreur-type pouvait apparaître)"
		LearningLockerRepositoryAggregation lrs = new LearningLockerRepositoryAggregation(task.useTestLRS());

		// Filter applied : same learner and same task family -> only incorrect answers
		// (not help)
		HashMap<String, Object> scopes = new HashMap<>();
		scopes.put("learner_id", task.getSensors().getId_learner());
		scopes.put("verb_id", Verbs.answered().getId());
		scopes.put("family_task", task.getSensors().getTaskFamily());
		scopes.put("no_gaming", "true");
		scopes.put("correct_answer", "false");

		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache_template/queryAVFt.mustache");
		try {
			mustache.execute(writer, scopes).flush();
			String query = writer.toString();
			lrs = lrs.filterByMatcher(query);
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		StatementResult results = lrs.getFilteredStatements();
		List<Statement> statements = results.getStatements();

		if (statements.isEmpty())
			return "0";

		ListIterator<Statement> listIterator = statements.listIterator(statements.size());

		int countError = 0;
		while (listIterator.hasPrevious()) {
			Statement statement = listIterator.previous();

			Context c = statement.getContext();
			HashMap<String, JsonElement> extensions = c.getExtensions();

			String codeError = "";
			if (extensions.containsKey("https://mindmath.lip6.fr/sensors")) {
				JsonObject jsonExt = extensions.get("https://mindmath.lip6.fr/sensors").getAsJsonObject();

				if (jsonExt.has("codeError")) {
					codeError = extensions.get("https://mindmath.lip6.fr/sensors").getAsJsonObject().get("codeError")
							.getAsString();
				}
			}

			if (StringUtils.isNotEmpty(codeError)) {
				// Error observed
				if (currentError.equals(codeError)) {
					// Same errors
					countError += 1;
				}
			}
		}

		double freq = (double) countError / statements.size();

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

		// "Stabilité d'une erreur-type chez tous les élèves au sein d'une famille de
		// tâche: (Nbre de l'erreur-type) / (Nbre de fois où l'erreur-type pouvait
		// apparaître)"
		LearningLockerRepositoryAggregation lrs = new LearningLockerRepositoryAggregation(task.useTestLRS());

		// Filter applied : same task family -> only incorrect answers (not help)
		HashMap<String, Object> scopes = new HashMap<>();
		scopes.put("verb_id", Verbs.answered().getId());
		scopes.put("family_task", task.getSensors().getTaskFamily());
		scopes.put("no_gaming", "true");
		scopes.put("correct_answer", "false");

		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache_template/queryAVFt.mustache");
		try {
			mustache.execute(writer, scopes).flush();
			String query = writer.toString();
			lrs = lrs.filterByMatcher(query);
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		StatementResult results = lrs.getFilteredStatements();
		List<Statement> statements = results.getStatements();

		if (statements.isEmpty())
			return "0";

		ListIterator<Statement> listIterator = statements.listIterator();

		Map<String, Integer> countError = new HashMap<>();
		while (listIterator.hasNext()) {
			Statement statement = listIterator.next();

			Context c = statement.getContext();
			HashMap<String, JsonElement> extensions = c.getExtensions();

			String codeError = "";
			if (extensions.containsKey("https://mindmath.lip6.fr/sensors")) {
				JsonObject jsonExt = extensions.get("https://mindmath.lip6.fr/sensors").getAsJsonObject();

				if (jsonExt.has("codeError")) {
					codeError = extensions.get("https://mindmath.lip6.fr/sensors").getAsJsonObject().get("codeError")
							.getAsString();
				}
			}

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

		double freq = (double) max / statements.size();

		return freq + "";
	}
}
