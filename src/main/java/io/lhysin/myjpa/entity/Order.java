package io.lhysin.myjpa.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@Table(name = "TORDER", schema = "ADM")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(Order.PK.class)
public class Order {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PK implements Serializable {
        private String custNo;
        private String ordNo;
    }

    @Id
    @Column(name = "CUST_NO")
    private String custNo;

    @Id
    @Column(name = "ORD_NO")
    private String ordNo;

    @Column(name = "NAME")
    private String name;

}
