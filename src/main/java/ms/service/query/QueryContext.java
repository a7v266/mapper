package ms.service.query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class QueryContext implements Filtrable, Sortable, Parameterizable, Paginable {

    private Class<?> entity;
    private int limit = 10;
    private int offset = 0;
    private List<Filter> filters = new ArrayList<>();
    private List<Sort> sorts = new ArrayList<>();
    private List<Parameter> parameters = new ArrayList<>();

    protected QueryContext(Class<?> entity) {
        this.entity = entity;
    }

    public String getEntityName() {
        return entity.getCanonicalName();
    }

    @Override
    public boolean hasFilters() {
        return !filters.isEmpty();
    }

    @Override
    public Stream<Filter> getFilters() {
        return filters.stream();
    }

    @Override
    public void addFilter(Filter filter) {
        if (filter != null) {
            filters.add(filter);
            addParameter(filter.getParameter());
        }
    }

    @Override
    public void forEachFilter(Consumer<Filter> consumer) {
        for (Filter filter : filters) {
            consumer.accept(filter);
        }
    }

    @Override
    public boolean hasSorts() {
        return !sorts.isEmpty();
    }

    @Override
    public Stream<Sort> getSorts() {
        return sorts.stream();
    }

    @Override
    public void addSort(Sort sort) {
        if (sort != null) {
            sorts.add(sort);
        }
    }

    @Override
    public void forEachSort(Consumer<Sort> consumer) {
        for (Sort sort : sorts) {
            consumer.accept(sort);
        }
    }

    @Override
    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    @Override
    public Stream<Parameter> getParameters() {
        return parameters.stream();
    }

    @Override
    public void addParameter(Parameter parameter) {
        if (parameter != null) {
            parameters.add(parameter);
        }
    }

    @Override
    public void forEachParameter(Consumer<Parameter> consumer) {
        for (Parameter parameter : parameters) {
            consumer.accept(parameter);
        }
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }
}
