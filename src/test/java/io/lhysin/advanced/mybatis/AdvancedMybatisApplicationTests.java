package io.lhysin.advanced.mybatis;

import io.lhysin.advanced.mybatis.entity.Order;
import io.lhysin.advanced.mybatis.mapper.CustomerMapper;
import io.lhysin.advanced.mybatis.repository.CustomerRepository;
import io.lhysin.advanced.mybatis.entity.Customer;
import io.lhysin.advanced.mybatis.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdvancedMybatisApplicationTests {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void test() {

        long count = customerRepository.count();
        assertTrue(count > 0);

        Customer customer = Customer.builder()
                .custNo("2022test")
                .age(13)
                .firstName("test1")
                .lastName("lastName2")
                .build();
        customerRepository.create(customer);

        Customer createdCustomer = customerRepository.findById(customer.getCustNo())
                .orElseThrow();
        assertEquals(customer, createdCustomer);

        createdCustomer.plusAge(20);
        customerRepository.update(createdCustomer);
        Customer updatedCustomer = customerRepository.findById(customer.getCustNo())
                .orElseThrow();
        assertEquals(updatedCustomer.getAge(), createdCustomer.getAge());

        customerRepository.delete(createdCustomer);
        Customer nullableCustomer = customerRepository.findById(createdCustomer.getCustNo())
                .orElse(null);
        assertNull(nullableCustomer);

        Customer customer1 = customerMapper.findById("20220101").orElseThrow();
        Customer customer2 = customerRepository.findById("20220101").orElseThrow();
        assertEquals(customer1, customer2);

        Order.PK pk = Order.PK.builder()
                .custNo("20220109")
                .ordNo("order01")
                .build();
        Order order = orderRepository.findById(pk)
                .orElseThrow();
        assertNotNull(order);

        int deleteCnt = orderRepository.deleteById(pk);
        assertTrue(deleteCnt > 0);

        Order nullableOrder = orderRepository.findById(pk)
                .orElse(null);
        assertNull(nullableOrder);

        List<String> idss = List.of("20220101", "20220102", "20220103");
        List<Customer> customers = customerRepository.findAllById(idss);
    }

}
