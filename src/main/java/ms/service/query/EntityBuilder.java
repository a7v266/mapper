package ms.service.query;

import ms.service.hibernate.HibernateMetadataExtractor;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Value;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityBuilder.class);

    private final Class entityClass;
    private final Type keyType;
    private final Method method;

    private EntityBuilder(Class entityClass) {
        this.entityClass = entityClass;
        String entityName = entityClass.getCanonicalName();
        PersistentClass persistentClass = HibernateMetadataExtractor.getEntityBinding(entityName);
        Property identifierProperty = persistentClass.getIdentifierProperty();
        keyType = identifierProperty.getType();
        Setter setter = identifierProperty.getSetter(entityClass);
        method = setter.getMethod();
    }

    public static EntityBuilder newInstance(Class objectClass) {
        return new EntityBuilder(objectClass);
    }

    public static EntityBuilder newInstance(EntityType entityType) {
        return EntityBuilder.newInstance(entityType.getReturnedClass());
    }

    public static EntityBuilder newInstance(Collection collection) {
        Value element = collection.getElement();
        Class elementClass = element.getType().getReturnedClass();
        return EntityBuilder.newInstance(elementClass);
    }

    public Object build(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key.getClass() == keyType.getReturnedClass()) {
            return buildEntity(key);
        }
        if (key.getClass() == String.class) {
            return buildEntity(normalize((String) key));
        }
        throw new UnsupportedOperationException();
    }

    public Object normalize(String key) {
        if (keyType.getReturnedClass() == Long.class) {
            return Long.parseLong(key);
        }
        throw new UnsupportedOperationException();
    }

    public Object buildEntity(Object key) {
        Object object = null;
        try {
            object = entityClass.newInstance();
            method.invoke(object, key);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
        return object;
    }

}
