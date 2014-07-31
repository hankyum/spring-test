package com.hank.mvc.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

@Component
public class ClientValidationGenerator {
	private static final Logger logger = Logger.getLogger(ClientValidationGenerator.class);
	private static final String notEmpty = "notEmpty";
	@SuppressWarnings("serial")
	private static Map<String, String> validatorMap = new HashMap<String, String>() {
		{
			put(Size.class.getName().toString(), "stringLength");
			put(NotNull.class.getName().toString(), notEmpty);
			put(Digits.class.getName().toString(), "digits");
		}
	};
	@Autowired
	private MessageSource messageSource;

	public Map<String, Map<String, String>> validationConfig(Class<?> cls) {
		Map<String, Map<String, String>> res = new HashMap<String, Map<String, String>>();
		for (final java.lang.reflect.Field f : cls.getDeclaredFields()) {
			try {
				Annotation[] anos = f.getAnnotations();
				Map<String, String> rr = new HashMap<String, String>();
				if (anos != null && anos.length > 0)
					for (Annotation a : anos) {
						rr.putAll(parseAnnotation(a));
						rr.put("type", a.annotationType().getSimpleName());
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

	public Map<String, String> parseAnnotation(Annotation a) throws IllegalAccessException, InvocationTargetException {
		logger.info("Parseing annotation " + a.annotationType().getName());
		Map<String, String> res = new HashMap<String, String>();
		for (Method m : a.annotationType().getDeclaredMethods()) {
			if (ClassUtils.isPrimitiveOrWrapper(m.getReturnType()) || m.getReturnType().isAssignableFrom(String.class)) {
				logger.info(String.format("Invoking method %s", m.getName()));
				Object r = m.invoke(a, new Object[] {});
				if (r != null && !r.equals(m.getDefaultValue())) {
					String val = r.toString();
					if (m.getName().equals("message")) {
						String configedMsg = messageSource.getMessage(val, new Object[] {}, Locale.US);
						if (!StringUtils.isEmpty(configedMsg)) {
							val = configedMsg;
						}
					}
					res.put(m.getName(), val);
				}
			}
		}
		return res;
	}

	public String json(Class<?> cls) throws JSONException {
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
