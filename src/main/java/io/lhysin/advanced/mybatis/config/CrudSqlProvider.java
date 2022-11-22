package io.lhysin.advanced.mybatis.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Column;


@Slf4j
public class CrudSqlProvider<T, ID> extends CrudSqlProviderSupport<T, ID> {

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

    public String findAllById(Iterable<ID> ids, ProviderContext ctx) {
        throw new UnsupportedOperationException("CrudSqlProvider.findAllById() not implemented.");
       /* return new SQL(){{
            SELECT(selectColumns(ctx));
            FROM(tableName(ctx));
            WHERE("1==1");

            for(ID id : ids) {
                OR();
                WHERE(wheresByIds(ctx));
            }

        }}.toString();
        return new SQL().SELECT(this.selectColumns(ctx))
                .FROM(this.tableName(ctx))
                .WHERE(this.wheresByIds(ctx))
                .toString();*/
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
        throw new UnsupportedOperationException("CrudSqlProvider.deleteAllById() not implemented.");
        /*return new SQL()
                .DELETE_FROM(this.tableName(ctx))
                .WHERE(this.wheresById(ctx))
                .toString();*/
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

    private String[] selectColumns(ProviderContext ctx) {
        return this.fields(ctx)
                .map(this::bindColumnAndAliasField)
                .toArray(String[]::new);
    }

    private String[] insertIntoColumns(ProviderContext ctx) {
        return this.fields(ctx)
                .map(this::bindColumn)
                .toArray(String[]::new);
    }

    private String[] updateColumns(ProviderContext ctx) {
        return this.withoutIdFields(ctx)
                .map(this::bindColumnAndParameter)
                .toArray(String[]::new);
    }

    private String[] intoValues(ProviderContext ctx) {
        return this.fields(ctx)
                .map(field -> bindParameter(field.getName()))
                .toArray(String[]::new);
    }

    private String[] wheresById(ProviderContext ctx) {
        return this.onlyIdFields(ctx)
                .map(this::bindColumnAndParameter)
                .toArray(String[]::new);
    }

    private String[] wheresByIds(ProviderContext ctx) {
        return this.onlyIdFields(ctx)
                .map(field -> {
                    return field.getAnnotation(Column.class).name() + " IN ${ids}";
                })
                .toArray(String[]::new);
    }

}
