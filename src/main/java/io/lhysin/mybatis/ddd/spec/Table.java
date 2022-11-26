package io.lhysin.mybatis.ddd.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Table
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * Table Name
     */
    String name();

    /**
     * Table Schema
     */
    String schema() default "";
}