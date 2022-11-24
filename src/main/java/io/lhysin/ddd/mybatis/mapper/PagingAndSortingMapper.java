package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.domain.Pageable;
import io.lhysin.ddd.mybatis.provider.PagingAndSortingSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.List;

public interface PagingAndSortingMapper<T, ID extends Serializable> extends ProvierMapper {

    @SelectProvider(type = PagingAndSortingSqlProvider.class, method = "findAll")
    List<T> findAll(Pageable pageable);

}
