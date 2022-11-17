package io.lhysin.myjpa.repository;

import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.stream.Collectors;

class CrudProvider<T> {
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