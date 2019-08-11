package ms.service.hibernate;

import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;

import java.util.ArrayList;
import java.util.List;

public class HibernateIntegratorProvider implements IntegratorProvider {

    private final List<Integrator> integrators = new ArrayList<>();;

    public HibernateIntegratorProvider() {
        integrators.add(new HibernateMetadataExtractor());
    }

    @Override
    public List<Integrator> getIntegrators() {
        return integrators;
    }
}
