package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.entity.Dummy;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyMapper extends CrudMapper<Dummy, String> {
}
