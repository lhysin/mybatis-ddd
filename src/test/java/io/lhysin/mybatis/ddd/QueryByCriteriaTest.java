package io.lhysin.mybatis.ddd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.lhysin.mybatis.ddd.domain.PageRequest;
import io.lhysin.mybatis.ddd.domain.Sort;
import io.lhysin.mybatis.ddd.dto.OrderCriteria;
import io.lhysin.mybatis.ddd.dto.OrderInClauseCriteria;
import io.lhysin.mybatis.ddd.dto.OrderLikeCriteria;
import io.lhysin.mybatis.ddd.entity.Order;
import io.lhysin.mybatis.ddd.mapper.OrderMapper;
import io.lhysin.mybatis.ddd.spec.Criteria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class QueryByCriteriaTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void test_QueryByCriteria() {

        Criteria<OrderCriteria> orderCriteria = Criteria.of(
            OrderCriteria.builder()
                .ordNo("ordNo")
                .ordSeq(1)
                .name(null)
                .build()
        );
        orderMapper.findBy(orderCriteria);

        Exception exception = assertThrows(Exception.class, () ->
            orderMapper.findBy(
                Criteria.of(
                    OrderInClauseCriteria.builder()
                        .build()
                )
            ));

        log.error(exception.getMessage());
        assertTrue(exception.getMessage().contains("Not Allow"));
    }

    @Test
    void test_QueryByCriteria_LIKE() {

        orderMapper.findBy(
            OrderLikeCriteria.builder()
                .ordNo("1")
                .startWithName("start")
                .build()
                .getCriteria()
        );

        orderMapper.findBy(
            OrderLikeCriteria.builder()
                .ordNo("1")
                .endWithName("end")
                .build()
                .getCriteria()
        );

        orderMapper.findBy(
            OrderLikeCriteria.builder()
                .ordNo("1")
                .anyName("any")
                .build()
                .getCriteria()
        );
    }

    @Test
    void test_QueryByCriteria_IN() {

        List<String> nameList = new ArrayList<>();
        nameList.add("name1");
        nameList.add("name2");

        orderMapper.findBy(
            OrderInClauseCriteria.builder()
                .ordNo("1")
                .build()
                .getCriteria()
        );

        orderMapper.findBy(
            OrderInClauseCriteria.builder()
                .inOrdNos(nameList)
                .notInOrdNos(nameList)
                .build()
                .getCriteria()
        );
    }

    @Test
    void test_QueryByCriteria_ORDER_BY() {
        orderMapper.findBy(
            Criteria.of(
                OrderCriteria.builder()
                    .name("x")
                    .build()
                , Sort.by(Sort.Direction.DESC, "ordNo")
            )
        );
    }

    @Test
    void test_QueryByCriteria_count() {
        long count = orderMapper.countBy(
            Criteria.of(
                OrderCriteria.builder()
                    .name("x")
                    .build()
            )
        );

        log.debug(String.format("test_QueryByCriteria_count : %s", count));
        assertTrue(count > 0);
    }

    @Test
    void test_QueryByCriteria_findBy_with_Pageable() {
        List<Order> orderList = orderMapper.findBy(
            Criteria.of(
                OrderCriteria.builder()
                    .name("x")
                    .build(),
                PageRequest.of(1, 10)
            )
        );

        log.debug(String.format("test_QueryByCriteria_findAllBy : %s", orderList));
        assertNotNull(orderList);
    }
}
