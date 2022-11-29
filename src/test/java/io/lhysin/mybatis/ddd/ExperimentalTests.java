package io.lhysin.mybatis.ddd;

import io.lhysin.mybatis.ddd.entity.Student;
import io.lhysin.mybatis.ddd.mapper.DummyMapper;
import io.lhysin.mybatis.ddd.mapper.OrderMapper;
import io.lhysin.mybatis.ddd.mapper.StudentMapper;
import io.lhysin.mybatis.ddd.type.Grade;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

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
        Student student = studentMapper.findById(1L)
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
        assertTrue(exception.getMessage().contains("Not Exists."));

    }
}
