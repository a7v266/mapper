package ms.service.repository;

import ms.model.Writer;

import java.util.List;

public interface WriterPersistence {
    List<Writer> findWriters();
}
