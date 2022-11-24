package io.lhysin.ddd.mybatis.domain;

public interface Pageable {
    long getOffset();
    int getLimit();
    Sort getSort();
}