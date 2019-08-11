package ms.service.query;

/**
 * Контракт контекста постраничного SQL запроса.
 *
 * @see QueryContext
 */
public interface Paginable {

    /**
     * Возвращает размер страницы.
     *
     * @return размер страницы
     */
    int getLimit();

    /**
     * Задает размер страницы.
     *
     * @param limit размер страницы
     */
    void setLimit(int limit);

    /**
     * Возвращает смещение сраницы.
     *
     * @return смещение страницы
     */
    int getOffset();

    /**
     * Задает смещение страницы.
     *
     * @param offset смещение страницы
     */
    void setOffset(int offset);

}
