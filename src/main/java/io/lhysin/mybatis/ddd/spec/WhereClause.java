package io.lhysin.mybatis.ddd.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Where clause.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WhereClause {

    /**
     * @return {@link Comparison}
     */
    Comparison comparison();

    /**
     * @return {@link Column}
     */
    Column column();

    /**
     * @return ignore Null value.
     */
    boolean ignoreNullValue() default true;
}