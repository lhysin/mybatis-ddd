package io.lhysin.ddd.mybatis.mapper;

import io.lhysin.ddd.mybatis.entity.Cart;
import io.lhysin.ddd.mybatis.entity.Customer;
import io.lhysin.ddd.mybatis.provider.CrudSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMapper extends CrudMapper<Cart, Cart.PK> {

    @Override
    @InsertProvider(type = CrudSqlProvider.class, method = "create")
    @SelectKey(keyColumn="CART_SEQ", keyProperty="cartSeq", resultType=Integer.class, before=true, statement="SELECT ADM.CART_SEQUENCE.nextval FROM DUAL")
    int create(Cart entity);
}
