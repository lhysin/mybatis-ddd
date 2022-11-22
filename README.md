# advanced-mybatis

## Dynamic SQL Generator from Java Persistence

### Mapperless pattern

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
public interface CustomerRepository extends CrudRepository<Customer, String> {
}

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer findById(String custNo) {
        return customerRepository.findById(custNo).orElseThrow();
    }
}
```

### reference by
```text
org.springframework.data.repository.CrudRepository
org.apache.ibatis.annotations.SelectProvider
org.apache.ibatis.annotations.InsertProvider
org.apache.ibatis.annotations.UpdateProvider
```
https://mybatis.org/mybatis-3/ko/statement-builders.html

https://mybatis.org/mybatis-3/apidocs/org/apache/ibatis/builder/annotation/ProviderContext.html

https://stackoverflow.com/questions/8451042/can-i-pass-a-list-as-a-parameter-to-a-mybatis-mapper
