package io.lhysin.myjpa.repository;

import io.lhysin.myjpa.entity.Customer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CRUDRepository<T, ID>{

    @SelectProvider(type= CRUDRepositoryImpl.CrudProvider.class, method="findById")
    Optional<T> findById(ID id);

    Iterable<T> findAll();

    void delete(ID id);

    <S extends T> S save(ID id);

}
