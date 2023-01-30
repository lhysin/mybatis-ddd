package io.lhysin.mybatis.ddd.spec;

/**
 * Where clause comparison
 */
public enum Comparison {

    /**
     * ADN field = value
     */
    EQUAL("="),

    /**
     * ADN field != value
     */
    NOT_EQUAL("!="),

    /**
     * ADN field &gt; value
     */
    GREATER_THAN(">"),

    /**
     * ADN field &gt;= value
     */
    GREATER_THAN_EQUAL(">="),

    /**
     * ADN field &lt; value
     */
    LESS_THAN("<"),
    /**
     * ADN field &lt;= value
     */
    LESS_THAN_EQUAL("<="),

    /**
     * ADN field LIKE value || '%'
     */
    START_WITH_LIKE("LIKE"),

    /**
     * ADN field LIKE '%' || value
     */
    END_WITH_LIKE("LIKE"),

    /**
     * ADN field LIKE '%' || value || '%'
     */
    ANY_LIKE("LIKE"),

    /**
     * ADN field IN (value)
     * <br>
     * ADN field IN (value1, value2, value3 ...)
     */
    IN("IN"),

    /**
     * ADN field NOT_IN (value)
     * <br>
     * ADN field NOT_IN (value1, value2, value3 ...)
     */
    NOT_IN("NOT IN"),

    ;

    private final String operator;

    Comparison(String operator) {
        this.operator = operator;
    }

    /**
     * Gets operator.
     *
     * @return where clause
     */
    public String getOperator() {
        return String.format(" %s ", this.operator);
    }
}