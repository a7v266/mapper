package ms.service.query;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Контракт фильтруемого контекста SQL запроса.
 *
 * @see QueryContext
 */
public interface Filtrable {

    /**
     * Идентифицирует наличие в контектсе фильтров.
     *
     * @return <tt>true</tt> если фильтры есть
     */
    boolean hasFilters();

    /**
     * Возвращает поток фильтров.
     *
     * @return набор фильтров
     */
    Stream<Filter> getFilters();

    /**
     * Добавляет фильтр в контекст.
     *
     * @param filter фильтр
     */
    void addFilter(Filter filter);


    /**
     * Последовательно передает фильтры консумеру.
     *
     * @param consumer консумер принимающий фильтр
     */
    void forEachFilter(Consumer<Filter> consumer);
}
