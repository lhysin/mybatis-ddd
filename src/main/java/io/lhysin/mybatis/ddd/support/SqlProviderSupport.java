package io.lhysin.mybatis.ddd.support;

import static io.lhysin.mybatis.ddd.support.ReflectionSupport.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.ibatis.builder.annotation.ProviderContext;

import io.lhysin.mybatis.ddd.domain.Sort;
import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Criteria;

/**
 * SqlProviderSupport
 *
 * @see ProviderContextSupport
 * @param <T> Table Entity
 * @param <ID> Table PK
 */
public abstract class SqlProviderSupport<T, ID extends Serializable> extends ProviderContextSupport<T, ID> {

    /**
     * @param ctx {@link ProviderContext}
     * @return select column array {@link String[]}
     */
    protected String[] selectColumns(ProviderContext ctx) {
        return this.columns(ctx)
            .map(this::columnNameAndAliasField)
            .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return insert into column array {@link String[]}
     */
    protected String[] insertIntoColumns(ProviderContext ctx) {
        return this.columns(ctx)
            .filter(this::insertable)
            .map(this::columnName)
            .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return update column array {@link String[]}
     */
    protected String[] updateColumns(ProviderContext ctx) {
        return this.conditionalUpdateColumns(null, ctx);
    }

    /**
     * @param domain domain {@link T}
     * @param ctx {@link ProviderContext}
     * @return dynamic update column array {@link String[]}
     */
    protected String[] dynamicUpdateColumns(T domain, ProviderContext ctx) {
        return this.conditionalUpdateColumns(domain, ctx);
    }

    /**
     * @param domain domain {@link T}
     * @param ctx {@link ProviderContext}
     * @return dynamic or update column array {@link String[]}
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
     * @return into value bind value array {@link String[]}
     */
    protected String[] intoValues(ProviderContext ctx) {
        return this.columns(ctx)
            .filter(this::insertable)
            .map(field -> bindParameter(field.getName()))
            .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return foreach into value bind value array {@link String}
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

    /**
     * @param criteria {@link Criteria}
     * @param ctx {@link ProviderContext}
     * @return where clause sql array {@link String[]}
     */
    protected String[] wheresByCriteria(Criteria<?> criteria, ProviderContext ctx) {
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
     * @return where column and bind value array {@link String[]}
     */
    protected String[] wheresById(ProviderContext ctx) {
        return this.onlyIdColumns(ctx)
            .map(this::columnNameAndBindParameter)
            .toArray(String[]::new);
    }

    /**
     * @param ctx {@link ProviderContext}
     * @return foreach where column and bind value array {@link String}
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
     * @param sort {@link Sort}
     * @param ctx {@link ProviderContext}
     * @return order by array {@link String[]}
     */
    protected String[] orders(Sort sort, ProviderContext ctx) {

        if (sort == null) {
            return new String[0];
        }

        Map<String, String> fieldAndColumn = this.columns(ctx)
            .collect(Collectors.toMap(
                Field::getName,
                field -> field.getAnnotation(Column.class).name()
                )
            );

        return sort.getOrders().stream()
            .filter(order -> fieldAndColumn.containsKey(order.getProperty()))
            /*
             * e.g.
             * ORD_NO DESC
             */
            .map(order -> String.format("%s %s", fieldAndColumn.get(order.getProperty()), order.getDirection()))
            .toArray(String[]::new);
    }

}
