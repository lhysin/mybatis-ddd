package io.lhysin.mybatis.ddd.spec;

import static io.lhysin.mybatis.ddd.support.ReflectionSupport.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.lhysin.mybatis.ddd.domain.Sort;

/**
 * The type Typed criteria.
 *
 * @see Criteria
 * @param <T>   the type parameter
 */
class TypedCriteria<T> implements Criteria<T> {

    private final T probe;
    private final Pageable pageable;
    private final Sort sort;

    /**
     * Instantiates a new Typed criteria.
     *
     * @param probe the probe
     */
    public TypedCriteria(T probe) {
        this(probe, (Pageable)null);
    }

    /**
     * @param probe the probe
     * @param pageable {@link Pageable}
     */
    public TypedCriteria(T probe, Pageable pageable) {
        this.probe = probe;
        this.pageable = pageable;
        this.sort = null;
    }

    /**
     * @param probe the probe
     * @param sort {@link Sort}
     */
    public TypedCriteria(T probe, Sort sort) {
        this.probe = probe;
        this.sort = sort;
        this.pageable = null;
    }

    public List<String> createWhereClause(Column column) {
        return Arrays.stream(this.getProbeType().getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(WhereClause.class))
            .filter(field -> field.getDeclaredAnnotation(WhereClause.class).column().equals(column))
            .filter(field -> {
                /*
                 * if value is null, ignore where clause.
                 */
                if (NullHandler.IGNORE.equals(field.getDeclaredAnnotation(WhereClause.class).nullHandler())) {
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

        /*
         * e.g.
         * ORD_NO IN
         */
        String columnWithOperator = String.format("%s%s", column.name(), comparison.getOperator());

        Object target = value(probe, field);
        if (Comparison.IN.equals(comparison) || Comparison.NOT_IN.equals(comparison)) {
            if (target instanceof Collection) {
                String contents = IntStream.range(0, ((Collection<?>)target).size()).mapToObj(i -> {

                    /*
                     * e.g.
                     * ordNo[0], ordNo[1], ordNo[2] ...
                     */
                    String fieldName = String.format("%s[%s]", field.getName(), i);

                    return this.bindParameterByComparison(comparison, fieldName);
                }).collect(Collectors.joining(","));
                /*
                 * e.g.
                 * ORD_NO IN (#{probe.ordNo[0]}, #{probe.ordNo[1]})
                 */
                return String.format("%s(%s)", columnWithOperator, contents);
            } else {
                /*
                 * e.g.
                 * ORD_NO IN (#{probe.ordNo})
                 */
                return String.format("%S(%s)", columnWithOperator,
                    this.bindParameterByComparison(comparison, field.getName()));
            }
        }

        return columnWithOperator.concat(this.bindParameterByComparison(comparison, field.getName()));
    }

    private String bindParameterByComparison(Comparison comparison, String fieldName) {
        String open =
            Comparison.START_WITH_LIKE.equals(comparison) || Comparison.ANY_LIKE.equals(comparison) ? "'%'||" : "";
        String close =
            Comparison.END_WITH_LIKE.equals(comparison) || Comparison.ANY_LIKE.equals(comparison) ? "||'%'" : "";

        /*
         * e.g.
         * 1. #{probe.ordNo}
         * 2. '%'|| #{probe.ordNo}
         * 3. #{probe.ordNo} ||'%'
         * 4.'%'|| #{probe.ordNo} ||'%'
         */
        return String.format("%s#{probe.%s}%s", open, fieldName, close);
    }

    @Override
    public T getProbe() {
        return this.probe;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }
}
