package ms.service;

import ms.model.Car;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    @Override
    public Car saveCar(Car car) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        try (StatelessSession session = sessionFactory.openStatelessSession()) {
            session.beginTransaction();
            try {
                Car entity = (Car) session.get(Car.class, car.getId());
                if (entity == null) {
                    entity = car;
                    session.insert(entity);
                } else {
                    entity.setName(car.getName());
                    entity.setVersion(car.getVersion());
                    session.update(entity);
                }
                session.getTransaction().commit();
                return entity;
            } catch (HibernateException exception) {
                session.getTransaction().rollback();
                throw exception;
            }
        }
    }

    @Override
    public Car mergeCar(Car car) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            try {
                Car entity = session.get(Car.class, car.getId());
                if (entity == null) {
                    entity = car;
                    session.save(entity);
                } else {
                    entity.setName(car.getName());
                    entity.setVersion(car.getVersion());
                    session.update(entity);
                }
                session.getTransaction().commit();
                return entity;
            } catch (HibernateException exception) {
                session.getTransaction().rollback();
                throw exception;
            }
        }
    }
}
