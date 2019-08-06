package ms.service.sql;

public abstract class EntityProperty implements MetaProperty {

    private final BeanProperty beanProperty;

    private String propertyAlias;

    public EntityProperty(BeanProperty property) {
        beanProperty = property;
    }

    public Class<?> getEntityClass() {
        return beanProperty.getBeanClass();
    }

    public Class<?> getPropertyClass() {
        return beanProperty.getPropertyClass();
    }

    public String getPropertyName() {
        return beanProperty.getPropertyName();
    }

    public String getPropertyAlias() {
        return propertyAlias;
    }

    public Class getCollectionItemClass() {
        return beanProperty.getCollectionItemClass();
    }

    public String createPropertyAlias(MetaEntity metaEntity) {
        propertyAlias = metaEntity.createPropertyAlias(getPropertyName());
        return propertyAlias;
    }

}
