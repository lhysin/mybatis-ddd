package io.lhysin.mybatis.ddd.mapper;

import org.springframework.stereotype.Repository;

import io.lhysin.mybatis.ddd.entity.Dummy;

/**
 * The interface Dummy mapper.
 */
@Repository
public interface DummyMapper extends CrudMapper<Dummy, String> {
}
