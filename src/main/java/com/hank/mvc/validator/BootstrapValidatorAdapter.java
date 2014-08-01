package com.hank.mvc.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class BootstrapValidatorAdapter extends AbstractValidationAdapter {
	private static final Logger logger = Logger.getLogger(AbstractValidationAdapter.class);
	private static final String notEmpty = "notEmpty";
	@SuppressWarnings("serial")
	private static Map<String, String> validatorMap = new HashMap<String, String>() {
		{
			put(Size.class.getName().toString(), "stringLength");
			put(NotNull.class.getName().toString(), notEmpty);
			put(Digits.class.getName().toString(), "digits");
		}
	};

	@Override
	public String parse(Class<?> cls) throws JSONException  {
		JSONObject res = new JSONObject();
		logger.info(Size.class.getName());
		for (final java.lang.reflect.Field f : cls.getDeclaredFields()) {
			try {
				Annotation[] anos = f.getAnnotations();
				JSONObject field = new JSONObject();
				res.put(f.getName(), field);
				JSONObject validators = new JSONObject();
				field.put("validators", validators);
				if (anos != null && anos.length > 0)
					for (Annotation a : anos) {
						logger.info(a.annotationType().getName());
						String valdationName = validatorMap.get(a.annotationType().getName());
						if (valdationName != null) {
							validators.put(valdationName, new JSONObject(parseAnnotation(a)));
							if (!notEmpty.equalsIgnoreCase(valdationName)) {
								validators.put(notEmpty, new JSONObject());
							}
						}
					}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		String str = res.toString(4);
		logger.info(str);
		return str;
	}

}
