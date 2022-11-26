package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.entity.Customer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerXmlMapper {

    Optional<Customer> findById(@Param("custNo") String custNo);
}
