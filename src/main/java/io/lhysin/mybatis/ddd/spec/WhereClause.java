package io.lhysin.mybatis.ddd.spec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Where clause.
 *
 * @see Comparison
 * @see Column
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
     * <pre>
     * // column="ORD_NO", optional=true, Comparison.EQUAL
     * String ordNo = null;
     * // column="ORD_SEQ", optional=false, Comparison.EQUAL
     * Integer ordSeq = null;
     *
     * if (optional == true) {
     *     'AND ORD_SEQ = null'
     * }
     *
     * if (optional == false) {
     *     'AND ORD_NO = null'
     *     'AND ORD_SEQ = null'
     * }
     * </pre>
     * @return optional
     */
    boolean optional() default false;
}