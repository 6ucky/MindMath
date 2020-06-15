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
import com.mocah.mindmath.parser.owlparser.OWLAPIparser;

/**
 * @author	Yan Wang
 */

@RestController
@RequestMapping("/ontology")
public class Ontologycontroller {

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
	
	// Send OWL file in default version 1.0
	@PostMapping("/")
	public ResponseEntity<String> getresourceontology(@RequestHeader("Authorization") String auth,
			@RequestBody String data){
		return getresourceontologyv1_0(auth, data);
	}
	
	/**
	 * Send OWL file in version 1.0
	 * @return the full list of statement information including resource, property and RDF node.
	 */
	@PostMapping("/v1.0")
	public ResponseEntity<String> getresourceontologyv1_0(@RequestHeader("Authorization") String auth,
			@RequestBody String data){
		if(!checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		Gson gson = new Gson();
		OWLAPIparser owlparser = new OWLAPIparser(data);
		return new ResponseEntity<String>(gson.toJson(owlparser.listall()), HttpStatus.ACCEPTED);
	}
}
