package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends CrudMapper<Customer, String>, PagingAndSortingMapper<Customer, String> {
}
