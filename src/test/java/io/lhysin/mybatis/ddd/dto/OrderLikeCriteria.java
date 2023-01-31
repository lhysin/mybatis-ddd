package io.lhysin.mybatis.ddd.dto;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Comparison;
import io.lhysin.mybatis.ddd.spec.Criteria;
import io.lhysin.mybatis.ddd.spec.WhereClause;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderLikeCriteria {

    @WhereClause(column = @Column(name = "ORD_NO"), comparison = Comparison.EQUAL, optional = true)
    private String ordNo;

    @WhereClause(column = @Column(name = "NAME"), comparison = Comparison.START_WITH_LIKE, optional = true)
    private String startWithName;

    @WhereClause(column = @Column(name = "NAME"), comparison = Comparison.END_WITH_LIKE, optional = true)
    private String endWithName;

    @WhereClause(column = @Column(name = "NAME"), comparison = Comparison.ANY_LIKE, optional = true)
    private String anyName;

    public Criteria<OrderLikeCriteria> getCriteria() {
        return Criteria.of(this);
    }

}
