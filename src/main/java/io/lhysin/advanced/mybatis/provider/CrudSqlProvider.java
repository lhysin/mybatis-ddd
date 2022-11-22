package io.lhysin.advanced.mybatis.provider;

import io.lhysin.advanced.mybatis.support.SqlProviderSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;


@Slf4j
public class CrudSqlProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID> {

    public String save(ProviderContext ctx) {
        throw new UnsupportedOperationException("CrudSqlProvider.save() not implemented.");
    }

    public String findById(ProviderContext ctx) {
        return new SQL()
                .SELECT(this.selectColumns(ctx))
                .FROM(this.tableName(ctx))
                .WHERE(this.wheresById(ctx))
                .toString();
    }

    public String findAllById(ProviderContext ctx) {
        return "<script>".concat(
                new SQL().SELECT(this.selectColumns(ctx))
                    .FROM(this.tableName(ctx))
                    .WHERE(this.whereByIds(ctx))
                    .toString()
                ).concat("</script>");
    }

    public String count(ProviderContext ctx) {
        return new SQL()
                .SELECT("count(*)")
                .FROM(this.tableName(ctx))
                .toString();
    }

    public String deleteById(ProviderContext ctx) {
        return new SQL()
                .DELETE_FROM(this.tableName(ctx))
                .WHERE(this.wheresById(ctx))
                .toString();
    }

    public String delete(ProviderContext ctx) {
        return new SQL()
                .DELETE_FROM(this.tableName(ctx))
                .WHERE(this.wheresById(ctx))
                .toString();
    }

    public String deleteAllById(ProviderContext ctx) {
        return "<script>".concat(
                new SQL()
                    .DELETE_FROM(this.tableName(ctx))
                    .WHERE(this.whereByIds(ctx))
                    .toString()
                ).concat("</script>");
    }

    public String create(ProviderContext ctx) {
        return new SQL()
                .INSERT_INTO(this.tableName(ctx))
                .INTO_COLUMNS(this.insertIntoColumns(ctx))
                .INTO_VALUES(this.intoValues(ctx))
                .toString();
    }

    public String update(ProviderContext ctx) {
        return new SQL()
                .UPDATE(this.tableName(ctx))
                .SET(this.updateColumns(ctx))
                .WHERE(this.wheresById(ctx))
                .toString();
    }
}
