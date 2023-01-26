package io.lhysin.mybatis.ddd.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.SelectProvider;

import io.lhysin.mybatis.ddd.provider.QueryByCriteriaProvider;
import io.lhysin.mybatis.ddd.spec.Criteria;

public interface QueryByCriteriaMapper<T, ID extends Serializable> extends MapperProvider<T, ID> {

    @SelectProvider(type = QueryByCriteriaProvider.class)
    Optional<T> findOne(Criteria<?> criteria);

    @SelectProvider(type = QueryByCriteriaProvider.class)
    List<T> findBy(Criteria<?> criteria);

}
