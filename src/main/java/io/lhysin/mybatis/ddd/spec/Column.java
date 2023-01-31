package io.lhysin.mybatis.ddd.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Table Column
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * @return Column Name {@link String}
     */
    String name();

    /**
     * @return available insert column {@link Boolean}
     */
    boolean insertable() default true;

    /**
     * @return available update column {@link Boolean}
     */
    boolean updatable() default true;
}