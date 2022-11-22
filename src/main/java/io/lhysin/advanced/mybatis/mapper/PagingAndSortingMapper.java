package io.lhysin.advanced.mybatis.mapper;

import io.lhysin.advanced.mybatis.domain.Pageable;
import io.lhysin.advanced.mybatis.domain.Sort;
import io.lhysin.advanced.mybatis.provider.PagingAndSortingSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

public interface PagingAndSortingMapper<T, ID extends Serializable> {

    @SelectProvider(type = PagingAndSortingSqlProvider.class, method = "findAll")
    List<T> findAll(Pageable pageable);

}
