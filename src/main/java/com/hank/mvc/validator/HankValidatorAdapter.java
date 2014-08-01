package com.hank.mvc.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class HankValidatorAdapter extends AbstractValidationAdapter {
	private static final Logger logger = Logger.getLogger(AbstractValidationAdapter.class);
	private ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

	public Map<String, Map<String, String>> validationConfig(Class<?> cls) {
		Map<String, Map<String, String>> res = new HashMap<String, Map<String, String>>();
		for (final java.lang.reflect.Field f : cls.getDeclaredFields()) {
			try {
				Annotation[] anos = f.getAnnotations();
				Map<String, String> rr = new HashMap<String, String>();
				if (anos != null && anos.length > 0)
					for (Annotation a : anos) {
						rr.putAll(parseAnnotation(a));
						if (a.annotationType().isAssignableFrom(NotNull.class)) {
							rr.put("required", "true");
						} else {
							rr.put("type", a.annotationType().getSimpleName());
						}
					}
				res.put(f.getName(), rr);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	@Override
	public String parse(Class<?> cls) throws Exception {
		return mapper.writeValueAsString(validationConfig(cls));
	}

}
