package io.lhysin.mybatis.ddd.support;

import io.lhysin.mybatis.ddd.spec.*;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ProviderContextSupport<T, ID extends Serializable> {

    protected Class<?> domainType(ProviderContext ctx) {
        return Arrays.stream(ctx.getMapperType().getGenericInterfaces())
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .findFirst()
                .map(type -> type.getActualTypeArguments()[0])
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .orElseThrow();
    }

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

    protected Stream<Field> columns(ProviderContext ctx) {
        return Arrays.stream(this.domainType(ctx).getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                .filter(it -> !it.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException(Column.class.getName().concat(" Not Exists."))
                ).stream();

    }

    protected Stream<Field> onlyIdColumns(ProviderContext ctx) {
        return this.columns(ctx)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of))
                .filter(it -> !it.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException(Id.class.getName().concat(" Not Exists."))
                ).stream();
    }

    protected Stream<Field> withoutIdColumns(ProviderContext ctx) {
        return this.columns(ctx)
                .filter(field -> !field.isAnnotationPresent(Id.class));
    }

    protected boolean isCompositeKey(ProviderContext ctx) {
        long idColumnCount = this.onlyIdColumns(ctx).count();
        return idColumnCount > 1;
    }

    protected String columnName(Field field) {
        return field.getAnnotation(Column.class).name();
    }

    protected String columnNameAndAliasField(Field field) {
        return columnName(field).concat(" AS ").concat(field.getName());
    }

    protected String columnNameAndBindParameter(Field field) {
        return columnName(field).concat(" = ").concat(bindParameter(field.getName()));
    }

    protected String bindParameter(String column) {
        return "#{".concat(column).concat("}");
    }

    protected String bindParameterWithKey(String column, String key) {
        return "#{".concat(key).concat(".").concat(column).concat("}");
    }

    protected boolean insertable(Field field) {
        return field.getAnnotation(Column.class).insertable();
    }

    protected boolean updatable(Field field) {
        return field.getAnnotation(Column.class).updatable();
    }
}
