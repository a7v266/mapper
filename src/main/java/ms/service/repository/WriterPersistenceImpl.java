package ms.service.repository;

import ms.model.Writer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;

public class WriterPersistenceImpl implements WriterPersistence {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Writer> findWriters() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Writer> criteria = builder.createQuery(Writer.class);
        Root<Writer> root = criteria.from(Writer.class);
        root.fetch("books", JoinType.LEFT);
        criteria.select(root);

        return entityManager.createQuery(criteria).getResultList();
    }

}
