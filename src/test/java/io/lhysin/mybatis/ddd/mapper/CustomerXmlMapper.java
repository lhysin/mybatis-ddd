package io.lhysin.mybatis.ddd.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import io.lhysin.mybatis.ddd.entity.Customer;

/**
 * The interface Customer xml mapper.
 */
@Repository
public interface CustomerXmlMapper {

	/**
	 * Find by id optional.
	 *
	 * @param custNo the cust no
	 * @return the optional
	 */
	Optional<Customer> findById(@Param("custNo") String custNo);
}
