package io.lhysin.mybatis.ddd.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.SelectProvider;

import io.lhysin.mybatis.ddd.provider.QueryByExampleProvider;
import io.lhysin.mybatis.ddd.spec.Example;

/**
 * The interface Query by example mapper.
 *
 * @param <T>  the type parameter
 * @param <ID> the type parameter
 */
public interface QueryByExampleMapper<T, ID extends Serializable> extends MapperProvider<T, ID> {

	/**
	 * find One Sql by Example NotNull prove Field Value.
	 *
	 * @param example the example
	 * @return the optional
	 */
	@SelectProvider(type = QueryByExampleProvider.class, method = "findOne")
	Optional<T> findOne(Example<T> example);

	/**
	 * find List Sql by Example NotNull prove Field Value.
	 *
	 * @param example the example
	 * @return the list
	 */
	@SelectProvider(type = QueryByExampleProvider.class, method = "findBy")
	List<T> findBy(Example<T> example);

}
