package ms.service.sql;

import ms.model.Writer;
import org.springframework.util.ReflectionUtils;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MetaEntityImpl implements MetaEntity {

    private BeanProperty id;
    private Table table;
    private Class<?> type;

    private Map<String, MetaProperty> properties = new HashMap<>();

    public MetaEntityImpl(Class<?> type) {
        this.type = type;
        ReflectionUtils.doWithFields(type, field -> {
            BeanProperty property = FieldProperty.create(field);
            if (field.isAnnotationPresent(Id.class)) {
                id = property;
            }
            if (field.isAnnotationPresent(Column.class)) {
                properties.put(property.getPropertyName(), new ColumnProperty(property));
            } else if (field.isAnnotationPresent(JoinColumn.class)) {
                properties.put(property.getPropertyName(), new JoinColumnProperty(property));
            } else if (field.isAnnotationPresent(JoinTable.class)) {
                properties.put(property.getPropertyName(), new JoinTableProperty(property));
            }
        });
        ReflectionUtils.doWithMethods(type, method -> {
            BeanProperty property = MethodProperty.create(method);
            if (property == null) {
                return;
            }
            if (method.isAnnotationPresent(Id.class)) {
                id = property;
            }
            if (method.isAnnotationPresent(Column.class)) {
                properties.put(property.getPropertyName(), new ColumnProperty(property));
            } else if (method.isAnnotationPresent(JoinColumn.class)) {
                properties.put(property.getPropertyName(), new JoinColumnProperty(property));
            } else if (method.isAnnotationPresent(JoinTable.class)) {
                properties.put(property.getPropertyName(), new JoinTableProperty(property));
            }
        });
        table = type.getAnnotation(Table.class);
    }

    @Override
    public String getEntityIdColumn() {
        Column column = id.getAnnotation(Column.class);
        return column.name();
    }

    @Override
    public String createPropertyAlias(String propertyName) {
        return propertyName;
    }

    public String getTableName() {
        return table.name();
    }

    @Override
    public String createQuery() {
        StringJoiner query = new StringJoiner(" ");
        query.add("SELECT");
        query.add(properties.values().stream()
                .map(property -> property.select(this)).collect(Collectors.joining(",")));
        query.add("FROM");
        query.add(getTableName());
        return query.toString();
    }

    public Object map(Map<String, Object> data) {
        try {
            Object object = type.newInstance();
            properties.values().forEach(property -> property.set(object, data));
            return object;
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void main(String[] args) {
        MetaEntity metadata = new MetaEntityImpl(Writer.class);
        System.out.println(metadata.createQuery());
    }
}
