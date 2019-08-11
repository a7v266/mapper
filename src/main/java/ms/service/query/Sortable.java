package ms.service.query;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Контракт сортируемого контекста SQL запроса.
 *
 * @see QueryContext
 */
public interface Sortable {

    /**
     * Идентифицирует наличие в контексте элементов сортировки {@link Sort}.
     *
     * @return <tt>true</tt> если в контексте есть параметры сортировки
     */
    boolean hasSorts();

    /**
     * Возвращает поток параметров сортировки.
     *
     * @return поток параметров сортировки
     */
    Stream<Sort> getSorts();

    /**
     * Задает список параметров сортировки.
     *
     * @param sort список параметров сортировки
     */
    void addSort(Sort sort);

    void forEachSort(Consumer<Sort> consumer);
}
