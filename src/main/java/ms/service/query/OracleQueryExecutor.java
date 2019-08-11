package ms.service.query;

import org.hibernate.StatelessSession;
import org.hibernate.mapping.Property;

public class OracleQueryExecutor extends AbstractQueryExecutor {

    private static final String AGGREGATE_COLUMN = "(SELECT XMLAGG(XMLELEMENT(E, %s)).getClobVal() FROM %s WHERE %s = %s) AS %s";

    public OracleQueryExecutor(QueryContext queryContext, StatelessSession session) {
        super(queryContext, session);
    }

    @Override
    String createAggregationColumn(
            String primaryKey,
            String referenceTable,
            String foreignKey,
            String entityKey,
            String columnAlias) {

        return String.format(AGGREGATE_COLUMN, primaryKey, referenceTable, foreignKey, entityKey, columnAlias);
    }


    @Override
    Object createEntityCollection(Property property, Object entityIds) {
/*
        if (entityIds == null) {
            return null;
        }
        if (entityIds instanceof CLOB) {
            EntityBuilder entityBuilder = EntityBuilder.newInstance((Collection) property.getValue());
            XmlParser<Object> entityParser = XmlParser.newInstance(EntityCollection.newInstance(property), entityBuilder::build);
            try {
                return entityParser.parse(((CLOB) entityIds).stringValue());

            } catch (SQLException exception) {
                LOGGER.error(exception.getMessage(), exception);
            }
        }
*/
        return null;
    }

}