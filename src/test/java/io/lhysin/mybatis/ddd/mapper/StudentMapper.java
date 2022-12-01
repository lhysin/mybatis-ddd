package io.lhysin.mybatis.ddd.mapper;

import org.springframework.stereotype.Repository;

import io.lhysin.mybatis.ddd.entity.Student;

/**
 * The interface Student mapper.
 */
@Repository
public interface StudentMapper extends CrudMapper<Student, Long> {
}
