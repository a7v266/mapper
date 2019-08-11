package ms.service;

import ms.model.Writer;
import ms.service.query.QueryContext;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public interface WriterService {

    List<Writer> getWriters();

    void scrollWriters(QueryContext context, Consumer<Iterator<?>> consumer);
}
