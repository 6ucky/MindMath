package com.mocah.mindmath.server.controller.feedback;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentList;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentListRedis;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
import com.mocah.mindmath.server.repository.derby.Derbyrepository;

@RestController
@RequestMapping("/feedback")
public class Feedbackcontroller {

	private static final String license_num = "mocah";
	
	@Autowired
	private RedisTemplate<String, Serializable> serializableRedisTemplate;

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

	/**
	 * Add new generator with feedback content list in Derby
	 * @param auth
	 * @param data feedbackcontentlist
	 * @return feedbackcontentlist
	 */
	@PostMapping(path = "/feedbackcontentlist", consumes = "application/json")
	public ResponseEntity<String> addfeedbacklistrepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		
		Gson gson = new Gson();
		FeedbackContentList feedbacks = gson.fromJson(data, FeedbackContentList.class);
		if(feedbacks == null || feedbacks.getGenerator() == "")
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No content.");
		
		if(getFeedbackcontentrepository().getAllFeedbackContentList(feedbacks.getGenerator()) != null)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Generator conflicts.");
		
		getFeedbackcontentrepository().save(feedbacks);
		
		//save persistent feedback content list in Redis
		FeedbackContentListRedis fbRedis = new FeedbackContentListRedis(getFeedbackcontentrepository().getAllFeedbackContentList());
		serializableRedisTemplate.opsForValue().set("FeedbackContentList-10-2020", fbRedis);
		
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.OK);
	}
	
	/**
	 * get all feedback content list
	 * @param auth
	 * @param data
	 * @return
	 */
	@GetMapping(path = "/feedbackcontentlist", consumes = "application/json")
	public ResponseEntity<String> getfeedbacklistrepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		
		Gson gson = new Gson();
		
		List<FeedbackContentList> feedbacklist = getFeedbackcontentrepository().getAllFeedbackContentList();
		
		return new ResponseEntity<>(gson.toJson(feedbacklist), HttpStatus.OK);
	}
	
	/**
	 * Delete feedbackcontent list based on generator
	 * @param auth
	 * @param generator parameter
	 * @return
	 */
	@DeleteMapping(path = "/feedbackcontentlist", consumes = "application/json")
	public ResponseEntity<String> deletefeedbacklistrepo(@RequestHeader("Authorization") String auth,
			@RequestParam String generator) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		
		FeedbackContentList feedbacklists = getFeedbackcontentrepository().getAllFeedbackContentList(generator);
		
		if(feedbacklists == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Generator not found.");

		getFeedbackcontentrepository().delete(feedbacklists);
		
		//save persistent feedback content list in Redis
		FeedbackContentListRedis fbRedis = new FeedbackContentListRedis(getFeedbackcontentrepository().getAllFeedbackContentList());
		serializableRedisTemplate.opsForValue().set("FeedbackContentList-10-2020", fbRedis);
		
		return new ResponseEntity<>("Generator " + generator + " is deleted.", HttpStatus.OK);
	}
	
	/**
	 * Update feedback content in Derby
	 * @param auth 
	 * @param data
	 * @param generator parameter in request
	 * @return
	 */
	@PostMapping(path = "/feedbackcontent", consumes = "application/json")
	public ResponseEntity<String> addfeedbackrepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data, @RequestParam String generator) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		FeedbackContentList feedbacklists = getFeedbackcontentrepository().getAllFeedbackContentList(generator);
		
		if(feedbacklists == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Generator not found.");
		
		Gson gson = new Gson();
		List<FeedbackContent> feedbacks = gson.fromJson(data, FeedbackContentList.class).getFeedback_content();
		
		if(feedbacks == null)
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No content.");
		
		getFeedbackcontentrepository().delete(feedbacklists);
		feedbacklists.setFeedback_content(feedbacks);
		getFeedbackcontentrepository().save(feedbacklists);
		
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.OK);
	}

	/**
	 * Update motivation in Derby
	 * @param auth 
	 * @param data
	 * @param generator parameter in request
	 * @return
	 */
	@PostMapping(path = "/motivation", consumes = "application/json")
	public ResponseEntity<String> addmotivationrepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data, @RequestParam String generator) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		FeedbackContentList feedbacklists = getFeedbackcontentrepository().getAllFeedbackContentList(generator);
		
		if(feedbacklists == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Generator not found.");
		
		Gson gson = new Gson();
		List<Motivation> feedbacks = gson.fromJson(data, FeedbackContentList.class).getMotivationlist();
		
		if(feedbacks == null)
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No content.");
		
		getFeedbackcontentrepository().delete(feedbacklists);
		feedbacklists.setMotivationlist(feedbacks);
		getFeedbackcontentrepository().save(feedbacklists);
		
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.OK);
	}

	/**
	 * Update glossary in Derby
	 * @param auth 
	 * @param data
	 * @param generator parameter in request
	 * @return
	 */
	@PostMapping(path = "/glossaire", consumes = "application/json")
	public ResponseEntity<String> addglossairerepo(@RequestHeader("Authorization") String auth,
			@RequestBody String data, @RequestParam String generator) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		FeedbackContentList feedbacklists = getFeedbackcontentrepository().getAllFeedbackContentList(generator);
		
		if(feedbacklists == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Generator not found.");
		
		Gson gson = new Gson();
		List<Glossaire> feedbacks = gson.fromJson(data, FeedbackContentList.class).getGlossairelist();
		
		if(feedbacks == null)
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No content.");
		
		getFeedbackcontentrepository().delete(feedbacklists);
		feedbacklists.setGlossairelist(feedbacks);
		getFeedbackcontentrepository().save(feedbacklists);
		
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.OK);
	}
	

	public Derbyrepository getFeedbackcontentrepository() {
		return feedbackcontentrepository;
	}

}
