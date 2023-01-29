package io.lhysin.mybatis.ddd.domain;

import static io.lhysin.mybatis.ddd.support.ReflectionSupport.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Comparison;
import io.lhysin.mybatis.ddd.spec.Criteria;
import io.lhysin.mybatis.ddd.spec.NullHandler;
import io.lhysin.mybatis.ddd.spec.WhereClause;

/**
 * The type Typed criteria.
 *
 * @param <T>   the type parameter
 */
public class TypedCriteria<T> implements Criteria<T> {

    private final T probe;
    private final Sort sort;

    /**
     * Instantiates a new Typed criteria.
     *
     * @param probe the probe
     */
    public TypedCriteria(T probe) {
        this(probe, null);
    }

    /**
     * Instantiates a new Typed criteria.
     *
     * @param probe the probe
     * @param sort {@link Sort}
     */
    public TypedCriteria(T probe, Sort sort) {
        this.probe = probe;
        this.sort = sort;
    }

    public List<String> createWhereClause(Column column) {
        return Arrays.stream(this.getProbeType().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(WhereClause.class))
            .filter(field -> field.getDeclaredAnnotation(WhereClause.class).column().equals(column))
            .filter(field -> {
                /*
                 * if value is null, ignore where clause.
                 */
                if(NullHandler.IGNORE.equals(field.getDeclaredAnnotation(WhereClause.class).nullHandler())) {
                    return value(probe, field) != null;
                } else {
                    return true;
                }
            })
            .map(field -> this.columnAndBindParameter(column, field))
            .collect(Collectors.toList());
    }

    private String columnAndBindParameter(Column column, Field field) {
        Comparison comparison = field.getDeclaredAnnotation(WhereClause.class).comparison();
        String columnWithOperator = column.name().concat(comparison.getOperator());

        Object target = value(probe, field);
        if(Comparison.IN.equals(comparison) || Comparison.NOT_IN.equals(comparison)) {
            if(target instanceof Collection) {
                String sql = columnWithOperator.concat("(");
                String contents = IntStream.range(0, ((Collection<?>) target).size())
                    .mapToObj(i -> this.bindParameterByComparison(comparison, field.getName().concat("[" + i + "]")))
                    .collect(Collectors.joining(","));

                return sql.concat(contents).concat(")");
            } else {
                return columnWithOperator.concat("(")
                    .concat(this.bindParameterByComparison(comparison, field.getName()))
                    .concat(")");
            }
        }

        return columnWithOperator.concat(this.bindParameterByComparison(comparison, field.getName()));
    }

    private String bindParameterByComparison(Comparison comparison, String fieldName) {
        String open = Comparison.START_WITH_LIKE.equals(comparison) ||
            Comparison.ANY_LIKE.equals(comparison) ? "'%'||" : "";
        String close = Comparison.END_WITH_LIKE.equals(comparison) ||
            Comparison.ANY_LIKE.equals(comparison) ? "||'%'" : "";

        return String.format("%s#{probe.%s}%s", open, fieldName, close);
    }

    @Override
    public T getProbe() {
        return this.probe;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }
}
