# mybatis-ddd

> Mybatis Domain Driven Design
>
> Dynamic Mybatis SQL Builder Like Java Persistence
>
> No Xml Mapper

### 0. Annotation (Not Support javax.persistence)

```java
import io.lhysin.mybatis.ddd.spec.Id;
import io.lhysin.mybatis.ddd.spec.Table;
import io.lhysin.mybatis.ddd.spec.Column;
```

### 1. findById

---

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

```sql
-- Preparing:
SELECT CUST_NO    AS custNo,
       FIRST_NAME AS firstName,
       LAST_NAME  AS lastName,
       AGE        AS age,
       CREATED_AT AS createdAt,
       UPDATED_AT AS updatedAt
FROM ADM.CUSTOMER
WHERE (CUST_NO = ?)
-- Parameters: 20220101(String)
```

### 2. update and dynamicUpdate

---

```java
void dynamicUpdateTest(){
    customerMapper.update(Customer.builder()
        .custNo("20220101")
        .age(null)
        .updatedAt(LocalDateTime.now())
        .build());

    customerMapper.dynamicUpdate(Customer.builder()
        .custNo("20220102")
        .age(null)
        .updatedAt(LocalDateTime.now())
        .build());
}

```

```sql
-- update
-- Preparing:
UPDATE ADM.CUSTOMER
SET FIRST_NAME = ?,
    LAST_NAME  = ?,
    AGE        = ?,
    UPDATED_AT = ?
WHERE (CUST_NO = ?)
-- Parameters: null, null, null, 2022-12-01T21:18:14.638(LocalDateTime), 20220101(String)

-- dynamicUpdate
-- Preparing:
UPDATE ADM.CUSTOMER
SET UPDATED_AT = ?
WHERE (CUST_NO = ?)
-- Parameters: 2022-12-01T21:18:14.702(LocalDateTime), 20220102(String)
```

### 3. with @SelectKey

---

```java
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
public interface CartMapper extends CrudMapper<Cart, Cart.PK> {

    @Override
    @InsertProvider(type = CrudSqlProvider.class, method = "create")
    @SelectKey(keyColumn = "CART_SEQ", keyProperty = "cartSeq", resultType = Integer.class, before = true,
        statement = "SELECT ADM.CART_SEQUENCE.nextval FROM DUAL")
    int create(Cart entity);
}
```

```sql
-- Preparing:
SELECT NVL(MAX(CART_SEQ), 0)
FROM ADM.CART
WHERE CUST_NO = ?
-- Parameters: 20220101(String)

-- Preparing:
INSERT INTO ADM.CART (CUST_NO, CART_SEQ, CREATED_AT, UPDATED_AT)
VALUES (?, ?, ?, ?)
-- Parameters: 20220101(String), 0(Integer), null, null
```

### 4. Dynamic Enum Type Handler

---

```text
SELECT * FROM ADM.STUDENT
+---------+-------+
| STD_SEQ | GRAGE |
+---------+-------+
| 1       | 3     |
+---------+-------+
| 2       | 6     |
+---------+-------+
| 3       | 1     |
+---------+-------+
```

```java
public class Student {
    @Column(name = "GRADE")
    private Grade grade;
}

public enum Grade implements Code {
    ONE("1"), TWO("2"), //...
    SIX("6")
}

public void registerTypeHandler() {
    sqlSessionFactory.getConfiguration()
        .getTypeHandlerRegistry()
        .register(new CodeTypeHandler<>(Grade.class));
}

Grade grade = studentMapper.findById(2L)
    .orElseThrow(NoSuchElementException::new)
    .getGrade();
```

```sql
-- Preparing:
SELECT STD_SEQ AS stdSeq, GRADE AS grade
FROM ADM.STUDENT
WHERE (STD_SEQ = ?)
-- Parameters: 2(Long)

-- Preparing:
INSERT INTO ADM.STUDENT (STD_SEQ, GRADE)
VALUES (?, ?)
-- Parameters: 3(Long), 6(String)

```

### 5. Query by Criteria

---

```java
public interface QueryByCriteriaMapper<T, ID extends Serializable>
    extends MapperProvider<T, ID>

@Getter
@Builder
public class OrderCriteria {
    @WhereClause(column = @Column(name = "ORD_NO"), comparison = Comparison.EQUAL)
    private String ordNo;

    @WhereClause(column = @Column(name = "ORD_SEQ"), comparison = Comparison.LESS_THAN_EQUAL, ignoreNullValue = false)
    private Integer ordSeq;

    @WhereClause(column = @Column(name = "NAME"), comparison = Comparison.NOT_EQUAL)
    private String name;
}

void test_QueryByCriteria() {
    Criteria<OrderCriteria> orderCriteria = Criteria.of(
        OrderCriteria.builder()
            .ordSeq(1)
            .name(null)
            .build()
    );
    orderMapper.findBy(orderCriteria);
}
```

```sql
--Preparing:
SELECT CUST_NO AS custNo, ORD_NO AS ordNo, ORD_SEQ AS ordSeq, ORD_DTM AS ordDtm,
       NAME AS name, ITEM_CD AS itemCd, CREATED_AT AS createdAt, UPDATED_AT AS updatedAt
FROM ADM.TORDER
WHERE WHERE (ORD_NO = ? AND ORD_SEQ <= ?)
-- Parameters: ordNo(String), 1(Integer)
```

```java
@Getter
@Builder
public class OrderLikeCriteria {

    @WhereClause(column = @Column(name = "ORD_NO"), comparison = Comparison.EQUAL)
    private String ordNo;

    @WhereClause(column = @Column(name = "NAME"), comparison = Comparison.START_WITH_LIKE)
    private String startWithName;// != NULL

    @WhereClause(column = @Column(name = "NAME"), comparison = Comparison.END_WITH_LIKE)
    private String endWithName;

    @WhereClause(column = @Column(name = "NAME"), comparison = Comparison.ANY_LIKE)
    private String anyName;

    public Criteria<OrderLikeCriteria> getCriteria() {
        return Criteria.of(this);
    }
}


void test_QueryByCriteria_LIKE() {
    orderMapper.findBy(
        OrderDynamicCriteria.builder()
            .ordNo("1")
            .startWithName("or")
            .build()
            .getCriteria()
    );

    orderMapper.findBy(
        OrderDynamicCriteria.builder()
            .ordNo("1")
            .endWithName("1")
            .build()
            .getCriteria()
    );

    orderMapper.findBy(
        OrderDynamicCriteria.builder()
            .ordNo("1")
            .anyName("")
            .build()
            .getCriteria()
    );
}
```
```sql
SELECT *
FROM ADM.TORDER WHERE (ORD_NO = ? AND NAME LIKE ?||'%')
-- Parameters: 1(String), start(String)

SELECT *
FROM ADM.TORDER WHERE (ORD_NO = ? AND NAME LIKE '%'||?)
-- Parameters: 1(String), end(String)

SELECT *
FROM ADM.TORDER WHERE (ORD_NO = ? AND NAME LIKE '%'||?||'%')
-- Parameters: 1(String), any(String)
```


```java
void test_QueryByCriteria_IN() {

    List<String> nameList = new ArrayList<String>();
    nameList.add("name1");
    nameList.add("name2");

    orderMapper.findBy(
        OrderInClauseCriteria.builder()
            .ordNo("1")
            .build()
            .getCriteria()
    );

    orderMapper.findBy(
        OrderInClauseCriteria.builder()
            .inOrdNos(nameList)
            .notInOrdNos(nameList)
            .build()
            .getCriteria()
    );
}
```

```sql
SELECT *
FROM ADM.TORDER WHERE (ORD_NO IN (?))
--Parameters: 1(String)

SELECT *
FROM ADM.TORDER WHERE (ORD_NO IN (?,?) AND ORD_NO NOT IN (?,?))
-- Parameters: name1(String), name2(String), name1(String), name2(String)

```


### reference by

> https://mybatis.org/mybatis-3/ko/statement-builders.html
https://mybatis.org/mybatis-3/apidocs/org/apache/ibatis/builder/annotation/ProviderContext.html
https://stackoverflow.com/questions/8451042/can-i-pass-a-list-as-a-parameter-to-a-mybatis-mapper
