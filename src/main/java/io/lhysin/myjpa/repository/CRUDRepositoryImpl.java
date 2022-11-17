package io.lhysin.myjpa.repository;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.convert.Delimiter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class CRUDRepositoryImpl<T, ID> implements CRUDRepository<T, ID> {

    private final Class<T> domainType;
    private final Class<ID> idType;

    private final SqlSessionFactory sqlSessionFactory;

    public CRUDRepositoryImpl(SqlSessionFactory sqlSessionFactory, Class<T> domainType, Class<ID> idType) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.domainType = domainType;
        this.idType = idType;
    }


    @Override
    @SelectProvider(type=CrudProvider.class, method="getPersonByName")
    public Optional<T> findById(ID id) {
    }

    private Method[] find(Class<T> klass) {

        return ReflectionUtils.getDeclaredMethods(klass);
    }

    @Override
    public Iterable<T> findAll() {
        return null;
    }

    @Override
    public void delete(ID id) {

    }

    @Override
    public <S extends T> S save(ID id) {
        return null;
    }

    static class CrudProvider<T> {
        public String findById(Class<T> domainType, String id) {

            Table table = domainType.getAnnotation(Table.class);

            String FROM = table.schema() + "." + table.name();

            String SELECT = Arrays.stream(domainType.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Column.class))
                    .map(field -> field.getAnnotation(Column.class).name() + " AS " + field.getName())
                    .collect(Collectors.joining(","));


            String idColumn = Arrays.stream(domainType.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .map(field -> field.getAnnotation(Column.class).name())
                    .findFirst()
                    .orElseThrow();

           return new SQL() {{
               SELECT(SELECT);
               FROM(FROM);
               WHERE(idColumn + " = " + id.toString());
           }}.toString();
        }
    }
}
