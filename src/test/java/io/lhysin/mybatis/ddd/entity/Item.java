package io.lhysin.mybatis.ddd.entity;

import io.lhysin.mybatis.ddd.spec.Column;
import io.lhysin.mybatis.ddd.spec.Id;
import io.lhysin.mybatis.ddd.spec.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Item.
 */
@Getter
@Table(name = "ITEM", schema = "ADM")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @Column(name = "ITEM_SEQ")
    private Long itemSeq;
}
