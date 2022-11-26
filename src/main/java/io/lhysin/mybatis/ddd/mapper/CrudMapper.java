package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.provider.CrudSqlProvider;
import org.apache.ibatis.annotations.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * CrudMapper
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
public interface CrudMapper<T, ID extends Serializable> extends ProviderMapper<T, ID> {

    /**
     * @param entity Table Entity
     * @return save Count
     */
    @InsertProvider(type = CrudSqlProvider.class, method = "save")
    int save(T entity);

    /**
     * @param id Table PK
     * @return find Table Entity
     */
    @SelectProvider(type = CrudSqlProvider.class, method = "findById")
    Optional<T> findById(ID id);

    /**
     * @param ids Table PKs
     * @return find Table Entities
     */
    @SelectProvider(type = CrudSqlProvider.class, method = "findAllById")
    List<T> findAllById(@Param("ids") Iterable<ID> ids);

    /**
     * @return find Table count
     */
    @SelectProvider(type = CrudSqlProvider.class, method = "count")
    long count();

    /**
     * @param id Table PK
     * @return delete count
     */
    @DeleteProvider(type = CrudSqlProvider.class, method = "deleteById")
    int deleteById(ID id);

    /**
     * @param entity Table entity
     * @return delete count
     */
    @DeleteProvider(type = CrudSqlProvider.class, method = "delete")
    int delete(T entity);

    /**
     * @param ids Table entities
     * @return delete count
     */
    @DeleteProvider(type = CrudSqlProvider.class, method = "deleteAllById")
    int deleteAllById(@Param("ids") Iterable<ID> ids);

    /**
     * @param entity Table entity
     * @return create count
     */
    @InsertProvider(type = CrudSqlProvider.class, method = "create")
    int create(T entity);

    /**
     * update all column
     * @param entity Table entity
     * @return update count
     */
    @UpdateProvider(type = CrudSqlProvider.class, method = "update")
    int update(T entity);

    /**
     * update nonnull column
     * @param entity Table entity
     * @return update count
     */
    @UpdateProvider(type = CrudSqlProvider.class, method = "dynamicUpdate")
    int dynamicUpdate(T entity);

    /**
     * bulk insert
     * @param entities Table entities
     * @return create count
     */
    @InsertProvider(type = CrudSqlProvider.class, method = "createAll")
    int createAll(@Param("entities") Iterable<T> entities);

}
