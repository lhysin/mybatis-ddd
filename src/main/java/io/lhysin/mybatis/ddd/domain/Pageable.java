package io.lhysin.mybatis.ddd.domain;

/**
 * Pageable
 */
public interface Pageable {
    /**
     * @return offset
     */
    long getOffset();

    /**
     * @return limit
     */
    int getLimit();

    /**
     * @return sort
     */
    Sort getSort();
}