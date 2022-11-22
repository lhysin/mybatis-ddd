package io.lhysin.advanced.mybatis.provider;

import io.lhysin.advanced.mybatis.domain.Pageable;
import io.lhysin.advanced.mybatis.support.SqlProviderSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import java.io.Serializable;

@Slf4j
public class PagingAndSortingSqlProvider<T, ID extends Serializable> extends SqlProviderSupport<T, ID> {

    public String findAll(Pageable pageable, ProviderContext ctx) {
        return new SQL()
                .SELECT(this.selectColumns(ctx))
                .FROM(this.tableName(ctx))
                // TODO dynamic CriteriaQuery
                //.WHERE(this.wheresById(ctx))
                .ORDER_BY(this.orders(pageable, ctx))
                .OFFSET_ROWS("#{offset}")
                .FETCH_FIRST_ROWS_ONLY("#{limit}")
                .toString();
    }

}
