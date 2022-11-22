package io.lhysin.advanced.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Sort {

    private List<Order> orders;

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

    @Getter
    @AllArgsConstructor
    public static class Order {
        private Direction direction;
        private String property;
    }

    public enum Direction {
        ASC, DESC
    }

}