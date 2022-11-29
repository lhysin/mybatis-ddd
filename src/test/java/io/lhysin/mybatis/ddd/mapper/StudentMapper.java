package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.entity.Student;
import org.springframework.stereotype.Repository;

/**
 * The interface Student mapper.
 */
@Repository
public interface StudentMapper extends CrudMapper<Student, Long> {
}
