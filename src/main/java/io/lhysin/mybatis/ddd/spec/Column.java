package io.lhysin.mybatis.ddd.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Table Column
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * @return Column Name
     */
    String name();

    /**
     * @return available insert column
     */
    boolean insertable() default true;

    /**
     * @return available update column
     */
    boolean updatable() default true;
}