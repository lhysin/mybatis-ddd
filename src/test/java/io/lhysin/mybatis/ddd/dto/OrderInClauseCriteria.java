package io.lhysin.mybatis.ddd.dto;

import java.util.List;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Comparison;
import io.lhysin.mybatis.ddd.spec.Criteria;
import io.lhysin.mybatis.ddd.spec.WhereClause;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderInClauseCriteria {

    @WhereClause(column = @Column(name = "ORD_NO"), comparison = Comparison.IN, optional = true)
    private String ordNo;

    @WhereClause(column = @Column(name = "ORD_NO"), comparison = Comparison.IN, optional = true)
    private List<String> inOrdNos;

    @WhereClause(column = @Column(name = "ORD_NO"), comparison = Comparison.NOT_IN, optional = true)
    private List<String> notInOrdNos;

    public Criteria<OrderInClauseCriteria> getCriteria() {
        return Criteria.of(this);
    }

}
