package ms.service.sql;

import javax.persistence.Column;
import java.util.Map;

public class ColumnProperty extends EntityProperty {

    private static final String REQUEST = "%s as %s";

    private Column column;

    public ColumnProperty(BeanProperty property) {
        super(property);
        column = property.getAnnotation(Column.class);
    }

    @Override
    public String select(MetaEntity metaEntity) {
        return String.format(REQUEST, column.name(), createPropertyAlias(metaEntity));
    }

    @Override
    public void set(Object object, Map<String, Object> data) {
        if (data.containsKey(getPropertyAlias())) {

        }
    }

}
