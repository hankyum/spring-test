package com.hank.web.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public abstract class AbstractValidationAdapter {
	private static final Logger logger = Logger.getLogger(AbstractValidationAdapter.class);
	public abstract JSONObject parse(Class<?> cls) throws Exception;
	@Autowired
	private MessageSource messageSource;
	
	protected MessageSource getMessageSource() {
		return messageSource;
	}
	
	public Map<String, String> parseAnnotation(Annotation a) throws IllegalAccessException, InvocationTargetException {
		logger.info("Parsing annotation " + a.annotationType().getName());
		Map<String, String> res = new HashMap<String, String>();
		for (Method m : a.annotationType().getDeclaredMethods()) {
			if (ClassUtils.isPrimitiveOrWrapper(m.getReturnType()) || m.getReturnType().isAssignableFrom(String.class)) {
				logger.info(String.format("Invoking method %s", m.getName()));
				Object r = m.invoke(a, new Object[] {});
				if (r != null && !r.equals(m.getDefaultValue())) {
					String val = r.toString();
					if (m.getName().equals("message")) {
						String configMsg = getMessageSource().getMessage(val, null, LocaleContextHolder.getLocale());
						if (!StringUtils.isEmpty(configMsg)) {
							val = configMsg;
						}
					}
					res.put(m.getName(), val);
				}
			}
		}
		return res;
	}
}
