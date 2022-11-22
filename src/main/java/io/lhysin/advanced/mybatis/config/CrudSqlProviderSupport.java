package io.lhysin.advanced.mybatis.config;

import org.apache.ibatis.builder.annotation.ProviderContext;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CrudSqlProviderSupport<T, ID> {

    protected Class<?> domainType(ProviderContext ctx) {
/*
        Class<?> mapperType = ctx.getMapperType();
        if(!CrudRepository.class.isInstance(mapperType.)) {
            throw new IllegalArgumentException("Not Found " + CrudRepository.class.getName());
        }
*/

        return Arrays.stream(ctx.getMapperType().getGenericInterfaces())
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .findFirst()
                .map(type -> type.getActualTypeArguments()[0])
                .filter(Class.class::isInstance).map(Class.class::cast)
                .orElseThrow();
    }

    protected String tableName(ProviderContext ctx) {
        Class<?> domainType = this.domainType(ctx);
        Table tableEnum = domainType.getAnnotation(Table.class);
        return Optional.ofNullable(tableEnum)
                .map(tEnum -> {
                    return Optional.ofNullable(tEnum.schema())
                            .map(schema -> schema + "." + tEnum.name())
                            .orElse(tEnum.name());
                })
                .orElseThrow(() -> new IllegalArgumentException("Not Found " + Table.class.getName()));
    }

    protected Stream<Field> fields(ProviderContext ctx) {
        return Arrays.stream(this.domainType(ctx).getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class));
    }

    protected Stream<Field> onlyIdFields(ProviderContext ctx) {
        return this.fields(ctx)
                .filter(field -> field.isAnnotationPresent(Id.class));
    }

    protected Stream<Field> withoutIdFields(ProviderContext ctx) {
        return this.fields(ctx)
                .filter(field -> !field.isAnnotationPresent(Id.class));
    }

    protected String bindColumn(Field field) {
        return field.getAnnotation(Column.class).name();
    }
    protected String bindColumnAndAliasField(Field field) {
        return bindColumn(field) + " AS " + field.getName();
    }

    protected String bindColumnAndParameter(Field field) {
        return bindColumn(field) + " = " + bindParameter(field.getName());
    }

    protected String bindParameter(String column) {
        return "#{" + column + "}";
    }

}
