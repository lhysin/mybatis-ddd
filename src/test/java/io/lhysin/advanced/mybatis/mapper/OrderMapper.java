package io.lhysin.advanced.mybatis.mapper;

import io.lhysin.advanced.mybatis.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends CrudMapper<Order, Order.PK> {
}
