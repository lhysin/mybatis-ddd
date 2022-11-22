package io.lhysin.advanced.mybatis.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PageRequest implements Pageable {

    private int page;

    private int size;

    private Sort sort;

    public static PageRequest of(int page, int size) {
        return PageRequest.of(page, size, null);
    }

    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    @Override
    public long getOffset() {
        return (long) page * (long) size;
    }

    @Override
    public int getLimit() {
        return this.size;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }
}