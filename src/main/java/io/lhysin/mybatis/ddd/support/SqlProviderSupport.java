package io.lhysin.mybatis.ddd.support;

import io.lhysin.mybatis.ddd.domain.Pageable;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * SqlProviderSupport
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
public abstract class SqlProviderSupport<T, ID extends Serializable> extends ProviderContextSupport<T, ID> {

    /**
     * @param ctx {@link ProviderContext}
     * @return select column array
     */
    protected String[] selectColumns(ProviderContext ctx) {
        return this.columns(ctx)
                .map(this::columnNameAndAliasField)
                .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return insert into column array
     */
    protected String[] insertIntoColumns(ProviderContext ctx) {
        return this.columns(ctx)
                .filter(this::insertable)
                .map(this::columnName)
                .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return update column array
     */
    protected String[] updateColumns(ProviderContext ctx) {
        return this.conditionalUpdateColumns(null, ctx);
    }

    /**
     * @param domain 
     * @param ctx
     * @return dynamic update column array
     */
    protected String[] dynamicUpdateColumns(T domain, ProviderContext ctx) {
        return this.conditionalUpdateColumns(domain, ctx);
    }

    /**
     * @param domain 
     * @param ctx
     * @return dynamic or update column array
     */
    private String[] conditionalUpdateColumns(T domain, ProviderContext ctx) {

        Predicate<Field> isDynamicUpdate = field -> value(domain, field) != null;
        if (domain == null) {
            isDynamicUpdate = field -> true;
        }

        return this.withoutIdColumns(ctx)
                .filter(this::updatable)
                .filter(isDynamicUpdate)
                .map(this::columnNameAndBindParameter)
                .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return into value bind value array
     */
    protected String[] intoValues(ProviderContext ctx) {
        return this.columns(ctx)
                .filter(this::insertable)
                .map(field -> bindParameter(field.getName()))
                .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return foreach into value bind value array
     */
    protected String bulkIntoValues(ProviderContext ctx) {
        String key = "entity";
        String joiningFields = this.columns(ctx)
                .filter(this::insertable)
                .map(field -> bindParameterWithKey(field.getName(), key))
                .collect(Collectors.joining(","));

        return "<foreach item='".concat(key).concat("' collection='entities' separator=','>")
                .concat("\n").concat("(").concat(joiningFields).concat(")")
                .concat("\n</foreach>");
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return where column and bind value array
     */
    protected String[] wheresById(ProviderContext ctx) {
        return this.onlyIdColumns(ctx)
                .map(this::columnNameAndBindParameter)
                .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return foreach where column and bind value array
     */
    protected String whereByIds(ProviderContext ctx) {

        // FIRST_NAME or FIRST_NAME, LAST_NAME
        String joiningColumns = this.onlyIdColumns(ctx)
                .map(this::columnName)
                .collect(Collectors.joining(","));

        String key = "id";

        Supplier<String> compositeCase = () ->
                "(".concat(this.onlyIdColumns(ctx)
                        .map(field -> this.bindParameterWithKey(field.getName(), key))
                        .collect(Collectors.joining(","))).concat(")");

        // (#{id.firstName}, #{id.lastName}) or #{id}
        String joiningFields = this.isCompositeKey(ctx) ? compositeCase.get() : this.bindParameter(key);

        return "(".concat(joiningColumns).concat(")")
                .concat("<foreach item='id' collection='ids' open=' IN (' separator=',' close=')'>")
                .concat("\n").concat(joiningFields)
                .concat("\n</foreach>");
    }

    /**
     * @param pageable {@link Pageable}
     * @param ctx {@link ProviderContext}
     * @return order by array
     */
    protected String[] orders(Pageable pageable, ProviderContext ctx) {
        List<String> allColumns = this.columns(ctx)
                .map(this::columnName)
                .collect(Collectors.toList());

        return pageable.getSort().getOrders().stream()
                .filter(order -> allColumns.contains(order.getProperty()))
                .map(order -> order.getProperty().concat(" ").concat(order.getDirection().name()))
                .toArray(String[]::new);
    }

    /**
     * @param obj target
     * @param field target field
     * @return target value
     */
    protected Object value(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(false);
        }
    }

}
