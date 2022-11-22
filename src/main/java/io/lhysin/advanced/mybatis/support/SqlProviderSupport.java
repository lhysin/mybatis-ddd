package io.lhysin.advanced.mybatis.support;

import io.lhysin.advanced.mybatis.domain.Pageable;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SqlProviderSupport<T, ID extends Serializable> extends ProviderContextSupport<T, ID> {

    protected String[] selectColumns(ProviderContext ctx) {
        return this.columns(ctx)
                .map(this::columnNameAndAliasField)
                .toArray(String[]::new);
    }

    protected String[] insertIntoColumns(ProviderContext ctx) {
        return this.columns(ctx)
                .map(this::columnName)
                .toArray(String[]::new);
    }

    protected String[] updateColumns(ProviderContext ctx) {
        return this.withoutIdColumns(ctx)
                .map(this::columnNameAndBindParameter)
                .toArray(String[]::new);
    }

    protected String[] intoValues(ProviderContext ctx) {
        return this.columns(ctx)
                .map(field -> bindParameter(field.getName()))
                .toArray(String[]::new);
    }

    protected String[] wheresById(ProviderContext ctx) {
        return this.onlyIdColumns(ctx)
                .map(this::columnNameAndBindParameter)
                .toArray(String[]::new);
    }

    protected String whereByIds(ProviderContext ctx) {
        // FIRST_NAME or FIRST_NAME, LAST_NAME
        String joiningColumns = this.onlyIdColumns(ctx)
                .map(this::columnName)
                .collect(Collectors.joining(","));

        // #{id} or (#{id.firstName}, #{id.lastName})
        String joiningFields = this.isCompositeKey(ctx) ?
                "(".concat(this.onlyIdColumns(ctx)
                        .map(field -> "#{id.".concat(field.getName()).concat("}"))
                        .collect(Collectors.joining(","))
                ).concat(")")
                : "#{id}";

        return "(".concat(joiningColumns).concat(")")
                .concat("<foreach item='id' collection='ids' open=' IN (' separator=',' close=')'>" )
                .concat("\n").concat(joiningFields)
                .concat("\n</foreach>");
    }

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
