package com.mocah.mindmath.server.cabri.feedback;

import java.util.ArrayList;
import java.util.List;

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
import com.mocah.mindmath.server.Derbyrepository;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentList;

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
	
	@PostMapping(path = "", consumes = "application/json")
	public ResponseEntity<String> addfeedbackrepo(@RequestHeader("Authorization") String auth, @RequestBody String data) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		Gson gson = new Gson();
		List<FeedbackContent> feedbacks = gson.fromJson(data, FeedbackContentList.class).getFeedbackcontents();
		for(FeedbackContent feedback : feedbacks)
		{
			getFeedbackcontentrepository().save(feedback);
		}
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.OK);
	}
	
	@GetMapping("")
	public ResponseEntity<String> getALLtask(@RequestHeader("Authorization") String auth) {
		if (!auth.equals("test"))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		List<FeedbackContent> feedbacks = new ArrayList<>();
		getFeedbackcontentrepository().getAllFeedbackContent().forEach(feedbacks::add);
		if (feedbacks.size() == 0)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
		Gson gson = new Gson();
		return new ResponseEntity<>(gson.toJson(feedbacks), HttpStatus.FOUND);
	}

	public Derbyrepository getFeedbackcontentrepository() {
		return feedbackcontentrepository;
	}

}
