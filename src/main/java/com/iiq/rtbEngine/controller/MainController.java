package com.iiq.rtbEngine.controller;

import com.iiq.rtbEngine.models.ActionType;
import com.iiq.rtbEngine.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.iiq.rtbEngine.util.Common.*;


@RestController
public class MainController {

	@Autowired
	private ActionService actionService;

	@PostConstruct
	public void init() {
		//initialize stuff after application finished start up
	}
	
	@GetMapping("/api")
	public ResponseEntity getRequest(HttpServletRequest request, HttpServletResponse response,
									 @RequestParam(name = ACTION_TYPE_VALUE,  required = true) int actionTypeId,
									 @RequestParam(name = ATTRIBUTE_ID_VALUE, required = false) Integer attributeId,
									 @RequestParam(name = PROFILE_ID_VALUE,   required = false) Integer profileId) {
		
		//GOOD LUCK! (;

		switch (ActionType.getActionTypeById(actionTypeId)){
			case ATTRIBUTION_REQUEST:return actionService.updateProfile(attributeId,profileId);
			case BID_REQUEST:return actionService.findCampaign(profileId);
			default:
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}


	}
	
}
