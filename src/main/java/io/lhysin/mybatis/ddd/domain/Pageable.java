package io.lhysin.mybatis.ddd.domain;

public interface Pageable {
    long getOffset();
    int getLimit();
    Sort getSort();
}