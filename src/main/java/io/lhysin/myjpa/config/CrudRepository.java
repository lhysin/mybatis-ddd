package io.lhysin.myjpa.config;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.Optional;

public interface CrudRepository<T, ID extends Serializable> {

    @SelectProvider(type = CrudSqlProvider.class, method = "findById")
    Optional<T> findById(ID id);

    @DeleteProvider(type = CrudSqlProvider.class, method = "delete")
    void delete(ID id);

    @InsertProvider(type = CrudSqlProvider.class, method = "save")
    void save(T domain);
}
