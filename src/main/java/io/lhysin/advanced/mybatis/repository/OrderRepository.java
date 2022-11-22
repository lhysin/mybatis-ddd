package io.lhysin.advanced.mybatis.repository;

import io.lhysin.advanced.mybatis.config.CrudRepository;
import io.lhysin.advanced.mybatis.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Order.PK> {
}
