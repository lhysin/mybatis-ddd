package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends CrudMapper<Order, Order.PK> {
}
