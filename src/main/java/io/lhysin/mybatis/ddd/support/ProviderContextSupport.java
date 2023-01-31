package io.lhysin.mybatis.ddd.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Id;
import io.lhysin.mybatis.ddd.spec.Table;

/**
 * ProviderContextSupport
 *
 * @see ProviderMethodResolver
 * @param <T>  Table Entity
 * @param <ID>  Table PK
 */
public abstract class ProviderContextSupport<T, ID extends Serializable> implements ProviderMethodResolver {

    /**
     * Domain type class.
     *
     * @param ctx {@link ProviderContext}
     * @return Table Entity type {@link Class}&lt;{@link T}&gt;
     */
    protected Class<?> domainType(ProviderContext ctx) {
        return Arrays.stream(ctx.getMapperType().getGenericInterfaces())
            .filter(ParameterizedType.class::isInstance)
            .map(ParameterizedType.class::cast)
            .findFirst()
            .map(type -> type.getActualTypeArguments()[0])
            .filter(Class.class::isInstance)
            .map(Class.class::cast)
            .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Table name string.
     *
     * @param ctx {@link ProviderContext}
     * @return table name {@link String}
     */
    protected String tableName(ProviderContext ctx) {
        Class<?> domainType = this.domainType(ctx);
        Table tableEnum = domainType.getAnnotation(Table.class);
        return Optional.ofNullable(tableEnum)
            .map(it -> Optional.ofNullable(it.schema())
                .map(schema -> String.format("%s.%s", schema, it.name()))
                .orElse(it.name()))
            .orElseThrow(() -> new IllegalArgumentException(Table.class.getName().concat(" Not Exists."))
            );
    }

    /**
     * Columns stream.
     *
     * @param ctx {@link ProviderContext}
     * @return column stream {@link Stream}&lt;{@link Field}&gt;
     */
    protected Stream<Field> columns(ProviderContext ctx) {
        return Arrays.stream(this.domainType(ctx).getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Column.class))
            .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
            .filter(it -> !it.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException(Column.class.getName().concat(" Not Exists."))
            ).stream();

    }

    /**
     * Only id columns stream.
     *
     * @param ctx {@link ProviderContext}
     * @return only id columns stream {@link Stream}&lt;{@link Field}&gt;
     */
    protected Stream<Field> onlyIdColumns(ProviderContext ctx) {
        return this.columns(ctx)
            .filter(field -> field.isAnnotationPresent(Id.class))
            .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
            .filter(it -> !it.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException(Id.class.getName().concat(" Not Exists."))
            ).stream();
    }

    /**
     * Without id columns stream.
     *
     * @param ctx {@link ProviderContext}
     * @return without id column stream {@link Stream}&lt;{@link Field}&gt;
     */
    protected Stream<Field> withoutIdColumns(ProviderContext ctx) {
        return this.columns(ctx)
            .filter(field -> !field.isAnnotationPresent(Id.class));
    }

    /**
     * composite
     * @param ctx {@link ProviderContext}
     * @return boolean {@link Boolean}
     */
    protected boolean isCompositeKey(ProviderContext ctx) {
        long idColumnCount = this.onlyIdColumns(ctx).count();
        return idColumnCount > 1;
    }

    /**
     * Column name string.
     *
     * @param field field {@link Field}
     * @return column name {@link String}
     */
    protected String columnName(Field field) {
        return field.getAnnotation(Column.class).name();
    }

    /**
     * COLUMN AS fieldName
     * @param field field {@link Field}
     * @return sql string {@link String}
     */
    protected String columnNameAndAliasField(Field field) {
        return columnName(field).concat(" AS ").concat(field.getName());
    }

    /**
     * column = #{fieldName}
     * @param field field {@link Field}
     * @return bind sql {@link String}
     */
    protected String columnNameAndBindParameter(Field field) {
        return columnName(field).concat(" = ").concat(bindParameter(field.getName()));
    }

    /**
     * Column name and bind parameter with key string.
     *
     * @param field field {@link Field}
     * @param column the column {@link Column}
     * @return column and bind parameter {@link String}
     */
    protected String columnNameAndBindParameterWithKey(Field field, String column) {
        return columnName(field).concat(" = ").concat(bindParameterWithKey(field.getName(), column));
    }

    /**
     * #{column}
     * @param column column {@link Column}
     * @return bind sql {@link String}
     */
    protected String bindParameter(String column) {
        return "#{".concat(column).concat("}");
    }

    /**
     * #{key.fieldName}
     * @param column column {@link Column}
     * @param key key {@link String}
     * @return bind sql {@link String}
     */
    protected String bindParameterWithKey(String column, String key) {
        return "#{".concat(key).concat(".").concat(column).concat("}");
    }

    /**
     * Insertable boolean.
     *
     * @param field field {@link Field}
     * @return insertable {@link Boolean}
     */
    protected boolean insertable(Field field) {
        return field.getAnnotation(Column.class).insertable();
    }

    /**
     * Updatable boolean.
     *
     * @param field field {@link Field}
     * @return updatable {@link Boolean}
     */
    protected boolean updatable(Field field) {
        return field.getAnnotation(Column.class).updatable();
    }
}
