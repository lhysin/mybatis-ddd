package io.lhysin.myjpa.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "select", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "insert", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
})
public class MybatisCRUDInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.debug(invocation.toString());
        return null;
    }
}
