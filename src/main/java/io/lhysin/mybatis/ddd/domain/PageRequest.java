package io.lhysin.mybatis.ddd.domain;

public class PageRequest implements Pageable {

    private final int page;

    private final int size;

    private final Sort sort;

    public PageRequest(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

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