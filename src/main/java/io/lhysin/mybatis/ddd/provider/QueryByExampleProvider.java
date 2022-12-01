package io.lhysin.mybatis.ddd.provider;

import io.lhysin.mybatis.ddd.spec.Example;
import io.lhysin.mybatis.ddd.support.SqlProviderSupport;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;

/**
 * CrudSqlProvider
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
public class QueryByExampleProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID> implements ProviderMethodResolver {


    /**
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String findOne(Example<T> example, ProviderContext ctx) {
        return new SQL()
                .SELECT(selectColumns(ctx))
                .FROM(tableName(ctx))
                .WHERE(wheresByExample(example ,ctx))
                .FETCH_FIRST_ROWS_ONLY(1)
                .toString();
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String findBy(Example<T> example, ProviderContext ctx) {
        return new SQL()
                .SELECT(selectColumns(ctx))
                .FROM(tableName(ctx))
                .WHERE(wheresByExample(example ,ctx))
                .toString();
    }
}
