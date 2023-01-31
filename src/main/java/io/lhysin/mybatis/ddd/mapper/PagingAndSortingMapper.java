package io.lhysin.mybatis.ddd.mapper;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import io.lhysin.mybatis.ddd.provider.PagingAndSortingSqlProvider;
import io.lhysin.mybatis.ddd.spec.Pageable;

/**
 * PagingAndSortingMapper
 *
 * @see PagingAndSortingSqlProvider
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
public interface PagingAndSortingMapper<T, ID extends Serializable> {

    /**
     * @param pageable {@link Pageable}
     * @return Table entities {@link List}&lt;{@link T}&gt;
     */
    @SelectProvider(type = PagingAndSortingSqlProvider.class)
    List<T> findAll(Pageable pageable);

}
