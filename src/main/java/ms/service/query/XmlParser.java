package ms.service.query;

import java.util.Collection;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class XmlParser<T> {

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("<E>(.*?)</E>");

    private final Collection<T> container;
    private final Function<String, T> builder;

    private XmlParser(Collection<T> container, Function<String, T> builder) {
        this.container = container;
        this.builder = builder;
    }

    public static <T> XmlParser<T> newInstance(Collection<T> container, Function<String, T> builder) {
        return new XmlParser<>(container, builder);
    }

    public Collection<T> parse(String source) {
        Matcher matcher = PROPERTY_PATTERN.matcher(source);
        while (matcher.find()) {
            container.add(builder.apply(matcher.group(1)));
        }
        return container;
    }

}
