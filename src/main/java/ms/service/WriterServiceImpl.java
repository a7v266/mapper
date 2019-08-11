package ms.service;

import ms.model.Writer;
import ms.service.query.PostgresQueryExecutor;
import ms.service.query.QueryContext;
import ms.service.query.QueryExecutor;
import ms.service.repository.WriterRepository;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Service
@Transactional
public class WriterServiceImpl implements WriterService {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<Writer> getWriters() {
        WriterSearch search = new WriterSearch();
        search.filterBirthdateGe(new Date(56, 1,1));
        search.filterBirthdateLe(new Date(56, 1, 1));
        List<Writer> writers = new ArrayList<>();
        scrollWriters(search, iterator -> {
            while(iterator.hasNext()) {
                writers.add((Writer) iterator.next());
            }
        });
        return writers;
    }

    @Override
    public void scrollWriters(QueryContext context, Consumer<Iterator<?>> consumer) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StatelessSession session = sessionFactory.openStatelessSession();
        Transaction transaction = session.beginTransaction();
        try {
            QueryExecutor queryExecutor = new PostgresQueryExecutor(context, session);
            queryExecutor.execute(consumer);
            transaction.commit();
        } catch (Exception exception) {
            transaction.rollback();
            throw new RuntimeException(exception);
        }
    }

}
