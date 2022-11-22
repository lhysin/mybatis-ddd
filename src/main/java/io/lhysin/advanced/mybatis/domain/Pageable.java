package io.lhysin.advanced.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public interface Pageable {
    long getOffset();
    int getLimit();
    Sort getSort();
}