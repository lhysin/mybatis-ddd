package io.lhysin.mybatis.ddd.config;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import io.lhysin.mybatis.ddd.handler.CodeTypeHandler;
import io.lhysin.mybatis.ddd.type.Grade;
import lombok.RequiredArgsConstructor;

/**
 * The type Appendix config.
 */
@Component
@RequiredArgsConstructor
public class AppendixConfig {

    private final SqlSessionFactory sqlSessionFactory;

    /**
     * Dynamic Register type handler.
     */
    @PostConstruct
    public void registerTypeHandler() {
        TypeHandlerRegistry typeHandlerRegistry = sqlSessionFactory.getConfiguration()
            .getTypeHandlerRegistry();

        typeHandlerRegistry.register(new CodeTypeHandler<>(Grade.class));

    }

}
