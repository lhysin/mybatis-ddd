package io.lhysin.mybatis.ddd.domain;

/**
 * PageRequest
 */
public class PageRequest implements Pageable {

    /**
     * page number
     */
    private final int page;

    /**
     * page size
     */
    private final int size;

    /**
     * sort
     */
    private final Sort sort;

    /**
     * @param page page
     * @param size size
     * @param sort sort
     */
    public PageRequest(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    /**
     * create PageRequest
     * @param page page
     * @param size size
     * @return PageRequest
     */
    public static PageRequest of(int page, int size) {
        return PageRequest.of(page, size, null);
    }

    /**
     * create PageRequest
     * @param page page
     * @param size size
     * @param sort sort
     * @return PageRequest
     */
    public static PageRequest of(int page, int size, Sort sort) {
        return new PageRequest(page, size, sort);
    }

    /**
     * @return offset
     */
    @Override
    public long getOffset() {
        return (long) page * (long) size;
    }

    /**
     * @return limit
     */
    @Override
    public int getLimit() {
        return this.size;
    }

    /**
     * @return sort
     */
    @Override
    public Sort getSort() {
        return this.sort;
    }
}