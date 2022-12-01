package io.lhysin.mybatis.ddd.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Table.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * Name string.
     *
     * @return the string
     */
    String name();

    /**
     * Schema string.
     *
     * @return the string
     */
    String schema() default "";
}