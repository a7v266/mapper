package ms.service.query;

public enum Operator {

    GREATER("%s > :%s"),
    GREATER_EQUAL("%s >= :%s"),
    LESS("%s < :%s"),
    LESS_EQUAL("%s <= :%s");

    private String template;

    Operator(String template) {
        this.template = template;
    }

    public String toSql(Object... parameters) {
        return String.format(template, parameters);
    }
}
