package ms.service;

import ms.model.Writer;
import ms.service.query.Filter;
import ms.service.query.QueryContext;

import java.util.Date;

public class WriterSearch extends QueryContext implements BirthdateFilter {

    public WriterSearch() {
        super(Writer.class);
    }

    @Override
    public void filterBirthdateGt(Date date) {
        if (date != null) {
            addFilter(Filter.gt(BIRTHDATE, date));
        }
    }

    @Override
    public void filterBirthdateGe(Date date) {
        if (date != null) {
            addFilter(Filter.ge(BIRTHDATE, date));
        }
    }

    @Override
    public void filterBirthdateLt(Date date) {
        if (date != null) {
            addFilter(Filter.lt(BIRTHDATE, date));
        }
    }

    @Override
    public void filterBirthdateLe(Date date) {
        if (date != null) {
            addFilter(Filter.le(BIRTHDATE, date));
        }
    }
}
