package io.lhysin.mybatis.ddd.mapper;

import io.lhysin.mybatis.ddd.entity.Item;
import io.lhysin.mybatis.ddd.provider.CrudSqlProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

/**
 * The interface Item mapper.
 */
@Repository
public interface ItemMapper extends CrudMapper<Item, Integer> {

    @Override
    @InsertProvider(type = CrudSqlProvider.class, method = "create")
    @SelectKey(keyColumn = "ITEM_SEQ", keyProperty = "itemSeq", resultType = Long.class, before = true, statement = "SELECT ADM.ITEM_SEQUENCE.nextval FROM DUAL")
    int create(Item entity);

}
