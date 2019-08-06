package ms.service;

import ms.model.Writer;
import org.springframework.transaction.annotation.Transactional;

public interface WriterService {
    @Transactional(readOnly = true)
    Iterable<Writer> getWriters();
}
