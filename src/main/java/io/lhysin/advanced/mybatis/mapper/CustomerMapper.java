package io.lhysin.advanced.mybatis.mapper;

import io.lhysin.advanced.mybatis.entity.Customer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerMapper {

    Optional<Customer> findById(@Param("custNo") String custNo);
}
