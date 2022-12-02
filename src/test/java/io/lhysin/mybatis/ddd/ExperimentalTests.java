package io.lhysin.mybatis.ddd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.lhysin.mybatis.ddd.entity.Order;
import io.lhysin.mybatis.ddd.entity.Student;
import io.lhysin.mybatis.ddd.mapper.DummyMapper;
import io.lhysin.mybatis.ddd.mapper.OrderMapper;
import io.lhysin.mybatis.ddd.mapper.StudentMapper;
import io.lhysin.mybatis.ddd.spec.Example;
import io.lhysin.mybatis.ddd.type.Grade;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Experimental tests.
 */
@Slf4j
@SpringBootTest
class ExperimentalTests {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private DummyMapper dummyMapper;

    /**
     * Join table and row bound test.
     */
    @Test
    void joinTableAndRowBoundTest() {
        int totalSize = orderMapper.findCustomerOrder().size();

        int onePageSize = orderMapper.findCustomerOrder(new RowBounds(0, 2)).size();
        int twoPageSize = orderMapper.findCustomerOrder(new RowBounds(3, 2)).size();
        int threePageSize = orderMapper.findCustomerOrder(new RowBounds(5, 2)).size();

        log.debug("totalSize : {}, onePageSize : {}, twoPageSize : {}, threePageSize : {}",
            totalSize,
            onePageSize,
            twoPageSize,
            threePageSize
        );

    }

    /**
     * Type handler test.
     */
    @Test
    void typeHandlerTest() {
        Student student = studentMapper.findById(2L)
            .orElseThrow(NoSuchElementException::new);

        Grade grade = student.getGrade();
        String gradeCode = grade.getCode();

        assertNotNull(gradeCode);

        studentMapper.create(Student.builder()
            .stdSeq(3L)
            .grade(Grade.SIX)
            .build());
    }

    /**
     * Dummy test.
     */
    @Test
    void dummyTest() {

        Exception exception = assertThrows(Exception.class, () -> dummyMapper.findById(""));
        log.debug(exception.getMessage());
        assertTrue(exception.getMessage().contains("Not Exists"));

    }

    /**
     * Find by example test.
     */
    @Test
    void findByExampleTest() {

        Optional<Student> student = studentMapper.findOne(
            Example.of(Student.builder()
                .grade(Grade.FIVE)
                .build())
        );
        assertTrue(student.isPresent());

        Optional<Order> order = orderMapper.findOne(
            Example.of(Order.builder()
                .name("orderName04")
                .build()
            )
        );
        assertTrue(order.isPresent());

        List<Order> orders = orderMapper.findBy(
            Example.of(Order.builder()
                .custNo("20220109")
                .build()
            )
        );
        assertFalse(orders.isEmpty());

        Optional<Order> exampleOfIncludeNullOrder = orderMapper.findOne(
            Example.withIncludeNullValues(
                Order.builder().build()
            )
        );
        assertFalse(exampleOfIncludeNullOrder.isPresent());

        Exception exception = assertThrows(Exception.class, () -> {
            orderMapper.findOne(
                Example.of(Order.builder()
                    .build()
                )
            );
        });

        log.debug(exception.getMessage());
        assertTrue(exception.getMessage().contains("Not Exists"));

    }
}
