package ms.service.sql;

import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodProperty implements BeanProperty {

    private static final Pattern METHOD_PATTERN = Pattern.compile("get([A-Z]\\S+?)");
    private static final String SETTER_FORMAT = "set%s";

    private String propertyName;
    private Method getter;
    private Method setter;

    public static MethodProperty create(Method method) {
        Matcher matcher = METHOD_PATTERN.matcher(method.getName());
        if (matcher.find()) {
            return new MethodProperty(matcher, method);
        }
        return null;
    }

    private MethodProperty(Matcher matcher, Method method) {
        getter = method;
        String property = matcher.group();
        setter = ReflectionUtils.findMethod(method.getDeclaringClass(), String.format(SETTER_FORMAT, property));
        char[] chars = property.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        propertyName = new String(chars);
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Class<?> getBeanClass() {
        return getter.getDeclaringClass();
    }

    @Override
    public Class<?> getPropertyClass() {
        return getter.getReturnType();
    }

    @Override
    public Class<?> getCollectionItemClass() {
        return null;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return getter.getAnnotation(annotation);
    }
}
