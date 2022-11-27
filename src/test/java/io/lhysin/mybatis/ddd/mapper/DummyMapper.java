package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.entity.Dummy;
import org.springframework.stereotype.Repository;

/**
 * The interface Dummy mapper.
 */
@Repository
public interface DummyMapper extends CrudMapper<Dummy, String> {
}
