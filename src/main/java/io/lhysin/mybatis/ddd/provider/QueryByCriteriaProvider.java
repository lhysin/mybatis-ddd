package io.lhysin.mybatis.ddd.provider;

import java.io.Serializable;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import io.lhysin.mybatis.ddd.domain.Sort;
import io.lhysin.mybatis.ddd.spec.Criteria;
import io.lhysin.mybatis.ddd.support.SqlProviderSupport;

/**
 * QueryByCriteriaProvider
 *
 * @see SqlProviderSupport
 * @param <T>  Table Entity
 * @param <ID>  Table PK
 */
public class QueryByCriteriaProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID> {

    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * <pre>
     * create dynamic where clause SQL.
     *
     * SELECT * FROM ENTITY
     * WHERE dynamic SQL
     * ROWS FETCH FIRST 1 ROWS ONLY
     * </pre>
     * @param criteria {@link Criteria}
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String findOne(Criteria<?> criteria, ProviderContext ctx) {
        String sql = new SQL()
            .SELECT(selectColumns(ctx))
            .FROM(tableName(ctx))
            .WHERE(wheresByCriteria(criteria, ctx))
            .FETCH_FIRST_ROWS_ONLY(1)
            .toString();
        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }
        return sql;
    }

    /**
     * <pre>
     * create dynamic where clause SQL.
     *
     * SELECT * FROM ENTITY
     * WHERE dynamic SQL
     * ORDER BY COLUMN DESC
     * OFFSET ?
     * ROWS FETCH FIRST ? ROWS ONLY
     *
     * support sort and pageable
     *</pre>
     * @param criteria {@link Criteria}
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String findBy(Criteria<?> criteria, ProviderContext ctx) {
        SQL sql = new SQL()
            .SELECT(selectColumns(ctx))
            .FROM(tableName(ctx))
            .WHERE(wheresByCriteria(criteria, ctx));

        if (criteria.getPageable() != null) {
            sql.OFFSET_ROWS("#{pageable.offset}");
            sql.FETCH_FIRST_ROWS_ONLY("#{pageable.limit}");

            Sort sort = criteria.getPageable().getSort();
            if (sort != null) {
                sql.ORDER_BY(orders(sort, ctx));
            }
        } else if(criteria.getSort() != null) {
            sql.ORDER_BY(orders(criteria.getSort(), ctx));
        }
        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }
        return sql.toString();
    }

    /**
     * <pre>
     * create dynamic where clause SQL.
     *
     * SELECT count(*) FROM ENTITY
     * WHERE dynamic SQL
     * OFFSET ?
     * ROWS FETCH FIRST ? ROWS ONLY
     *
     * support pageable
     *</pre>
     * @param  criteria {@link Criteria}
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String countBy(Criteria<?> criteria, ProviderContext ctx) {
        SQL sql = new SQL()
            .SELECT("count(*)")
            .FROM(tableName(ctx))
            .WHERE(wheresByCriteria(criteria, ctx));

        if (criteria.getPageable() != null) {
            sql.OFFSET_ROWS("#{pageable.offset}");
            sql.FETCH_FIRST_ROWS_ONLY("#{pageable.limit}");
        }

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql.toString();
    }

}
