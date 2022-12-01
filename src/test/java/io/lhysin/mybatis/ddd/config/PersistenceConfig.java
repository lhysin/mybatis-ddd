package io.lhysin.mybatis.ddd.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * The type Persistence config.
 */
@Configuration
@MapperScan({"io.lhysin"})
public class PersistenceConfig {

    /**
     * Sql session factory sql session factory.
     *
     * @param dataSource the data source
     * @return the sql session factory
     * @throws Exception the exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setMapperLocations(
            (new PathMatchingResourcePatternResolver().getResources("classpath*:**/mapper/*Mapper.xml")));
        factoryBean.setDataSource(dataSource);
        return factoryBean.getObject();
    }
}