package ms.service.sql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class FieldProperty implements BeanProperty {

    private final Field field;

    public static FieldProperty create(Field field) {
        return new FieldProperty(field);
    }

    private FieldProperty(Field field) {
        this.field = field;
    }

    @Override
    public String getPropertyName() {
        return field.getName();
    }

    @Override
    public Class<?> getBeanClass() {
        return field.getDeclaringClass();
    }

    @Override
    public Class<?> getPropertyClass() {
        return field.getType();
    }

    @Override
    public Class<?> getCollectionItemClass() {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        return (Class<?>) parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return field.getAnnotation(annotation);
    }
}
