package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.entity.Cart;
import io.lhysin.ddd.mybatis.entity.Order;
import io.lhysin.ddd.mybatis.provider.CrudSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends CrudMapper<Order, Order.PK> {
    @Override
    @InsertProvider(type = CrudSqlProvider.class, method = "create")
    @SelectKey(keyColumn="ORD_SEQ", keyProperty="ordSeq", resultType=Integer.class, before=true, statement="SELECT ADM.ORDER_SEQUENCE.nextval FROM DUAL")
    int create(Order entity);
}
