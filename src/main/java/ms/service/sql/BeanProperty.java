package ms.service.sql;

import java.lang.annotation.Annotation;

public interface BeanProperty {

    String getPropertyName();

    Class<?> getBeanClass();

    Class<?> getPropertyClass();

    Class<?> getCollectionItemClass();

    <T extends Annotation> T  getAnnotation(Class<T> annotation);

}
