package io.lhysin.mybatis.ddd.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import io.lhysin.mybatis.ddd.entity.Cart;
import io.lhysin.mybatis.ddd.provider.CrudSqlProvider;

/**
 * The interface Cart mapper.
 */
@Repository
public interface CartMapper extends CrudMapper<Cart, Cart.PK> {

	@Override
	@InsertProvider(type = CrudSqlProvider.class, method = "create")
	@SelectKey(keyColumn = "CART_SEQ", keyProperty = "cartSeq", resultType = Integer.class, before = true,
		statement = "SELECT NVL(MAX(CART_SEQ), 0) FROM ADM.CART WHERE CUST_NO = #{custNo}")
	int create(Cart entity);
}
