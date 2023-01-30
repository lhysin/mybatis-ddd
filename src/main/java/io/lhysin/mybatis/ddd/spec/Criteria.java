package io.lhysin.mybatis.ddd.spec;

import java.util.List;

import io.lhysin.mybatis.ddd.domain.Sort;

/**
 * The interface Criteria.
 *
 * @param <T>    the type parameter
 */
public interface Criteria<T> {

    /**
     * @param <T> probe type
     * @param probe probe
     * @return {@link Criteria}
     */
    static <T> Criteria<T> of(T probe) {
        return new TypedCriteria<>(probe);
    }

    /**
     * @param <T> probe type
     * @param probe probe
     * @param pageable {@link Pageable}
     * @return {@link Criteria}
     */
    static <T> Criteria<T> of(T probe, Pageable pageable) {
        return new TypedCriteria<>(probe, pageable);
    }

    /**
     * @param <T> probe type
     * @param probe probe
     * @param sort {@link Sort}
     * @return {@link Criteria}
     */
    static <T> Criteria<T> of(T probe, Sort sort) {
        return new TypedCriteria<>(probe, sort);
    }

    /**
     * @return the probe
     */
    T getProbe();

    /**
     * nullable pageable.
     *
     * @return {@link Pageable}
     */
    Pageable getPageable();

    /**
     * nullable sort.
     *
     * @return {@link Sort}
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
}
