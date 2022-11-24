package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends CrudMapper<Customer, String>, PagingAndSortingMapper<Customer, String> {
}
