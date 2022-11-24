package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.entity.Customer;
import io.lhysin.ddd.mybatis.provider.CrudSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends CrudMapper<Customer, String>, PagingAndSortingMapper<Customer, String> {
}
