/**
 *
 */
package com.mocah.mindmath.datasimulation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mocah.mindmath.datasimulation.attributes.Answer;
import com.mocah.mindmath.datasimulation.attributes.ErrorCode;
import com.mocah.mindmath.datasimulation.attributes.Trigger;
import com.mocah.mindmath.datasimulation.attributes.constraints.between.AnswerConstraint;
import com.mocah.mindmath.datasimulation.attributes.constraints.between.GeneratorConstraint;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.AnswerEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ErrorCodeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TriggerEnum;
import com.mocah.mindmath.datasimulation.dataexports.DataExporter;
import com.mocah.mindmath.datasimulation.dataexports.GraphExporter;
import com.mocah.mindmath.datasimulation.json.SimulatedData;
import com.mocah.mindmath.datasimulation.json.SimulatedDataContainer;
import com.mocah.mindmath.datasimulation.json.SimulatedDataLearner;
import com.mocah.mindmath.datasimulation.profiles.AbstractProfile;
import com.mocah.mindmath.datasimulation.profiles.IProfile;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class MainSimulation {
	private static Random rand = new Random();

	/**
	 * @param args
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SimulatedDataContainer container = new SimulatedDataContainer();

		String finalQTable = null;

		int learnerIteration = 1;
		for (Entry<Class<? extends IProfile>, Integer> entry : AppConfig.learners.entrySet()) {
			Constructor<?> ctor = entry.getKey().getConstructor();

			int currentLearnerIteration = 1;
			while (currentLearnerIteration <= entry.getValue()) {
				AbstractProfile profile = (AbstractProfile) ctor.newInstance();
				profile.generateLearnerID();
				profile.initLearner();

				SimulatedDataLearner sdLearner = new SimulatedDataLearner(learnerIteration, profile,
						profile.getLearnerID());
				container.getDatasets().put(profile.getLearnerID(), sdLearner);

				int currentIteration = 1;
				FeedbackData feedbackData = null;
				while (currentIteration <= AppConfig.MAX_ITERATION) {
					CabriData data = new CabriData();
					data.setLearnerId(profile.getLearnerID());
					data.setDomain(profile.getDomain());
					data.setGenerator(profile.getGenerator());
					data.setTaskFamily(profile.getTaskFamily());
					data.setActivityMode(profile.getActivityMode());

					// Simulate a trigger
					Trigger trigger;
					if (rand.nextDouble() < profile.getAskHelpProb()) {
						// Ask help
						trigger = new Trigger(TriggerEnum.HELP);
					} else {
						// Validate exercise
						trigger = new Trigger(TriggerEnum.VALIDATE);
					}
					data.setTrigger(trigger);

					// Simulate a correct answer
					Answer correctAnswer;
					switch (trigger.getEnum().getThis()) {
					case HELP:
						correctAnswer = new Answer(AnswerEnum.NULL);
						break;

					default:
					case VALIDATE:
						if (rand.nextDouble() < profile.getSuccessProb()) {
							// Learner makes a correct answer
							correctAnswer = new Answer(AnswerEnum.TRUE);
						} else {
							// Learner makes an error
							correctAnswer = new Answer(AnswerEnum.FALSE);
						}
						break;

					}
					data.setCorrectAnswer(correctAnswer);

					// Simulate an error code
					ErrorCode errorCode;
					switch (correctAnswer.getEnum().getThis()) {
					case FALSE:
						if (rand.nextDouble() < AppConfig.RECON_ERROR_PROB) {
							// The error was detected

							Set<ErrorCodeEnum> answerError = AnswerConstraint.map
									.get(correctAnswer.getEnum().getThis());
							Set<ErrorCodeEnum> generatorError = GeneratorConstraint.map2
									.get(data.getGenerator().getEnum().getThis());

							Set<ErrorCodeEnum> errors = Sets.intersection(generatorError, answerError);

							int i = rand.nextInt(errors.size());

							errorCode = new ErrorCode(Iterables.get(errors, i));
						} else {
							// The error wasn't detected
							errorCode = new ErrorCode(ErrorCodeEnum.NULL);
						}
						break;

					case TRUE:
					case NULL:
					default:
						errorCode = new ErrorCode(ErrorCodeEnum.NULL);
						break;
					}

					data.setErrorCode(errorCode);

					String feedback = ServerConn.postData(data);
					System.out.println("Données envoyées : " + AppConfig.getGson().toJson(data));
					System.out.println("Feedback reçu : " + feedback);

					feedbackData = AppConfig.getGson().fromJson(feedback, FeedbackData.class);

					SimulatedData simData = new SimulatedData(currentIteration, data, feedbackData);
					simData.setExerciseSuccessProb(profile.getSuccessProb());
					simData.setActivityModeIncreaseSuccessProb(profile.getActivityModeIncreaseProb());
					sdLearner.getDataset().add(simData);

					int fdbkInfoWeight = 0;
					if (profile.isReadingFeedback() && feedbackData != null) {
						fdbkInfoWeight = AppConfig.getWeightInfo(feedbackData.getIdFeedback());
					}

					profile.calcNewSuccessProb(fdbkInfoWeight);

					// Learner had a correct answer, since it's a new exercise, recalc it's success
					// probability
					if (correctAnswer.getEnum().getThis() == AnswerEnum.TRUE) {
						profile.learnFromExercise();
					}

					currentIteration++;
				}

				String qtable = ServerConn.getQtable();
				sdLearner.setLearnerCSV(qtable);
				finalQTable = qtable;
				System.out.println(qtable);

				currentLearnerIteration++;
				learnerIteration++;
			}
		}

		container.setFinalCSV(finalQTable);

		DataExporter de = new DataExporter(container);
		de.export();
		GraphExporter ge = new GraphExporter(container);
		ge.export();
	}
}
