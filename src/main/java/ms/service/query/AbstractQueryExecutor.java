package ms.service.query;

import ms.service.hibernate.HibernateMetadataExtractor;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.StatelessSession;
import org.hibernate.boot.Metadata;
import org.hibernate.internal.AbstractScrollableResults;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.*;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.CollectionType;
import org.hibernate.type.EntityType;
import org.hibernate.type.SingleColumnType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractQueryExecutor implements QueryExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractQueryExecutor.class);

    private static final String PROPERTY_TEMPLATE = "%s %s";
    private static final String ALIAS_TEMPLATE = "a%d";
    private static final String COLUMN_TEMPLATE = "%s.%s";
    private static final String SCHEMA_TEMPLATE = "%s.%s %s";
    private static final String TABLE_TEMPLATE = "%s %s";
    private static final String QUERY_TEMPLATE = "SELECT %s FROM %s";
    private static final String QUERY_DELIMITER = ", ";
    private static final String FILTER_TEMPLATE = "%s WHERE %s";
    private static final String FILTER_DELIMITER = " AND ";
    private static final String ORDER_TEMPLATE = "%s ORDER BY %s";
    private static final String ORDER_DELIMITER = ", ";

    private static final boolean READ_ONLY = true;
    private static final int FETCH_SIZE = 1000;

    private StatelessSession session;
    private QueryContext queryContext;
    private Table entityTable;
    private PersistentClass entityClass;
    private Property entityKeyProperty;
    private Column entityKeyColumn;

    private Map<String, Property> columnAliasToProperty = new HashMap<>();
    private Map<String, Property> propertyNameToProperty = new HashMap<>();

    AbstractQueryExecutor(QueryContext queryContext, StatelessSession session) {
        this.session = session;
        this.queryContext = queryContext;
        this.entityClass = HibernateMetadataExtractor.getEntityBinding(queryContext.getEntityName());
        this.entityTable = entityClass.getTable();
        this.entityKeyProperty = entityClass.getIdentifierProperty();
        this.entityKeyColumn = (Column) entityKeyProperty.getColumnIterator().next();
    }

    @Override
    public void execute(Consumer<Iterator<?>> consumer) {
        NativeQuery nativeQuery = createNativeQuery();
        nativeQuery.setReadOnly(READ_ONLY);
        nativeQuery.setFetchSize(FETCH_SIZE);

        try (ObjectIterator iterator = new ObjectIterator(nativeQuery.scroll(ScrollMode.FORWARD_ONLY))) {
            consumer.accept(iterator);
        }
    }

    private String getColumn(String propertyName) {
        return getColumn(propertyNameToProperty.get(propertyName));
    }

    private String getColumn(Property property) {
        Column column = (Column) property.getColumnIterator().next();
        return String.format(COLUMN_TEMPLATE, entityTable.getName(), column.getName());
    }

    private String getAlias(Property property) {
        String alias = String.format(ALIAS_TEMPLATE, System.identityHashCode(property));
        columnAliasToProperty.put(alias, property);
        return alias;
    }

    private String createPropertyColumn(Property property) {
        return String.format(PROPERTY_TEMPLATE, getColumn(property), getAlias(property));
    }

    private String getTable(Table table) {
        if (StringUtils.isEmpty(table.getSchema())) {
            return String.format(TABLE_TEMPLATE, table.getName(), table.getName());
        }
        return String.format(SCHEMA_TEMPLATE, table.getSchema(), table.getName(), table.getName());
    }

    private String getColumn(Table table, Column column) {
        return String.format(COLUMN_TEMPLATE, table.getName(), column.getName());
    }

    private String createAggregationColumn(Property property) {
        Collection collection = (Collection) property.getValue();
        KeyValue key = collection.getKey();
        Column keyColumn = (Column) key.getColumnIterator().next();
        Value element = collection.getElement();
        Column elementColumn = (Column) element.getColumnIterator().next();
        Table table = collection.getCollectionTable();

        return createAggregationColumn(
                getColumn(table, elementColumn),
                getTable(table),
                getColumn(table, keyColumn),
                getColumn(entityTable, entityKeyColumn),
                getAlias(property));
    }

    abstract String createAggregationColumn(String primaryKey,
                                            String referenceTable,
                                            String foreignKey,
                                            String entityKey,
                                            String columnAlias);

    private NativeQuery createNativeQuery() {

        StringJoiner queryBuilder = new StringJoiner(QUERY_DELIMITER);
        queryBuilder.add(createPropertyColumn(entityKeyProperty));
        Iterator propertyIterator = entityClass.getPropertyIterator();
        while (propertyIterator.hasNext()) {
            Property property = (Property) propertyIterator.next();
            propertyNameToProperty.put(property.getName(), property);
            Type type = property.getType();
            if (type instanceof SingleColumnType || type instanceof EntityType) {
                queryBuilder.add(createPropertyColumn(property));
            } else if (type instanceof CollectionType) {
                queryBuilder.add(createAggregationColumn(property));
            }
        }

        String sql = String.format(QUERY_TEMPLATE, queryBuilder.toString(), getTable(entityTable));

        if (queryContext.hasFilters()) {
            sql = String.format(FILTER_TEMPLATE, sql, queryContext.getFilters()
                    .map(filter -> filter.toSql(this::getColumn))
                    .collect(Collectors.joining(FILTER_DELIMITER)));
        }

        if (queryContext.hasSorts()) {
            sql = String.format(ORDER_TEMPLATE, sql, queryContext.getSorts()
                    .map(sort -> sort.toSql(this::getColumn))
                    .collect(Collectors.joining(ORDER_DELIMITER)));
        }

        LOGGER.debug(sql);

        NativeQuery nativeQuery = session.createNativeQuery(sql);

        queryContext.forEachParameter(parameter -> nativeQuery.setParameter(parameter.getName(), parameter.getValue()));

        return nativeQuery;
    }

    private Object createEntity(Property property, Object entityId) {
        return EntityBuilder.newInstance((EntityType) property.getType()).build(entityId);
    }

    abstract Object createEntityCollection(Property property, Object entityIds);

    String toString(Object source) throws IOException, SQLException {
        if (source instanceof String) {
            return (String) source;
        }
        if (source instanceof Clob) {
            return toString((Clob) source);
        }
        throw new UnsupportedOperationException();
    }

    private String toString(Clob clob) throws SQLException, IOException {
        long length = clob.length();
        if (length > Integer.MAX_VALUE) {
            throw new UnsupportedOperationException();
        }
        char[] buffer = new char[(int) length];
        try (Reader reader = clob.getCharacterStream()) {
            return String.valueOf(buffer, 0, reader.read(buffer));
        }
    }

    public final class ObjectIterator implements Iterator<Object>, AutoCloseable {

        private static final String METHOD_GET_RESULT_SET = "getResultSet";
        private final ScrollableResults dataSource;
        private final ResultSet resultSet;
        private final ResultSetMetaData metaData;

        private ObjectIterator(ScrollableResults scrollableResults) {
            try {
                dataSource = scrollableResults;
                Method method = AbstractScrollableResults.class.getDeclaredMethod(METHOD_GET_RESULT_SET);
                method.setAccessible(true);
                resultSet = (ResultSet) method.invoke(scrollableResults);
                metaData = resultSet.getMetaData();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | SQLException exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public boolean hasNext() {
            return dataSource.next();
        }

        @Override
        public Object next() {
            try {
                Class objectClass = entityClass.getMappedClass();
                Object object = objectClass.newInstance();

                for (int index = 1; index <= metaData.getColumnCount(); index++) {
                    String columnAlias = toLowerCase(metaData.getColumnName(index));
                    Property property = columnAliasToProperty.get(columnAlias);
                    Setter setter = property.getSetter(objectClass);
                    Method method = setter.getMethod();
                    Type type = property.getType();
                    if (type instanceof EntityType) {
                        method.invoke(object, createEntity(property, resultSet.getObject(index)));
                    } else if (type instanceof CollectionType) {
                        method.invoke(object, createEntityCollection(property, resultSet.getObject(index)));
                    } else {
                        method.invoke(object, resultSet.getObject(index));
                    }
                }
                return object;

            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        @Override
        public void close() {
            this.dataSource.close();
        }

        private String toLowerCase(String string) {
            return string != null ? string.toLowerCase(Locale.ENGLISH) : null;
        }
    }
}