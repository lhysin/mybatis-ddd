package io.lhysin.mybatis.ddd.entity;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Id;
import io.lhysin.mybatis.ddd.spec.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Table(name = "TORDER", schema = "ADM")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @Column(name = "CUST_NO")
    @EqualsAndHashCode.Include
    private String custNo;
    @Id
    @Column(name = "ORD_NO")
    @EqualsAndHashCode.Include
    private String ordNo;
    @Id
    @Column(name = "ORD_SEQ")
    @EqualsAndHashCode.Include
    private Integer ordSeq;
    @Column(name = "ORD_DTM")
    private LocalDateTime ordDtm;
    @Column(name = "ITEM_CD")
    private String itemCd;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PK implements Serializable {
        private String custNo;
        private String ordNo;
        private Integer ordSeq;
    }
}
