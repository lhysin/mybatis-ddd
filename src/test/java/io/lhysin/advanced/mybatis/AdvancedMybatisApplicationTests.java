package io.lhysin.advanced.mybatis;

import io.lhysin.advanced.mybatis.domain.PageRequest;
import io.lhysin.advanced.mybatis.domain.Pageable;
import io.lhysin.advanced.mybatis.domain.Sort;
import io.lhysin.advanced.mybatis.entity.Customer;
import io.lhysin.advanced.mybatis.entity.Order;
import io.lhysin.advanced.mybatis.mapper.CustomerXmlMapper;
import io.lhysin.advanced.mybatis.mapper.CustomerMapper;
import io.lhysin.advanced.mybatis.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Slf4j
class AdvancedMybatisApplicationTests {

    @Autowired
    private CustomerXmlMapper customerXmlMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void countCustomer() {
        long count = customerMapper.count();
        assertTrue(count > 0);
    }

    @Test
    void pagingAndSorting() {
        Sort sort = Sort.by("AGE")
                .and(Sort.Direction.DESC, "FIRST_NAME");
        Pageable req = PageRequest.of(2, 3, sort);
        List<Customer> customers = customerMapper.findAll(req);

        Sort fakeSort = Sort.by(Sort.Direction.ASC, "asd")
                        .and("OOO")
                        .and("exists")
                        .and("INSERT INTO");
        Pageable fakePageable = PageRequest.of(0, 3, fakeSort);
        List<Customer> fakeCustomers =customerMapper.findAll(fakePageable);
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
                        .build(),
                Order.PK.builder()
                        .custNo("20220111")
                        .ordNo("order11")
                        .build(),
                Order.PK.builder()
                        .custNo("20220111")
                        .ordNo("order12")
                        .build()
        );

        List<Order> orders = orderMapper.findAllById(orderIds);
        assertFalse(orders.isEmpty());

        int deleteCnt = orderMapper.deleteAllById(orderIds);
        assertTrue(deleteCnt > 0);
    }

}
