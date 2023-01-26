package io.lhysin.mybatis.ddd.spec;

import java.util.List;

import io.lhysin.mybatis.ddd.domain.TypedCriteria;

public interface Criteria<T> {

    T getProbe();

    List<String> createWhereClause(Column column);

    default Class<?> getProbeType() {
        return getProbe().getClass();
    }

    static <T> Criteria<T> of(T object) {
        return new TypedCriteria<>(object);
    }
}
