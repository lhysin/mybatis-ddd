package io.lhysin.advanced.mybatis.repository;

import io.lhysin.advanced.mybatis.config.CrudRepository;
import io.lhysin.advanced.mybatis.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {
}
