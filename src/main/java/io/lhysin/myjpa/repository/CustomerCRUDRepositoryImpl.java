package io.lhysin.myjpa.repository;

import io.lhysin.myjpa.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerCRUDRepositoryImpl implements CustomerCRUDRepository {

    private final CRUDRepositoryImpl<Customer, String> crudRepositoryImpl;

    public CustomerCRUDRepositoryImpl(SqlSessionFactory sqlSessionFactory) {
        this.crudRepositoryImpl = new CRUDRepositoryImpl(sqlSessionFactory, Customer.class, String.class);
    }

    @Override
    public Optional<Customer> findById(String s) {
        return crudRepositoryImpl.findById(s);
    }

    @Override
    public Iterable<Customer> findAll() {
        return crudRepositoryImpl.findAll();
    }

    @Override
    public void delete(String s) {
        crudRepositoryImpl.delete(s);
    }

    @Override
    public <S extends Customer> S save(String s) {
        return crudRepositoryImpl.save(s);
    }
}
