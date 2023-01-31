package io.lhysin.mybatis.ddd.support;

import java.lang.reflect.Field;

/**
 * The type Reflection support.
 */
public class ReflectionSupport {

    /**
     * Value of target Object by Field
     *
     * @param obj target {@link Object}
     * @param field field {@link Field}
     * @return target value {@link Object}
     */
    public static Object value(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(false);
        }
    }
}

