package ms.service.query;

import org.springframework.util.ObjectUtils;

import java.util.function.Function;

public final class Filter {

    private Operator operator;
    private String property;
    private Parameter parameter;

    private Filter(Operator operator, String property, Object value) {
        this.operator = operator;
        this.property = property;
        this.parameter = new Parameter(value);
    }

    public static Filter gt(String property, Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return new Filter(Operator.GREATER, property, value);
    }

    public static Filter ge(String property, Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return new Filter(Operator.GREATER_EQUAL, property, value);
    }

    public static Filter lt(String property, Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return new Filter(Operator.LESS, property, value);
    }

    public static Filter le(String property, Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return new Filter(Operator.LESS_EQUAL, property, value);
    }

    public Operator getOperator() {
        return operator;
    }

    public String getProperty() {
        return property;
    }

    public String toSql(Function<String, String> columnSeeker) {
        return operator.toSql(columnSeeker.apply(property), parameter.getName());
    }

    public Parameter getParameter() {
        return parameter;
    }

}
