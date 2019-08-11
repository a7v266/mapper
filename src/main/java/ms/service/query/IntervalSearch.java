package ms.service.query;

import java.util.Date;

/**
 * Контракт поискового контейнера по интервалу.
 */
public interface IntervalSearch {

    /**
     * Возвращает дату начала интервала.
     *
     * @return дата начала интервала
     */
    Date getStartDate();

    /**
     * Возвращает дату окончания интервала.
     *
     * @return дата окончания интервала
     */
    Date getEndDate();

}
