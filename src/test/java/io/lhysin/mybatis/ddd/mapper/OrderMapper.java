package io.lhysin.mybatis.ddd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import io.lhysin.mybatis.ddd.dto.CustomerOrderDto;
import io.lhysin.mybatis.ddd.entity.Order;
import io.lhysin.mybatis.ddd.provider.CrudSqlProvider;

/**
 * The interface Order mapper.
 */
@Repository
public interface OrderMapper extends CrudMapper<Order, Order.PK>,
    PagingAndSortingMapper<Order, Order.PK>,
    QueryByCriteriaMapper<Order, Order.PK> {

    @Override
    @InsertProvider(type = CrudSqlProvider.class, method = "create")
    @SelectKey(keyColumn = "ORD_SEQ", keyProperty = "ordSeq", resultType = Integer.class, before = true, statement = "SELECT ADM.ORDER_SEQUENCE.nextval FROM DUAL")
    int create(Order entity);

    List<CustomerOrderDto> findCustomerOrder();

    List<CustomerOrderDto> findCustomerOrder(RowBounds rb);
}
