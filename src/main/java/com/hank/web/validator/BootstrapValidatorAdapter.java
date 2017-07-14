package com.hank.web.validator;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BootstrapValidatorAdapter extends AbstractValidationAdapter {
    private static final Logger logger = Logger.getLogger(AbstractValidationAdapter.class);
    private static final String notEmpty = "notEmpty";
    @SuppressWarnings("serial")
    private static Map<String, String> validatorMap = new HashMap<String, String>() {
        {
            put(Size.class.getName(), "stringLength");
            put(NotNull.class.getName(), notEmpty);
            put(Digits.class.getName(), "digits");
        }
    };

    @Override
    public JSONObject parse(Class<?> cls) throws JSONException {
        JSONObject res = new JSONObject();
        logger.info(Size.class.getName());
        for (final java.lang.reflect.Field f : cls.getDeclaredFields()) {
            try {
                List<Annotation> annotations = Arrays.asList(f.getAnnotations())
                        .stream()
                        .filter(a -> a.annotationType().getName().contains("javax.validation.constraints"))
                        .collect(Collectors.toList());
                if (annotations != null && annotations.size() > 0) {
                    JSONObject field = new JSONObject();
                    res.put(f.getName(), field);
                    JSONObject validators = new JSONObject();
                    field.put("validators", validators);
                    for (Annotation a : annotations) {
                        logger.info("parsing annotation " + a.annotationType().getName());
                        String validationName = validatorMap.get(a.annotationType().getName());
                        if (validationName != null) {
                            validators.put(validationName, new JSONObject(parseAnnotation(a)));
                            if (!notEmpty.equalsIgnoreCase(validationName)) {
                                validators.put(notEmpty, new JSONObject());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info(res.toString(4));
        return res;
    }

}
