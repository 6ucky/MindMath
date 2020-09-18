/**
 *
 */
package com.mocah.mindmath.datasimulation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.mocah.mindmath.PostCabriJsonTest;
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
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.server.ServerApplication;
import com.mocah.mindmath.server.controller.cabri.CabriVersion;
import com.mocah.mindmath.server.controller.cabri.Taskcontroller;

import gov.adlnet.xapi.model.Verbs;

/**
 * @author Thibaut SIMON-FINE
 *
 */
@SpringBootTest(classes=ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
public class MainSimulationTest {

	@LocalServerPort
	private int port;

	//inject restTemplate bean
	@Autowired
	private TestRestTemplate restTemplate;
		
	private static Random rand = new Random();

	@Test
	public void postCabriSimulation() throws Exception {
		int learnerIteration = main(AppConfig.version);
		int sum = 0;
		for(Class<? extends IProfile> profile: AppConfig.learners.keySet())
		{
			sum += AppConfig.learners.get(profile);
		}
		assertThat(learnerIteration).isEqualTo(sum+1);
	}
	/**
	 * @param args
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public int main(CabriVersion version) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SimulatedDataContainer container = new SimulatedDataContainer();

		String posttask_url = "";
		String getqvalue_url = "";
		switch(version)
		{
		case v1_0:
			posttask_url = "http://localhost:" + port + "/task/v1.0";
			getqvalue_url = "http://localhost:" + port + "/learning/qvalues";
			break;
		case v1_1:
			posttask_url = "http://localhost:" + port + "/task/v1.1";
			getqvalue_url = "http://localhost:" + port + "/learning/qvalues";
			break;
		}
		String finalQTable = null;

		List<Class<? extends IProfile>> learners = new ArrayList<>();
		for (Entry<Class<? extends IProfile>, Integer> entry : AppConfig.learners.entrySet()) {
			int i = 1;
			while (i <= entry.getValue()) {
				learners.add(entry.getKey());
				i++;
			}
		}

		if (AppConfig.useRandomProfileOrder) {
			Collections.shuffle(learners);
		}

		System.out.println("Profile generation order:");
		System.out.println(learners);

		int learnerIteration = 1;
		for (Class<? extends IProfile> profileClass : learners) {
			Constructor<?> ctor = profileClass.getConstructor();

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
				HttpEntity<String> entity = new HttpEntity<>(AppConfig.getGson().toJson(data), getHeader());
				ResponseEntity<String> response = restTemplate.exchange(
						posttask_url, 
						HttpMethod.POST, 
						entity, 
						String.class);
				String feedback = response.getBody();
				
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
				System.out.println("Learner " + learnerIteration + "/" + learners.size() + " iteration "
						+ currentIteration + "/" + AppConfig.MAX_ITERATION);
			}

			HttpEntity<String> entity_qvalues = new HttpEntity<>("", getHeader());
			ResponseEntity<String> response_qvalues = restTemplate.exchange(
					getqvalue_url, 
					HttpMethod.GET, 
					entity_qvalues, 
					String.class);
			String qtable = response_qvalues.getBody();
			
			sdLearner.setLearnerCSV(qtable);
			finalQTable = qtable;
			System.out.println(qtable);

			learnerIteration++;
		}

		container.setFinalCSV(finalQTable);

		DataExporter de = new DataExporter(container);
		de.export();
		GraphExporter ge = new GraphExporter(container);
		ge.export();
		return learnerIteration;
	}
	
	public HttpHeaders getHeader() {
		HttpHeaders headers = new HttpHeaders();
		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "mocah");
		headers.add("Version-LIP6", "1.0");
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
