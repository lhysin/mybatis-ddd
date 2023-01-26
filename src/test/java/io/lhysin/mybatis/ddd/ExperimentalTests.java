package io.lhysin.mybatis.ddd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.lhysin.mybatis.ddd.dto.OrderCriteria;
import io.lhysin.mybatis.ddd.dto.OrderInClauseCriteria;
import io.lhysin.mybatis.ddd.dto.OrderLikeCriteria;
import io.lhysin.mybatis.ddd.entity.Student;
import io.lhysin.mybatis.ddd.mapper.DummyMapper;
import io.lhysin.mybatis.ddd.mapper.OrderMapper;
import io.lhysin.mybatis.ddd.mapper.StudentMapper;
import io.lhysin.mybatis.ddd.spec.Criteria;
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


        Exception exception = assertThrows(Exception.class, () -> {
            orderMapper.findBy(
                Criteria.of(
                    OrderInClauseCriteria.builder()
                    .build()
                )
            );
        });

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

        List<String> nameList = new ArrayList<String>();
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
}
