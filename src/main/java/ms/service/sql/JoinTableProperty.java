package ms.service.sql;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

public class JoinTableProperty extends EntityProperty {

    private static final String REQUEST = "(SELECT string_agg(%s::varchar, ',') FROM %s WHERE %s = %s) AS %s";

    private final JoinTable joinTable;

    public JoinTableProperty(BeanProperty property) {
        super(property);
        joinTable = property.getAnnotation(JoinTable.class);
    }

    @Override
    public String select(MetaEntity metaEntity) {
        String joinedTable = joinTable.name();

        JoinColumn[] joinColumns = joinTable.joinColumns();
        if (joinColumns.length != 1) {
            throw new UnsupportedOperationException(String.format("Join table contains more that one join column %s", getPropertyName()));
        }
        String joinedColumn = joinColumns[0].name();

        JoinColumn[] inverseJoinColumns = joinTable.inverseJoinColumns();
        if (inverseJoinColumns.length != 1) {
            throw new UnsupportedOperationException(String.format("Join table contains more that one inverse join column %s", getPropertyName()));
        }
        String mappedColumn = inverseJoinColumns[0].name();

        return String.format(REQUEST, joinedColumn, joinedTable, mappedColumn, metaEntity.getEntityIdColumn(), createPropertyAlias(metaEntity));
    }

    @Override
    public void set(Object object, Map<String, Object> data) {
        try {

            String source = (String) data.get(getPropertyAlias());

            if (source == null) {
                return;
            }

            Stream.of(source.split(",")).map(Long::valueOf).map(id -> {
                try {
                    Object item = getCollectionItemClass().newInstance();



                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            })


            Collection collection = (Collection) getPropertyClass().newInstance();



        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
