package io.lhysin.myjpa.repository;

import io.lhysin.myjpa.config.CrudRepository;
import io.lhysin.myjpa.entity.Customer;
import org.springframework.stereotype.Component;

public interface CustomerRepository extends CrudRepository<Customer, String> {
}
