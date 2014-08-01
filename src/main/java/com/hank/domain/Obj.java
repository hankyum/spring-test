package com.hank.domain;

import com.hank.Application;
import com.hank.mvc.validator.BootstrapValidatorAdapter;
import com.hank.mvc.validator.HankValidatorAdapter;

public class Obj {
	public String getBootstrapValidator() {
		try {
			return Application.getValidationAdapter(BootstrapValidatorAdapter.class).parse(this.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getHankValidator() {
		try {
			return Application.getValidationAdapter(HankValidatorAdapter.class).parse(this.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
