package io.lhysin.mybatis.ddd.mapper;

import org.springframework.stereotype.Repository;

import io.lhysin.mybatis.ddd.entity.Customer;

/**
 * The interface Customer mapper.
 */
@Repository
public interface CustomerMapper extends CrudMapper<Customer, String>, PagingAndSortingMapper<Customer, String> {
}
