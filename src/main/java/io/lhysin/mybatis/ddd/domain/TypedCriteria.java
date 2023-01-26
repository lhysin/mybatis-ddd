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
import io.lhysin.mybatis.ddd.spec.WhereClause;

public class TypedCriteria<T> implements Criteria<T> {

    private final T probe;
    public TypedCriteria(T probe) {
        this.probe = probe;
    }

    public List<String> createWhereClause(Column column) {
        return Arrays.stream(this.getProbeType().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(WhereClause.class))
            .filter(field -> field.getDeclaredAnnotation(WhereClause.class).column().equals(column))
            .filter(field -> {
                if(field.getDeclaredAnnotation(WhereClause.class).ignoreNullValue()) {
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
        String open = "#{probe.";
        String close = "}";

        if(Comparison.START_WITH_LIKE.equals(comparison)) {
            close = close.concat("||'%'");
        } else if(Comparison.END_WITH_LIKE.equals(comparison)) {
            open = "'%'||".concat(open);
        } else if(Comparison.ANY_LIKE.equals(comparison)) {
            open = "'%'||".concat(open);
            close = close.concat("||'%'");
        }

        return open.concat(fieldName).concat(close);
    }

    @Override
    public T getProbe() {
        return this.probe;
    }
}
