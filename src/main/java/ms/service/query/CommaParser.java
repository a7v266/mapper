package ms.service.query;

import java.util.Collection;
import java.util.function.Function;

public final class CommaParser<T> {

    private static final String COMMA = ",";

    private final Collection<T> container;
    private final Function<String, T> builder;

    private CommaParser(Collection<T> container, Function<String, T> builder) {
        this.container = container;
        this.builder = builder;
    }

    public static <T> CommaParser<T> newInstance(Collection<T> container, Function<String, T> builder) {
        return new CommaParser<>(container, builder);
    }

    public Collection<T> parse(String source) {
        for (String id : source.split(COMMA)) {
            container.add(builder.apply(id));
        }
        return container;
    }

}
