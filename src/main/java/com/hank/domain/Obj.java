package com.hank.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hank.Application;

public class Obj {
	public String getFormConfig() {
		try {
			return Application.formValidatinConfig.json(this.getClass());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Map<String, String>> getValidationConfig() throws IllegalAccessException, InvocationTargetException, JSONException, JsonProcessingException {
		return Application.formValidatinConfig.validationConfig(this.getClass());
	}
}
