package ms.service.query;

import java.util.Iterator;
import java.util.function.Consumer;

public interface QueryExecutor {

    void execute(Consumer<Iterator<?>> consumer);

}
