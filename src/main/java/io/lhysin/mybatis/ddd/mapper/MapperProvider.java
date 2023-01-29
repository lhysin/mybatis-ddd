package io.lhysin.mybatis.ddd.mapper;

import java.io.Serializable;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

/**
 * MapperProvider
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
interface MapperProvider<T, ID extends Serializable> extends ProviderMethodResolver {
}
