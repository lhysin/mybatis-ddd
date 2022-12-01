package io.lhysin.mybatis.ddd.spec;

import io.lhysin.mybatis.ddd.domain.TypedExample;

public interface Example<T> {

    enum NullHandler {
        INCLUDE, IGNORE
    }

    T getProbe();

    boolean isIgnoreNullValues();

    default Class<?> getProbeType() {
        return getProbe().getClass();
    }

    static <T> Example<T> of(T probe) {
        return new TypedExample<>(probe);
    }

    static <T> Example<T> ofIncludeNullValues(T probe) {
        return new TypedExample<>(probe, NullHandler.INCLUDE);
    }
}
