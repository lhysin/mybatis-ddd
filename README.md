# mybatis-ddd

> Mybatis Domain Driven Design   
> 
> Dynamic Mybatis SQL Builder Like Java Persistence
> 
> No Xml Mapper

### 1. How to Use
```java
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Table(name = "CUSTOMER", schema = "ADM")
public class Customer {
    @Id
    @Column(name = "CUST_NO")
    private String custNo;

    @Column(name = "FIRST_NAME")
    private String firstName;
}

@Repository
public interface CustomerMapper extends CrudMapper<Customer, String> {
}

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerMapper customerMapper;

    public Customer findById(String custNo) {
        return customerMapper.findById(custNo).orElseThrow();
    }
}
```
### 2. Like @GeneratedValue with @SelectKey
```java
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMapper extends CrudMapper<Cart, Cart.PK> {

    @Override
    @InsertProvider(type = CrudSqlProvider.class, method = "create")
    @SelectKey(keyColumn="CART_SEQ", keyProperty="cartSeq", resultType=Integer.class, before=true, statement="SELECT ADM.CART_SEQUENCE.nextval FROM DUAL")
    int create(Cart entity);
}
```

### reference by
```text
org.springframework.data.repository.CrudRepository
org.apache.ibatis.annotations.SelectProvider
org.apache.ibatis.annotations.InsertProvider
org.apache.ibatis.annotations.UpdateProvider
```
>https://mybatis.org/mybatis-3/ko/statement-builders.html  
https://mybatis.org/mybatis-3/apidocs/org/apache/ibatis/builder/annotation/ProviderContext.html
https://stackoverflow.com/questions/8451042/can-i-pass-a-list-as-a-parameter-to-a-mybatis-mapper  
