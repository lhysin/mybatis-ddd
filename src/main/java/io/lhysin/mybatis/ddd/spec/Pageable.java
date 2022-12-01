package io.lhysin.mybatis.ddd.spec;

import io.lhysin.mybatis.ddd.domain.Sort;

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