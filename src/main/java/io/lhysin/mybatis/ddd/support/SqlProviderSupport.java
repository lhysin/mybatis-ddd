package io.lhysin.mybatis.ddd.support;

import static io.lhysin.mybatis.ddd.support.ReflectionSupport.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.ibatis.builder.annotation.ProviderContext;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Criteria;
import io.lhysin.mybatis.ddd.spec.Pageable;

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
     * @param domain  domain
     * @param ctx {@link ProviderContext}
     * @return dynamic update column array
     */
    protected String[] dynamicUpdateColumns(T domain, ProviderContext ctx) {
        return this.conditionalUpdateColumns(domain, ctx);
    }

    /**
     * @param domain  domain
     * @param ctx {@link ProviderContext}
     * @return dynamic or update column array
     */
    private String[] conditionalUpdateColumns(T domain, ProviderContext ctx) {

        Predicate<Field> isDynamicUpdate = field -> value(domain, field) != null;
        if (domain == null) {
            isDynamicUpdate = field -> true;
        }

        List<String> columns = this.withoutIdColumns(ctx)
            .filter(this::updatable)
            .filter(isDynamicUpdate)
            .map(this::columnNameAndBindParameter)
            .collect(Collectors.toList());

        if (columns.isEmpty()) {
            throw new IllegalArgumentException("Not Exists Updatable Value.");
        }

        return columns.toArray(new String[0]);
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

        if (joiningFields.isEmpty()) {
            throw new IllegalArgumentException("Not Exists insertable Value.");
        }

        return "<foreach item='".concat(key).concat("' collection='entities' separator=','>")
            .concat("\n").concat("(").concat(joiningFields).concat(")")
            .concat("\n</foreach>");
    }

    protected String[] wheresByCriteria(Criteria<T> criteria, ProviderContext ctx) {
        return this.columns(ctx)
            .map(field -> criteria.createWhereClause(field.getDeclaredAnnotation(Column.class)))
            .flatMap(Collection::stream)
            .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
            .filter(it -> !it.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("Not Allow Empty Where Clause."))
            .toArray(new String[0]);

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

}
