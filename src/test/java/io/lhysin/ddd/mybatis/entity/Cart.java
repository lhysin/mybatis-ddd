package io.lhysin.ddd.mybatis.entity;

import io.lhysin.ddd.mybatis.spec.Column;
import io.lhysin.ddd.mybatis.spec.Id;
import io.lhysin.ddd.mybatis.spec.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Table(name = "CART", schema = "ADM")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cart {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PK implements Serializable {
        private String custNo;
        private Integer cartSeq;
    }

    @Id
    @Column(name = "CUST_NO")
    @EqualsAndHashCode.Include
    private String custNo;

    @Id
    @Column(name = "CART_SEQ")
    private Integer cartSeq;

}
