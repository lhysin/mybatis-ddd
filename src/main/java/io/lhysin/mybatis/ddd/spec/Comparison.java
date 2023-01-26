package io.lhysin.mybatis.ddd.spec;

/**
 * Where clause condition
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
     * ADN field > value
     */
    GREATER_THAN(">"),

    /**
     * ADN field >= value
     */
    GREATER_THAN_EQUAL(">="),

    /**
     * ADN field < value
     */
    LESS_THAN("<"),
    /**
     * ADN field <= value
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
     * ADN field IN (value1, value2, value3 ...)
     */
    IN("IN"),

    /**
     * ADN field NOT_IN (value)
     * ADN field NOT_IN (value1, value2, value3 ...)
     */
    NOT_IN("NOT IN"),

    ;

    private final String operator;

    Comparison(String operator) {
        this.operator = operator;
    }

    public String getOperator() {

        return " ".concat(this.operator).concat(" ");
    }
}