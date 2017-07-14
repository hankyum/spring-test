package com.hank.domain;

import com.hank.Application;
import com.hank.web.validator.BootstrapValidatorAdapter;
import com.hank.web.validator.HankValidatorAdapter;

public class ValidationConfig {

	public String getBootstrapValidator() {
		try {
			return Application.getValidationAdapter(BootstrapValidatorAdapter.class).parse(Person.class).toString(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public String getHankValidator() {
		try {
			return Application.getValidationAdapter(HankValidatorAdapter.class).parse(Person.class).toString(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
