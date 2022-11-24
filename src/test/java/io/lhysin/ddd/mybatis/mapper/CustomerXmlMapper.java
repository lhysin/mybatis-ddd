package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.entity.Customer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerXmlMapper {

    Optional<Customer> findById(@Param("custNo") String custNo);
}
