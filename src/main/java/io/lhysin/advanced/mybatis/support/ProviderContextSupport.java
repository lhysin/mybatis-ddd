package io.lhysin.advanced.mybatis.support;

import org.apache.ibatis.builder.annotation.ProviderContext;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;


public abstract class ProviderContextSupport<T, ID extends Serializable> {

    protected Class<?> domainType(ProviderContext ctx) {
/* TODO 유효성 검증 추가 필요.
        Class<?> mapperType = ctx.getMapperType();
        if(!CrudRepository.class.isInstance(mapperType.)) {
            throw new IllegalArgumentException("Not Found ".concat(CrudRepository.class.getName()));
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
                .map(it -> {
                    return Optional.ofNullable(it.schema())
                            .map(schema -> schema.concat(".").concat(it.name()))
                            .orElse(it.name());
                })
                .orElseThrow(() -> new IllegalArgumentException("Not Found ".concat(Table.class.getName())));
    }

    protected Stream<Field> columns(ProviderContext ctx) {
        return Arrays.stream(this.domainType(ctx).getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class));
    }

    protected Stream<Field> onlyIdColumns(ProviderContext ctx) {
        return this.columns(ctx)
                .filter(field -> field.isAnnotationPresent(Id.class));
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
}
