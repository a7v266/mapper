package ms.service.query;

public final class Parameter {

    private static final String NAME_TEMPLATE = "p%d";
    private final String name;
    private final Object value;

    public Parameter(Object value) {
        this.value = value;
        this.name = String.format(NAME_TEMPLATE, System.identityHashCode(this));
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
