package ms.service.query;

import java.io.Serializable;
import java.util.function.Function;

public final class Sort implements Serializable {

    private final String property;
    private final boolean desc;
    private final String DESC_TEMPLATE = "%s DESC";

    private Sort(String property, boolean desc) {
        this.property = property;
        this.desc = desc;
    }

    public static Sort create(String property, Boolean desc) {
        return new Sort(property, desc);
    }

    public static Sort asc(String property) {
        return new Sort(property, false);
    }

    public static Sort desc(String property) {
        return new Sort(property, true);
    }

    public String getProperty() {
        return property;
    }

    public boolean isDesc() {
        return desc;
    }

    public String toSql(Function<String, String> columnSeeker) {
        return desc ? String.format(DESC_TEMPLATE, columnSeeker.apply(property)) : columnSeeker.apply(property);
    }
}
