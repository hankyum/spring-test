package com.hank.mvc.validator;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HankValidatorAdapter extends AbstractValidationAdapter {
    private static final Logger logger = Logger.getLogger(AbstractValidationAdapter.class);

    @Override
    public JSONObject parse(Class<?> cls) throws JSONException {
        JSONObject res = new JSONObject();
        for (final java.lang.reflect.Field f : cls.getDeclaredFields()) {
            try {
                List<Annotation> annotations = Arrays.asList(f.getAnnotations())
                        .stream()
                        .filter(a -> a.annotationType().getName().contains("javax.validation.constraints"))
                        .collect(Collectors.toList());
                if (annotations != null && annotations.size() > 0) {
                    JSONObject rr = null;
                    for (Annotation a : annotations) {
                        logger.info("parsing annotation " + a.annotationType().getName());
                        if (rr == null) {
                            rr = new JSONObject(parseAnnotation(a));
                        }
                        if (a.annotationType().isAssignableFrom(NotNull.class)) {
                            rr.put("required", true);
                        } else {
                            rr.put("type", a.annotationType().getSimpleName());
                        }
                    }
                    res.put(f.getName(), rr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info(res.toString(4));
        return res;
    }
}
