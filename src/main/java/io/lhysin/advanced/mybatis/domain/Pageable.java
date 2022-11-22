package io.lhysin.advanced.mybatis.domain;

public interface Pageable {
    long getOffset();
    int getLimit();
    Sort getSort();
}