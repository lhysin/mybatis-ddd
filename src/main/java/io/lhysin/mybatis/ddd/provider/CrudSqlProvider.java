package io.lhysin.mybatis.ddd.provider;

import java.io.Serializable;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import io.lhysin.mybatis.ddd.support.SqlProviderSupport;

/**
 * CrudSqlProvider
 *
 * @see SqlProviderSupport
 * @param <T>  Table Entity
 * @param <ID>  Table PK
 */
public class CrudSqlProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID> {

    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Not Implemented.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String save(ProviderContext ctx) {
        throw new UnsupportedOperationException("CrudSqlProvider.save() Not Implemented.");
    }

    /**
     * Find by id.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String findById(ProviderContext ctx) {
        String sql = new SQL()
            .SELECT(this.selectColumns(ctx))
            .FROM(this.tableName(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Find all by ids.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String findAllById(ProviderContext ctx) {
        String sql = "<script>".concat(
            new SQL().SELECT(this.selectColumns(ctx))
                .FROM(this.tableName(ctx))
                .WHERE(this.whereByIds(ctx))
                .toString()
        ).concat("</script>");

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Count.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String count(ProviderContext ctx) {
        String sql = new SQL()
            .SELECT("count(*)")
            .FROM(this.tableName(ctx))
            .toString();

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Delete by id.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String deleteById(ProviderContext ctx) {
        String sql = new SQL()
            .DELETE_FROM(this.tableName(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Delete by entity.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String delete(ProviderContext ctx) {
        String sql = new SQL()
            .DELETE_FROM(this.tableName(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Delete all by ids.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String deleteAllById(ProviderContext ctx) {
        String sql = "<script>".concat(
            new SQL()
                .DELETE_FROM(this.tableName(ctx))
                .WHERE(this.whereByIds(ctx))
                .toString()
        ).concat("</script>");

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Insert by entity.
     *
     * @param domin the domin
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String create(T domin, ProviderContext ctx) {
        String sql = new SQL()
            .INSERT_INTO(this.tableName(ctx))
            .INTO_COLUMNS(this.insertIntoColumns(ctx))
            .INTO_VALUES(this.intoValues(ctx))
            .toString();

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Update by entity.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String update(ProviderContext ctx) {
        String sql = new SQL()
            .UPDATE(this.tableName(ctx))
            .SET(this.updateColumns(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Dynamic Update by entity.
     *
     * @param domain Table entity
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String dynamicUpdate(T domain, ProviderContext ctx) {
        String sql = new SQL()
            .UPDATE(this.tableName(ctx))
            .SET(this.dynamicUpdateColumns(domain, ctx))
            .WHERE(this.wheresById(ctx))
            .toString();

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }

    /**
     * Bulk Insert by entities
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL {@link String}
     */
    public String createAll(ProviderContext ctx) {
        String sql = "<script>".concat(
                new SQL()
                    .INSERT_INTO(this.tableName(ctx))
                    .INTO_COLUMNS(this.insertIntoColumns(ctx))
                    .toString()
            ).concat(" VALUES ")
            .concat(this.bulkIntoValues(ctx))
            .concat("</script>");

        if(log.isTraceEnabled()) {
            log.trace("created sql : " + sql);
        }

        return sql;
    }
}
