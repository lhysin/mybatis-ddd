package io.lhysin.myjpa.config;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

public class CrudSqlProvider<T, ID> {

    private String dot = ".";
    private String lrOnePadAlias = " AS ";
    private String lrOnePadEqual = " = ";
    private String hash = "#";
    private String startBrace = "{";
    private String endBrace = "}";

    private String comma = ",";

    public String findById(ProviderContext providerContext) throws ClassNotFoundException {

        Class<?> domainClass = this.domainType(providerContext);
        Class<?> idClass = this.idType(providerContext);

        String[] selectColumns = this.selectColumns(domainClass);
        String table = this.table(domainClass);
        String[] wheres = this.wheres(domainClass);

        return new SQL()
                .SELECT(selectColumns)
                .FROM(table)
                .WHERE(wheres)
                .toString();
    }

    public String delete(ProviderContext providerContext) throws ClassNotFoundException {
        Class<?> domainClass = this.domainType(providerContext);

        String table = this.table(domainClass);
        String[] wheres = this.wheres(domainClass);

        return new SQL()
                .DELETE_FROM(table)
                .WHERE(wheres)
                .toString();
    }

    public String save(ProviderContext providerContext) throws ClassNotFoundException {
/*        Class<?> domainClass = this.domainType(providerContext);

        String table = this.table(domainClass);
        String[] updateColumns = this.updateColumns(domainClass);
        String[] wheres = this.wheres(domainClass, id);



        String updateQuery = new SQL()
                .UPDATE(table)
                .SET(updateColumns)
                .WHERE(wheres)
                .toString();

        String insertQuery = new SQL()
                .INSERT_INTO(table)
                .VALUES()
                .WHERE(wheres)
                .toString();*/

        return "";
    }

    /*
     * table.FIRST_NAME AS firstName
     * */
    private String[] selectColumns(Class<?> domainClass) {
        Table tableEnum = domainClass.getAnnotation(Table.class);
        return Arrays.stream(domainClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field ->
                        tableEnum.name().toLowerCase() +
                                dot +
                                field.getAnnotation(Column.class).name().toUpperCase() +
                                lrOnePadAlias +
                                field.getName()
                )
                .toArray(String[]::new);
    }

    /*
     * table.FIRST_NAME = #{table.firstName}"
     * */
    private String[] updateColumns(Class<?> domainClass) {
        String table = this.table(domainClass);
        return Arrays.stream(domainClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> {
                    return table +
                            dot +
                            field.getAnnotation(Column.class).name().toUpperCase() +
                            lrOnePadEqual +
                            hash +
                            startBrace +
                            table +
                            dot +
                            field.getName() +
                            endBrace;
                })
                .toArray(String[]::new);
    }

    /*
     * "ID", "FULL_NAME"
     * */
    private String[] intoColumns(Class<?> domainClass) {
        return Arrays.stream(domainClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> field.getAnnotation(Column.class).name() + lrOnePadAlias + field.getName())
                .toArray(String[]::new);
    }

    /*
     * "#{subPerson.id}", "#{subPerson.fullName}"
     * */
    private String[] valueColumns(Class<?> domainClass, T domain) {
        return Arrays.stream(domainClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> field.getAnnotation(Column.class).name() + lrOnePadAlias + field.getName())
                .toArray(String[]::new);
    }

    /*
    * shema가 존재하면 shema.tableName 없으면 tableName
    * ex) ADM.TABLE table / TABLE table
    * */
    private String table(Class<?> domainClass) {
        Table tableEnum = domainClass.getAnnotation(Table.class);
        return Optional.ofNullable(tableEnum.schema())
                .map(schema -> schema + dot + tableEnum.name().toUpperCase() + " " + tableEnum.name().toLowerCase())
                .orElse(tableEnum.name().toUpperCase() + " " + tableEnum.name().toLowerCase());
    }

    private String[] wheres(Class<?> domainClass) {
        Table tableEnum = domainClass.getAnnotation(Table.class);
        String[] conditions = Arrays.stream(domainClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .map(field ->
                        field.getAnnotation(Column.class).name() +
                        lrOnePadEqual +
                        hash +
                        startBrace +
                                // #{asd.value} => asd는 PK 명이 와야함
                        /*tableEnum.name().toLowerCase() +
                                dot + */
                                field.getName() +
                        endBrace
                ).toArray(String[]::new);
        return conditions;
    }

    private Class<?> domainType(ProviderContext providerContext) throws ClassNotFoundException {
        Type domainType = this.genericType(providerContext).getActualTypeArguments()[0];
        return Class.forName(domainType.getTypeName());
    }

    private Class<?> idType(ProviderContext providerContext) throws ClassNotFoundException {
        // TODO @Idclass 검증이 필요한가?
        Type idType = this.genericType(providerContext).getActualTypeArguments()[1];
        return Class.forName(idType.getTypeName());
    }

    private ParameterizedType genericType(ProviderContext providerContext) {
        Class<?> mapperType = providerContext.getMapperType();

        Class<?>[] a = mapperType.getInterfaces();

        /*
         TODO mapperType => CrudSqlProvider를 활용하는 타입
              CrudRepostory만 허용할 수 있게 유효성 검증 필요?
         */

        return Arrays.stream(mapperType.getGenericInterfaces())
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .findFirst()
                .orElseThrow();
    }

}
