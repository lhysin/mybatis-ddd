package io.lhysin.myjpa.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "CUSTOMER", schema = "ADM")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    @Column(name = "CUST_NO")
    private String custNo;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AGE")
    private Integer age;
}
