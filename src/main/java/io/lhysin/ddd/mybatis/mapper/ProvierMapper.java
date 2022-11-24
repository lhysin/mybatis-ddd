package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.provider.CrudSqlProvider;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface ProvierMapper<T, ID extends Serializable> {
}
