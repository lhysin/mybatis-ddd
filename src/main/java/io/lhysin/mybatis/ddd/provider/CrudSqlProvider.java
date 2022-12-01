package io.lhysin.mybatis.ddd.provider;

import java.io.Serializable;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;
import org.apache.ibatis.jdbc.SQL;

import io.lhysin.mybatis.ddd.support.SqlProviderSupport;

/**
 * CrudSqlProvider
 * @param <T>  Table Entity
 * @param <ID>  Table PK
 */
public class CrudSqlProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID>
    implements ProviderMethodResolver {

    /**
     * Not Implemented.
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String save(ProviderContext ctx) {
        throw new UnsupportedOperationException("CrudSqlProvider.save() Not Implemented.");
    }

    /**
     * Find by id string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String findById(ProviderContext ctx) {
        return new SQL()
            .SELECT(this.selectColumns(ctx))
            .FROM(this.tableName(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();
    }

    /**
     * Find all by id string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String findAllById(ProviderContext ctx) {
        return "<script>".concat(
            new SQL().SELECT(this.selectColumns(ctx))
                .FROM(this.tableName(ctx))
                .WHERE(this.whereByIds(ctx))
                .toString()
        ).concat("</script>");
    }

    /**
     * Count string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String count(ProviderContext ctx) {
        return new SQL()
            .SELECT("count(*)")
            .FROM(this.tableName(ctx))
            .toString();
    }

    /**
     * Delete by id string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String deleteById(ProviderContext ctx) {
        return new SQL()
            .DELETE_FROM(this.tableName(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();
    }

    /**
     * Delete string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String delete(ProviderContext ctx) {
        return new SQL()
            .DELETE_FROM(this.tableName(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();
    }

    /**
     * Delete all by id string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String deleteAllById(ProviderContext ctx) {
        return "<script>".concat(
            new SQL()
                .DELETE_FROM(this.tableName(ctx))
                .WHERE(this.whereByIds(ctx))
                .toString()
        ).concat("</script>");
    }

    /**
     * Create string.
     *
     * @param domin the domin
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String create(T domin, ProviderContext ctx) {
        return new SQL()
            .INSERT_INTO(this.tableName(ctx))
            .INTO_COLUMNS(this.insertIntoColumns(ctx))
            .INTO_VALUES(this.intoValues(ctx))
            .toString();
    }

    /**
     * Update string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String update(ProviderContext ctx) {
        return new SQL()
            .UPDATE(this.tableName(ctx))
            .SET(this.updateColumns(ctx))
            .WHERE(this.wheresById(ctx))
            .toString();
    }

    /**
     * Dynamic update string.
     *
     * @param domain Table entity
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String dynamicUpdate(T domain, ProviderContext ctx) {
        return new SQL()
            .UPDATE(this.tableName(ctx))
            .SET(this.dynamicUpdateColumns(domain, ctx))
            .WHERE(this.wheresById(ctx))
            .toString();
    }

    /**
     * Create all string.
     *
     * @param ctx {@link ProviderContext}
     * @return dynamic SQL
     */
    public String createAll(ProviderContext ctx) {
        return "<script>".concat(
                new SQL()
                    .INSERT_INTO(this.tableName(ctx))
                    .INTO_COLUMNS(this.insertIntoColumns(ctx))
                    .toString()
            ).concat(" VALUES ")
            .concat(this.bulkIntoValues(ctx))
            .concat("</script>");
    }
}
