package io.lhysin.mybatis.ddd.provider;

import java.io.Serializable;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import io.lhysin.mybatis.ddd.spec.Criteria;
import io.lhysin.mybatis.ddd.support.SqlProviderSupport;

/**
 * QueryByCriteriaProvider
 *
 * @param <T>  Table Entity
 * @param <ID>  Table PK
 */
public class QueryByCriteriaProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID>
    implements ProviderMethodResolver {

    /**
     * @param criteria {@link Criteria}
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String findOne(Criteria<T> criteria, ProviderContext ctx) {
        return new SQL()
            .SELECT(selectColumns(ctx))
            .FROM(tableName(ctx))
            .WHERE(wheresByCriteria(criteria, ctx))
            .ORDER_BY(this.orders(criteria.getSort(), ctx))
            .FETCH_FIRST_ROWS_ONLY(1)
            .toString();
    }

    /**
     * @param criteria {@link Criteria}
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String findBy(Criteria<T> criteria, ProviderContext ctx) {
        return new SQL()
            .SELECT(selectColumns(ctx))
            .FROM(tableName(ctx))
            .WHERE(wheresByCriteria(criteria, ctx))
            .ORDER_BY(this.orders(criteria.getSort(), ctx))
            .toString();
    }

    /**
     * @param  criteria {@link Criteria}
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String countBy(Criteria<T> criteria, ProviderContext ctx) {
        return new SQL()
            .SELECT("count(*)")
            .FROM(tableName(ctx))
            .WHERE(wheresByCriteria(criteria, ctx))
            .toString();
    }
}
