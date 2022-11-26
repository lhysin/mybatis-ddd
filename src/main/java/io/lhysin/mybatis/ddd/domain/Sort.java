package io.lhysin.mybatis.ddd.domain;

import java.util.ArrayList;
import java.util.List;

public class Sort {

    private final List<Order> orders;

    private Sort(Order order) {
        this.orders = new ArrayList<>();
        this.orders.add(order);
    }

    private Sort(List<Order> orders) {
        this.orders = orders;
    }

    public static Sort by(Direction direction, String property) {
        return new Sort(new Order(direction, property));
    }

    public static Sort by(String property) {
        return Sort.by(Direction.ASC, property);
    }

    public Sort and(Direction direction, String property) {
        this.getOrders().add(new Order(direction, property));
        return new Sort(this.getOrders());
    }

    public Sort and(String property) {
        this.getOrders().add(new Order(Direction.ASC, property));
        return new Sort(this.getOrders());
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public enum Direction {
        ASC, DESC
    }

    public static class Order {
        private final Direction direction;
        private final String property;

        public Order(Direction direction, String property) {
            this.direction = direction;
            this.property = property;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public String getProperty() {
            return this.property;
        }
    }

}