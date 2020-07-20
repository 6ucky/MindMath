/**
 *
 */
package com.mocah.mindmath.datasimulation;

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
import com.mocah.mindmath.datasimulation.json.DataExporter;
import com.mocah.mindmath.datasimulation.json.SimulatedData;
import com.mocah.mindmath.datasimulation.json.SimulatedDataContainer;
import com.mocah.mindmath.datasimulation.json.SimulatedDataLearner;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class MainSimulation {
	private static Random rand = new Random();

	private static String learnerId;
	private static double successProb;

	private static void initALearner() {
		learnerId = Config.getLearnerID();
		successProb = Config.BASE_SUCCESS_PROB;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimulatedDataContainer container = new SimulatedDataContainer();

		String finalQTable = null;
		int currentLearnerIteration = 1;
		while (currentLearnerIteration <= Config.NB_DIF_LEARNERS) {
			initALearner();
			SimulatedDataLearner sdLearner = new SimulatedDataLearner(currentLearnerIteration, learnerId);
			container.put(learnerId, sdLearner);

			int currentIteration = 1;
			FeedbackData feedbackData = null;
			while (currentIteration <= Config.MAX_ITERATION) {
				CabriData data = new CabriData();
				data.setLearnerId(learnerId);
				data.setDomain(Config.domain);
				data.setGenerator(Config.generator);
				data.setTaskFamily(Config.taskFamily);
				data.setActivityMode(Config.activityMode);

				// Simulate a trigger
				Trigger trigger;
				if (rand.nextDouble() < Config.ASK_HELP_PROB) {
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
					if (rand.nextDouble() < successProb) {
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
					if (rand.nextDouble() < Config.RECON_ERROR_PROB) {
						// The error was detected

						Set<ErrorCodeEnum> answerError = AnswerConstraint.map.get(correctAnswer.getEnum().getThis());
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
				System.out.println("Données envoyées : " + Config.getGson().toJson(data));
				System.out.println("Feedback reçu : " + feedback);

				feedbackData = Config.getGson().fromJson(feedback, FeedbackData.class);

				SimulatedData simData = new SimulatedData(currentIteration, data, feedbackData);
				sdLearner.add(simData);

				int fdbkInfoWeight = 0;
				if (Config.READ_FEEDBACK) {
					fdbkInfoWeight = Config.getWeightInfo(feedbackData.getIdFeedback());
				}

				successProb = Config.calcNewSuccessProb(successProb, fdbkInfoWeight);

				currentIteration++;
			}

			String qtable = ServerConn.getQtable();
			sdLearner.setLearnerCSV(qtable);
			finalQTable = qtable;
			System.out.println(qtable);

			currentLearnerIteration++;
		}

		container.setFinalCSV(finalQTable);

		DataExporter de = new DataExporter(container);
		de.export();
	}
}
