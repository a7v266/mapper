package ms.service.sql;

public interface MetaEntity {

    String getEntityIdColumn();

    String createPropertyAlias(String propertyName);

    String createQuery();
}
