package ms.service.query;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Parameterizable {

    boolean hasParameters();

    Stream<Parameter> getParameters();

    void addParameter(Parameter parameter);

    void forEachParameter(Consumer<Parameter> consumer);

}
