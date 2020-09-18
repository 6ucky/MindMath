package com.mocah.mindmath.server.controller.feedback;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mocah.mindmath.server.entity.feedback.Feedbackjson;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentList;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
import com.mocah.mindmath.server.repository.LocalRoute;
import com.mocah.mindmath.server.repository.LocalRouteRepository;
import com.mocah.mindmath.server.repository.derby.Derbyrepository;

@RestController
@RequestMapping("/feedback")
public class Feedbackcontroller {

	private static final String license_num = "mocah";

	@Autowired
	private Derbyrepository feedbackcontentrepository;

	/**
	 * check the post request based on authorization
	 *
	 * @param auth the authorization parameter from headers
	 * @return authorized or unauthorized
	 */
	private static boolean checkauth(String auth) {
		if (auth.equals(license_num))
			return true;
		return false;
	}

	//Update feedbackcontent in Derby
	@PostMapping(path = "/feedbackcontent", consumes = "application/json")
	public ResponseEntity<String> addfeedbackrepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		getFeedbackcontentrepository().deleteAll(getFeedbackcontentrepository().getAllFeedbackContent());
		
		Gson gson = new Gson();
		List<FeedbackContent> feedbacks = gson.fromJson(data, FeedbackContentList.class).getFeedbackcontentlist();
		for (FeedbackContent feedback : feedbacks) {
			getFeedbackcontentrepository().save(feedback);
		}
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.OK);
	}

	//Update motivation in Derby
	@PostMapping(path = "/motivation", consumes = "application/json")
	public ResponseEntity<String> addmotivationrepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		
		getFeedbackcontentrepository().deleteAll(getFeedbackcontentrepository().getAllMotivation());
		
		Gson gson = new Gson();
		List<Motivation> motivations = gson.fromJson(data, FeedbackContentList.class).getMotivationlist();
		for (Motivation motivation : motivations) {
			getFeedbackcontentrepository().save(motivation);
		}
		return new ResponseEntity<>(gson.toJson(motivations), HttpStatus.OK);
	}

	//Update glossaire in Derby
	@PostMapping(path = "/glossaire", consumes = "application/json")
	public ResponseEntity<String> addglossairerepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		
		getFeedbackcontentrepository().deleteAll(getFeedbackcontentrepository().getAllGlossaire());
		
		Gson gson = new Gson();
		List<Glossaire> glossaires = gson.fromJson(data, FeedbackContentList.class).getGlossairelist();
		for (Glossaire glossaire : glossaires) {
			getFeedbackcontentrepository().save(glossaire);
		}
		return new ResponseEntity<>(gson.toJson(glossaires), HttpStatus.OK);
	}

	// a test for feedbackcontent database to feedbackjson
	@GetMapping(path = "/test/feedbackexample")
	public ResponseEntity<String> getFeedbackExample(@RequestHeader("Authorization") String auth)
			throws JsonSyntaxException, IOException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		getFeedbackcontentrepository().deleteAll(getFeedbackcontentrepository().getAllFeedbackContent());
		getFeedbackcontentrepository().deleteAll(getFeedbackcontentrepository().getAllGlossaire());
		getFeedbackcontentrepository().deleteAll(getFeedbackcontentrepository().getAllMotivation());
		addfeedbackrepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.FeedbackContentRoute));
		addmotivationrepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.MotivationRoute));
		addglossairerepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.GlossaireRoute));

		String feedbackID = "1.1.GNC.0"; // TEST name
		String leaf = "11";
		String error_code = "1";

		FeedbackContent fb = getFeedbackcontentrepository().getFeedbackContent(feedbackID, leaf);
		List<Motivation> motivations = getFeedbackcontentrepository().getMotivation(fb.getMotivation_leaf());
		HashMap<String, String> glossaireMap = new HashMap<>();
		for (int i = 0; i < fb.getContentErrorType(error_code).getGlossaire().size(); i++) {
			String mapkey = fb.getContentErrorType(error_code).getGlossaire().get(i);
			Glossaire temp = getFeedbackcontentrepository().getGlossaire(mapkey);
			glossaireMap.put(temp.getGlossaire_name(), temp.getGlossaire_content());
		}
		Feedbackjson feedbackjson = new Feedbackjson(
				motivations.get(new Random().nextInt(motivations.size())).getMotivation_data(),
				fb.getContentErrorType(error_code).getContent_url(), fb.getContentErrorType(error_code).getFormat(),
				glossaireMap);
		Gson gson = new Gson();
		return new ResponseEntity<>(gson.toJson(feedbackjson), HttpStatus.OK);
	}

	public Derbyrepository getFeedbackcontentrepository() {
		return feedbackcontentrepository;
	}

}
