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
		if (auth.equalsIgnoreCase(license_num))
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

	public Derbyrepository getFeedbackcontentrepository() {
		return feedbackcontentrepository;
	}

}
