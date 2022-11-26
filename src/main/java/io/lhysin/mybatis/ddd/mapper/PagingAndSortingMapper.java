package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.domain.Pageable;
import io.lhysin.mybatis.ddd.provider.PagingAndSortingSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

public interface PagingAndSortingMapper<T, ID extends Serializable> extends ProvierMapper<T, ID> {

    @SelectProvider(type = PagingAndSortingSqlProvider.class, method = "findAll")
    List<T> findAll(Pageable pageable);

}
