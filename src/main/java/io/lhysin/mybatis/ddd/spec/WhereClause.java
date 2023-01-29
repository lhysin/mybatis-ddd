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
     * {@link NullHandler#INCLUDE} Include null value.
     * {@link NullHandler#IGNORE} Ignore null value.
     * @return {@link NullHandler}
     */
    NullHandler nullHandler();
}