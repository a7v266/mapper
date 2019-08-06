package ms.service.repository;

import ms.model.Writer;
import org.springframework.data.repository.CrudRepository;

public interface WriterRepository extends CrudRepository<Writer, Long>, WriterPersistence {
}
