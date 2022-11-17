package io.lhysin.myjpa.repository;

import io.lhysin.myjpa.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CustomerCRUDRepository extends CRUDRepository<Customer, String> {
}
