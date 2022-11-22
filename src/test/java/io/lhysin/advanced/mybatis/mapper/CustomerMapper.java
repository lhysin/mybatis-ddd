package io.lhysin.advanced.mybatis.mapper;

import io.lhysin.advanced.mybatis.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends CrudMapper<Customer, String>, PagingAndSortingMapper<Customer, String> {
}
