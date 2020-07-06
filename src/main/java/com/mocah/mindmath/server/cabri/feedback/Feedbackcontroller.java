package com.mocah.mindmath.server.cabri.feedback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mocah.mindmath.repository.LocalRoute;
import com.mocah.mindmath.repository.LocalRouteRepository;
import com.mocah.mindmath.server.Derbyrepository;
import com.mocah.mindmath.server.entity.feedbackContent.ContentErrorType;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentList;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;

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
	
	@PostMapping(path = "/feedbackcontent", consumes = "application/json")
	public ResponseEntity<String> addfeedbackrepo(@RequestHeader("Authorization") String auth, @RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		Gson gson = new Gson();
		List<FeedbackContent> feedbacks = gson.fromJson(data, FeedbackContentList.class).getFeedbackcontentlist();
		for(FeedbackContent feedback : feedbacks)
		{
			getFeedbackcontentrepository().save(feedback);
		}
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.OK);
	}

	@PostMapping(path = "/motivation", consumes = "application/json")
	public ResponseEntity<String> addmotivationrepo(@RequestHeader("Authorization") String auth, @RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		Gson gson = new Gson();
		List<Motivation> motivations = gson.fromJson(data, FeedbackContentList.class).getMotivationlist();
		for(Motivation motivation : motivations)
		{
			getFeedbackcontentrepository().save(motivation);
		}
		return new ResponseEntity<>(gson.toJson(motivations), HttpStatus.OK);
	}
	
	@PostMapping(path = "/glossaire", consumes = "application/json")
	public ResponseEntity<String> addglossairerepo(@RequestHeader("Authorization") String auth, @RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		Gson gson = new Gson();
		List<Glossaire> glossaires = gson.fromJson(data, FeedbackContentList.class).getGlossairelist();
		for(Glossaire glossaire : glossaires)
		{
			getFeedbackcontentrepository().save(glossaire);
		}
		return new ResponseEntity<>(gson.toJson(glossaires), HttpStatus.OK);
	}
	
	@GetMapping(path = "/example")
	public ResponseEntity<String> getExample(@RequestHeader("Authorization") String auth) throws JsonSyntaxException, IOException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		addfeedbackrepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.FeedbackContentRoute));
		addmotivationrepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.MotivationRoute));
		addglossairerepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.GlossaireRoute));
		
		String feedbackName = "3.2.INC"; // TEST name
		
		FeedbackContent fb = getFeedbackcontentrepository().getFeedbackContent(feedbackName, "6");
		ContentErrorType error = fb.getContentErrorType("2");
		System.out.println("content_url:" + error.getContent_url());
		for(int i = 0; i < error.getGlossaire().size(); i++)
		{
			System.out.println(error.getGlossaire().get(i) + ":" + getFeedbackcontentrepository().getGlossaire(error.getGlossaire().get(i)).getGlossaire_content());
		}
		Gson gson = new Gson();
		return new ResponseEntity<>(gson.toJson(getFeedbackcontentrepository().getMotivation(fb.getMotivation_leaf())), HttpStatus.OK);
	}
	
	@GetMapping(path = "/feedbackexample")
	public ResponseEntity<String> getFeedbackExample(@RequestHeader("Authorization") String auth) throws JsonSyntaxException, IOException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		addfeedbackrepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.FeedbackContentRoute));
		addmotivationrepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.MotivationRoute));
		addglossairerepo(license_num, LocalRouteRepository.readFileasString(LocalRoute.GlossaireRoute));
		
		String feedbackName = "3.0"; // TEST name
		String leaf = "6";
		String error_code = "3";
		
		FeedbackContent fb = getFeedbackcontentrepository().getFeedbackContent(feedbackName, leaf);
		List<Motivation> motivations = getFeedbackcontentrepository().getMotivation(fb.getMotivation_leaf());
		HashMap<String, String> glossaireMap = new HashMap<String, String>();
		for(int i = 0; i < fb.getContentErrorType(error_code).getGlossaire().size(); i++)
		{
			String mapkey = fb.getContentErrorType(error_code).getGlossaire().get(i);
			glossaireMap.put(mapkey, getFeedbackcontentrepository().getGlossaire(mapkey).getGlossaire_content());
		}
		Feedbackjson feedbackjson = new Feedbackjson(
				motivations.get(new Random().nextInt(motivations.size()-1)).getMotivation_data(),
				fb.getContentErrorType(error_code).getContent_url(),
				fb.getContentErrorType(error_code).getFormat(),
				glossaireMap);
		Gson gson = new Gson();
		return new ResponseEntity<>(gson.toJson(feedbackjson), HttpStatus.OK);
	}
	
	//TODO check how to delete database entity
	@DeleteMapping(path = "/example")
	public void deleteExample(@RequestHeader("Authorization") String auth) {
		getFeedbackcontentrepository().deleteFeedbackContent();
		getFeedbackcontentrepository().deleteGlossaire();
		getFeedbackcontentrepository().deleteMotivation();
	}
	
	public Derbyrepository getFeedbackcontentrepository() {
		return feedbackcontentrepository;
	}

}
