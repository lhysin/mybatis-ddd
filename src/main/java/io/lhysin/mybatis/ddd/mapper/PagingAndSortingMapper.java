package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.spec.Pageable;
import io.lhysin.mybatis.ddd.provider.PagingAndSortingSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

/**
 * PagingAndSortingMapper
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
public interface PagingAndSortingMapper<T, ID extends Serializable> extends ProviderMapper<T, ID> {

    /**
     * @param pageable {@link Pageable}
     * @return Table entities
     */
    @SelectProvider(type = PagingAndSortingSqlProvider.class, method = "findAll")
    List<T> findAll(Pageable pageable);

}
