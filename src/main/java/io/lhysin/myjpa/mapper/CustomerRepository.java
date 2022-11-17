package io.lhysin.myjpa.mapper;

import io.lhysin.myjpa.entity.Customer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository {

    //Customer findById(@Param("custNo") String custNo);
}
