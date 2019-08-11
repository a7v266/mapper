package ms.service.query;

import org.hibernate.StatelessSession;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Property;

import java.io.IOException;
import java.sql.SQLException;

public class PostgresQueryExecutor extends AbstractQueryExecutor {

    private static final int BUFFER_SIZE = 1048576;
    private static final String AGGREGATE_COLUMN = "(SELECT STRING_AGG(CAST(%s AS VARCHAR), ',') FROM %s WHERE %s = %s) AS %s";

    public PostgresQueryExecutor(QueryContext queryContext, StatelessSession session) {
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
    Object createEntityCollection(Property property, Object source) {
        if (source == null) {
            return null;
        }
        CommaParser<Object> parser = CommaParser.newInstance(
                EntityCollection.newInstance(property),
                EntityBuilder.newInstance((Collection) property.getValue())::build);
        try {
            return parser.parse(toString(source));

        } catch (SQLException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}