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

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Id;
import io.lhysin.mybatis.ddd.spec.Table;

/**
 * ProviderContextSupport
 * @param <T>  Table Entity
 * @param <ID>  Table PK
 */
public abstract class ProviderContextSupport<T, ID extends Serializable> {

    /**
     * Domain type class.
     *
     * @param ctx {@link ProviderContext}
     * @return Table Entity tpye
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
     * @return table name
     */
    protected String tableName(ProviderContext ctx) {
        Class<?> domainType = this.domainType(ctx);
        Table tableEnum = domainType.getAnnotation(Table.class);
        return Optional.ofNullable(tableEnum)
            .map(it -> {
                return Optional.ofNullable(it.schema())
                    .map(schema -> schema.concat(".").concat(it.name()))
                    .orElse(it.name());
            })
            .orElseThrow(() -> new IllegalArgumentException(Table.class.getName().concat(" Not Exists."))
            );
    }

    /**
     * Columns stream.
     *
     * @param ctx {@link ProviderContext}
     * @return column stream
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
     * @return only id columns stream
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
     * @return without id column stream
     */
    protected Stream<Field> withoutIdColumns(ProviderContext ctx) {
        return this.columns(ctx)
            .filter(field -> !field.isAnnotationPresent(Id.class));
    }

    /**
     * composite
     * @param ctx {@link ProviderContext}
     * @return boolean boolean
     */
    protected boolean isCompositeKey(ProviderContext ctx) {
        long idColumnCount = this.onlyIdColumns(ctx).count();
        return idColumnCount > 1;
    }

    /**
     * Column name string.
     *
     * @param field the field
     * @return the string
     */
    protected String columnName(Field field) {
        return field.getAnnotation(Column.class).name();
    }

    /**
     * COLUMN AS fieldName
     * @param field the field
     * @return sql string
     */
    protected String columnNameAndAliasField(Field field) {
        return columnName(field).concat(" AS ").concat(field.getName());
    }

    /**
     * column = #{fieldName}
     * @param field the field
     * @return bind sql
     */
    protected String columnNameAndBindParameter(Field field) {
        return columnName(field).concat(" = ").concat(bindParameter(field.getName()));
    }

    /**
     * Column name and bind parameter with key string.
     *
     * @param field the field
     * @param column the column
     * @return the string
     */
    protected String columnNameAndBindParameterWithKey(Field field, String column) {
        return columnName(field).concat(" = ").concat(bindParameterWithKey(field.getName(), column));
    }

    /**
     * #{column}
     * @param column column
     * @return bind sql
     */
    protected String bindParameter(String column) {
        return "#{".concat(column).concat("}");
    }

    /**
     * #{key.fieldName}
     * @param column column
     * @param key key
     * @return bind sql
     */
    protected String bindParameterWithKey(String column, String key) {
        return "#{".concat(key).concat(".").concat(column).concat("}");
    }

    /**
     * Insertable boolean.
     *
     * @param field the field
     * @return insertable boolean
     */
    protected boolean insertable(Field field) {
        return field.getAnnotation(Column.class).insertable();
    }

    /**
     * Updatable boolean.
     *
     * @param field the field
     * @return updatable boolean
     */
    protected boolean updatable(Field field) {
        return field.getAnnotation(Column.class).updatable();
    }
}
