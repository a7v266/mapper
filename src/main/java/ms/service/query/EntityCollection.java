package ms.service.query;

import org.hibernate.mapping.Property;

import java.util.*;

public final class EntityCollection {

    public static <T> Collection<T> newInstance(Property property) {
        Class<?> collectionClass = property.getType().getReturnedClass();
        if (collectionClass.isAssignableFrom(Set.class) || collectionClass.isAssignableFrom(Collection.class)) {
            return new HashSet<>();
        }
        if (collectionClass.isAssignableFrom(List.class)) {
            return new ArrayList<>();
        }
        throw new UnsupportedOperationException();
    }

}
