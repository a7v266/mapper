package ms.service.sql;

import javax.persistence.JoinColumn;

public class JoinColumnProperty extends EntityProperty {

    private static final String REQUEST = "%s AS %s";

    private final JoinColumn joinColumn;

    public JoinColumnProperty(BeanProperty property) {
        super(property);
        joinColumn = property.getAnnotation(JoinColumn.class);
    }

    @Override
    public String select(MetaEntity metaEntity) {
        return String.format(REQUEST, joinColumn.name(), createPropertyAlias(metaEntity));
    }
}
