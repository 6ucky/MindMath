package com.mocah.mindmath.server.controller.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mocah.mindmath.server.repository.LocalRouteRepository;

@RestController
@RequestMapping("/file")
public class Filecontroller {
	private static final String license_num = "mocah";

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
	
	/**
	 * overwrite imageHTML mustache, if backup return the default value
	 * @param auth
	 * @param data the content of imageHTML
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/mustache/imageHTML")
	public ResponseEntity<String> replaceImageHTML(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws IOException {
		if(!checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		String backup = "<a href='{{default_img_url}}' target='_blank'><img width='100%' src='{{default_img_url}}' alt='Feedback'></a>";
		String route = "mustache_template/contentFBImage.mustache";
		if(data.equals("backup"))
			LocalRouteRepository.writeFile(backup, route);
		else
			LocalRouteRepository.writeFile(data, route);
		return new ResponseEntity<>(LocalRouteRepository.readFileasString(route), HttpStatus.OK);
	}
	
	/**
	 * overwrite videoHTML mustache, if backup return the default value
	 * @param auth
	 * @param data the content of videoHTML
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/mustache/videoHTML")
	public ResponseEntity<String> replaceVideoHTML(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws IOException {
		if(!checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		String backup = "<video width='100%' controls poster='{{default_img_url}}'><source src='{{video_url}}' type='video/mp4'/><track kind='subtitles' src='{{video_srt_url}}' srclang='fr' label='FR' default></video><p>Le composant Video de HTML5 est requis pour lire cette vidéo.<a id='downloadLink' href='{{video_url}}'> Télécharger la vidéo.</a></p>";
		String route = "mustache_template/contentFBVideo.mustache";
		if(data.equals("backup"))
			LocalRouteRepository.writeFile(backup, route);
		else
			LocalRouteRepository.writeFile(data, route);
		return new ResponseEntity<>(LocalRouteRepository.readFileasString(route), HttpStatus.OK);
	}
	
	/**
	 * overwrite generalHTML mustache, if backup return the default value
	 * @param auth
	 * @param data the content of generalHTML
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/mustache/generalHTML")
	public ResponseEntity<String> replaceGeneralHTML(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws IOException {
		if(!checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		String backup = "<h1>{{content}}</h1>";
		String route = "mustache_template/generalHTML.mustache";
		if(data.equals("backup"))
			LocalRouteRepository.writeFile(backup, route);
		else
			LocalRouteRepository.writeFile(data, route);
		return new ResponseEntity<>(LocalRouteRepository.readFileasString(route), HttpStatus.OK);
	}
	
	/**
	 * overwrite glossary mustache, if backup return the default value
	 * @param auth
	 * @param data the content of glossary mustache
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/mustache/glossaireHTML")
	public ResponseEntity<String> replaceGlossaireHTML(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws IOException {
		if(!checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		String backup = "<p><b>Propriété.</b> {{content_propriete}}</p><p><b>Préservation.</b> {{content_preservation}}</p>";
		String route = "mustache_template/glossaryFB.mustache";
		if(data.equals("backup"))
			LocalRouteRepository.writeFile(backup, route);
		else
			LocalRouteRepository.writeFile(data, route);
		return new ResponseEntity<>(LocalRouteRepository.readFileasString(route), HttpStatus.OK);
	}
	
	/**
	 * read file from local path
	 * @param auth
	 * @param route	the path of the local file
	 * @return
	 * @throws IOException
	 */
	@GetMapping("")
	public ResponseEntity<String> getFileAsString(@RequestHeader("Authorization") String auth,
			@RequestBody String route) throws IOException {
		return new ResponseEntity<>(LocalRouteRepository.readFileasString(route), HttpStatus.OK);
	}
}
