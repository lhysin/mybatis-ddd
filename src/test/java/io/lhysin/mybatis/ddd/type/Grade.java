package io.lhysin.mybatis.ddd.type;

import io.lhysin.mybatis.ddd.spec.Code;

/**
 * The enum Grade.
 */
public enum Grade implements Code {
    /**
     * One grade.
     */
    ONE("1"),
    /**
     * Two grade.
     */
    TWO("2"),
    /**
     * Three grade.
     */
    THREE("3"),
    /**
     * Four grade.
     */
    FOUR("4"),
    /**
     * Five grade.
     */
    FIVE("5"),
    /**
     * Six grade.
     */
    SIX("6");

    private final String code;

    Grade(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}