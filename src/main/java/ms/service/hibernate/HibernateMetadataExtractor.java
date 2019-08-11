package ms.service.hibernate;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class HibernateMetadataExtractor implements Integrator {

    private static Metadata METADATA;

    public static Metadata getMetadata() {
        if (METADATA == null) {
            throw new IllegalStateException();
        }
        return METADATA;
    }

    public static PersistentClass getEntityBinding(String entityName) {
        return getMetadata().getEntityBinding(entityName);
    }

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        METADATA = metadata;
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
    }
}
