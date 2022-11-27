package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.entity.Customer;
import org.springframework.stereotype.Repository;

/**
 * The interface Customer mapper.
 */
@Repository
public interface CustomerMapper extends CrudMapper<Customer, String>, PagingAndSortingMapper<Customer, String> {
}
