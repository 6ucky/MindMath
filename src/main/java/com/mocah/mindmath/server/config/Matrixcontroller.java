package com.mocah.mindmath.server.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.Tree;
import com.mocah.mindmath.decisiontree.search.DeepFirstSearch;
import com.mocah.mindmath.repository.Matrixrepository;

/**
 * @author	Yan Wang
 */

@RestController
@RequestMapping("/matrix")
public class Matrixcontroller {
	
	private static final String license_num = "mocah";
	
	/**
	 * check the post request based on authorization
	 * @param auth the authorization parameter from headers
	 * @return authorized or unauthorized
	 */
	private static boolean checkauth(String auth) {
		if(auth.equals(license_num))
			return true;
		return false;
	}
	
	//receive JSON and update the decision tree class in default version 1.0
	@PostMapping("")
	public ResponseEntity<String> addmatrix(@RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		return addmatrixv1_0(auth, data);
	}
	
	/**	
	 * receive JSON and update the decision tree class in version 1.0
	 * @param auth authorization headers
	 * @param data receive JSON file as String
	 * @return HTTP status and message
	 */
	@PostMapping("/v1.0")
	public ResponseEntity<String> addmatrixv1_0(@RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		if(!checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		Gson gson = new Gson();
		Tree tree = gson.fromJson(data, Tree.class);
		Node root = tree.getRoot();
		DeepFirstSearch dfs = new DeepFirstSearch(tree);
		dfs = Matrixrepository.getVisitedNode(dfs, root);
		
		return new ResponseEntity<String>("Matrix is updated in the server.\nVisit order: " + dfs.getVisitedNodes(), HttpStatus.CREATED);
	}

}
