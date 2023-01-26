package io.lhysin.mybatis.ddd.support;

import java.lang.reflect.Field;

/**
 * The type Reflection support.
 */
public class ReflectionSupport {

    /**
     * Value object.
     *
     * @param obj target
     * @param field target field
     * @return target value
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

