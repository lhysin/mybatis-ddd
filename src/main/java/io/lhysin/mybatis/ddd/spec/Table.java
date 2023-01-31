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
     * Get Table Name.
     *
     * @return table name {@link String}
     */
    String name();

    /**
     * Get Table Schema.
     *
     * @return table schema {@link String}
     */
    String schema() default "";
}