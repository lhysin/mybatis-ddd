package io.lhysin.mybatis.ddd.spec;

import java.util.List;

import io.lhysin.mybatis.ddd.domain.Sort;
import io.lhysin.mybatis.ddd.domain.TypedCriteria;

/**
 * The interface Criteria.
 *
 * @param <T>   the type parameter
 */
public interface Criteria<T> {

    /**
     * Gets probe.
     *
     * @return the probe
     */
    T getProbe();

    /**
     * Gets sort.
     *
     * @return the sort
     */
    Sort getSort();

    /**
     * Create where clause list.
     *
     * @param column the column
     * @return the list
     */
    List<String> createWhereClause(Column column);

    /**
     * Gets probe type.
     *
     * @return the probe type
     */
    default Class<?> getProbeType() {
        return getProbe().getClass();
    }

    /**
     * Of criteria.
     *
     * @param <T> probe type
     * @param object probe
     * @return {@link Criteria}
     */
    static <T> Criteria<T> of(T object) {
        return new TypedCriteria<>(object);
    }

    static <T> Criteria<T> of(T object, Sort sort) {
        return new TypedCriteria<>(object, sort);
    }
}
