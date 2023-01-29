package io.lhysin.mybatis.ddd.provider;

import java.io.Serializable;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import io.lhysin.mybatis.ddd.spec.Pageable;
import io.lhysin.mybatis.ddd.support.SqlProviderSupport;

/**
 * PagingAndSortingSqlProvider
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
public class PagingAndSortingSqlProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID>
    implements ProviderMethodResolver {

    /**
     * @param pageable {@link Pageable}
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String findAll(Pageable pageable, ProviderContext ctx) {
        return new SQL()
            .SELECT(this.selectColumns(ctx))
            .FROM(this.tableName(ctx))
            // TODO dynamic CriteriaQuery
            //.WHERE(this.wheresById(ctx))
            .ORDER_BY(this.orders(pageable.getSort(), ctx))
            .OFFSET_ROWS("#{offset}")
            .FETCH_FIRST_ROWS_ONLY("#{limit}")
            .toString();
    }
}
