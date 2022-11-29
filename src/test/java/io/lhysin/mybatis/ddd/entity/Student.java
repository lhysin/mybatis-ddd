package io.lhysin.mybatis.ddd.entity;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Id;
import io.lhysin.mybatis.ddd.spec.Table;
import io.lhysin.mybatis.ddd.type.Grade;
import lombok.*;

/**
 * The type Student.
 */
@Getter
@Table(name = "STUDENT", schema = "ADM")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @Column(name = "STD_SEQ")
    private Long stdSeq;

    @Column(name = "GRADE")
    private Grade grade;

}
