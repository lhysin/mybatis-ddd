package io.lhysin.mybatis.ddd;

import io.lhysin.mybatis.ddd.domain.PageRequest;
import io.lhysin.mybatis.ddd.domain.Pageable;
import io.lhysin.mybatis.ddd.domain.Sort;
import io.lhysin.mybatis.ddd.entity.Cart;
import io.lhysin.mybatis.ddd.entity.Customer;
import io.lhysin.mybatis.ddd.entity.Order;
import io.lhysin.mybatis.ddd.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@SpringBootApplication
class MybatisDomainDrivenApplicationTests {

    @Autowired
    private CustomerXmlMapper customerXmlMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private DummyMapper dummyMapper;

    @Test
    void dummy() {

        Exception exception = assertThrows(Exception.class, () -> dummyMapper.findById(""));
        log.debug(exception.getMessage());
        assertTrue(exception.getMessage().contains("Not Exists."));

    }

    @Test
    void sequence() {
        Cart cart = Cart.builder().custNo("20220101").build();
        cartMapper.create(cart);

        assertNotNull(cart.getCartSeq());

        List<Order> orders = IntStream.range(0, 100)
                .mapToObj(i -> Order.builder()
                        .custNo("20220109")
                        .ordNo("order_".concat(i + ""))
                        .build())
                .collect(Collectors.toList());

        orders.forEach(it -> orderMapper.create(it));

        assertNotNull(orders.get(10).getOrdSeq());
    }

    @Test
    void createAll() {

        long count = customerMapper.count();
        List<Customer> customers = IntStream.range(0, 1000)
                .mapToObj(i -> Customer.builder()
                        .custNo("2022_BULK_".concat(i + ""))
                        .firstName("FIRST_N_".concat(i + ""))
                        .lastName("LAST_N_".concat(i + ""))
                        .age(i)
                        .build())
                .collect(Collectors.toList());

        customerMapper.createAll(customers);

        assertEquals(customerMapper.count() - count, customers.size());

    }

    @Test
    void countCustomer() {
        long count = customerMapper.count();
        assertTrue(count > 0);
    }

    @Test
    void dynamicUpdate() {
        String custNo1 = "20220101";
        Customer customer1 = Customer.builder()
                .custNo(custNo1)
                .firstName("CHANGE_FIRST_NAME")
                .lastName("CHANGE_LAST_NAME")
                .age(null)
                .build();
        customerMapper.update(customer1);
        Customer updatedCustomer1 = customerMapper.findById(custNo1).orElseThrow();

        assertNull(updatedCustomer1.getAge());

        String custNo2 = "20220102";
        Customer customer2 = Customer.builder()
                .custNo(custNo2)
                .firstName("ONLY_CHANGE_FIRST_NAME")
                .build();
        customerMapper.dynamicUpdate(customer2);

        Customer dynamicUpdatedCustomer2 = customerMapper.findById(custNo2).orElseThrow();
        assertNotNull(dynamicUpdatedCustomer2.getAge());

    }

    @Test
    void pagingAndSorting() {
        Sort sort = Sort.by("AGE")
                .and(Sort.Direction.DESC, "FIRST_NAME");
        Pageable req = PageRequest.of(2, 3, sort);
        List<Customer> customers = customerMapper.findAll(req);

        // check safty order by
        Sort fakeSort = Sort.by(Sort.Direction.ASC, "asd")
                .and("OOO")
                .and("exists")
                .and("INSERT INTO");
        Pageable fakePageable = PageRequest.of(0, 3, fakeSort);
        List<Customer> fakeCustomers = customerMapper.findAll(fakePageable);
    }

    @Test
    void xmlMapperAndCrudMapper() {
        Customer customer1 = customerXmlMapper.findById("20220101").orElseThrow();
        Customer customer2 = customerMapper.findById("20220101").orElseThrow();
        assertEquals(customer1, customer2);
    }

    @Test
    void createAndFindCustomer() {
        Customer customer = Customer.builder()
                .custNo("2022test")
                .age(13)
                .firstName("test1")
                .lastName("lastName2")
                .build();
        customerMapper.create(customer);

        Customer createdCustomer = customerMapper.findById(customer.getCustNo())
                .orElseThrow();
        assertEquals(customer, createdCustomer);
    }

    @Test
    void findAndUpdateCustomer() {
        Customer customer = customerMapper.findById("20220101")
                .orElseThrow();

        customer.plusAge(20);
        customerMapper.update(customer);

        Customer updatedCustomer = customerMapper.findById(customer.getCustNo())
                .orElseThrow();
        assertEquals(updatedCustomer.getAge(), customer.getAge());
    }

    @Test
    void findAndDeleteCustomer() {
        Customer customer = customerMapper.findById("20220108")
                .orElseThrow();

        customerMapper.delete(customer);
        Customer nullableCustomer = customerMapper.findById(customer.getCustNo())
                .orElse(null);
        assertNull(nullableCustomer);
    }

    @Test
    void findAndDeleteOrder() {
        Order.PK pk = Order.PK.builder()
                .custNo("20220109")
                .ordNo("order01")
                .ordSeq(1)
                .build();
        Order order = orderMapper.findById(pk)
                .orElseThrow();
        assertNotNull(order);

        int deleteCnt = orderMapper.deleteById(pk);
        assertTrue(deleteCnt > 0);

        Order nullableOrder = orderMapper.findById(pk)
                .orElse(null);
        assertNull(nullableOrder);
    }

    @Test
    void findAllByIdsAndDeleteByIds() {
        List<String> customerIds = List.of("20220101", "20220102", "20220103");
        List<Customer> customers = customerMapper.findAllById(customerIds);
        assertFalse(customers.isEmpty());

        List<Order.PK> orderIds = List.of(
                Order.PK.builder()
                        .custNo("20220111")
                        .ordNo("order10")
                        .ordSeq(3)
                        .build(),
                Order.PK.builder()
                        .custNo("20220111")
                        .ordNo("order11")
                        .ordSeq(4)
                        .build(),
                Order.PK.builder()
                        .custNo("20220111")
                        .ordNo("order12")
                        .ordSeq(5)
                        .build()
        );

        List<Order> orders = orderMapper.findAllById(orderIds);
        assertFalse(orders.isEmpty());

        int deleteCnt = orderMapper.deleteAllById(orderIds);
        assertTrue(deleteCnt > 0);
    }

}
