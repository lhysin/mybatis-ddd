package io.lhysin.myjpa.repository;

import io.lhysin.myjpa.config.CrudRepository;
import io.lhysin.myjpa.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Order.PK> {
}
