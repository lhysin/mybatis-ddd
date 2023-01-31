package io.lhysin.mybatis.ddd.dto;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Comparison;
import io.lhysin.mybatis.ddd.spec.WhereClause;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotOptionalOrderCriteria {

    @WhereClause(column = @Column(name = "ORD_NO"), comparison = Comparison.EQUAL)
    private String ordNo;

    @WhereClause(column = @Column(name = "ORD_SEQ"), comparison = Comparison.LESS_THAN_EQUAL)
    private Integer ordSeq;


}
