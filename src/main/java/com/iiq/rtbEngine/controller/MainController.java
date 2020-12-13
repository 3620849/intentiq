package com.iiq.rtbEngine.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {

	private static final String ACTION_TYPE_VALUE = "act";
	private static final String ATTRIBUTE_ID_VALUE = "atid";
	private static final String PROFILE_ID_VALUE = "pid";
	
	private enum UrlParam {
		ACTION_TYPE(ACTION_TYPE_VALUE),
		ATTRIBUTE_ID(ATTRIBUTE_ID_VALUE),
		PROFILE_ID(PROFILE_ID_VALUE),
		;
		
		private final String value;
		
		private UrlParam(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	private enum ActionType {
		ATTRIBUTION_REQUEST(0),
		BID_REQUEST(1),
		;
		
		private int id;
		private static Map<Integer, ActionType> idToRequestMap = new HashMap<>();
		
		static {
			for(ActionType actionType : ActionType.values())
				idToRequestMap.put(actionType.getId(), actionType);
		}
		
		private int getId() {
			return this.id;
		}
		
		private ActionType(int id) {
			this.id = id; 
		}
		
		public static ActionType getActionTypeById(int id) {
			return idToRequestMap.get(id);
		}
	}
	
	@Autowired
	private DbManager dbManager;
	
	@PostConstruct
	public void init() {
		//initialize stuff after application finished start up
	}
	
	@GetMapping("/api")
	public void getRequest(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(name = ACTION_TYPE_VALUE,  required = true) int actionTypeId,
			@RequestParam(name = ATTRIBUTE_ID_VALUE, required = false) Integer attributeId,
			@RequestParam(name = PROFILE_ID_VALUE,   required = false) Integer profileId) {
		
		//GOOD LUCK! (;
		
	}
	
}
