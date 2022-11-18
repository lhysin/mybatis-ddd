package io.lhysin.myjpa;

import io.lhysin.myjpa.entity.Customer;
import io.lhysin.myjpa.entity.Order;
import io.lhysin.myjpa.mapper.CustomerMapper;
import io.lhysin.myjpa.repository.CustomerRepository;
import io.lhysin.myjpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class MybatisJavaPersistenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisJavaPersistenceApplication.class, args);
    }

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/v1/customers")
    public ResponseEntity customers() {
        Customer customer = customerMapper.findById("20220101").orElse(null);
        Customer customer2 = customerRepository.findById("20220101").orElse(null);
        return ResponseEntity.ok().body(customer2);
    }

    @GetMapping("/v1/orders")
    public ResponseEntity orders() {
        Order order = orderRepository.findById(Order.PK.builder().custNo("20220101").ordNo("order01").build()).orElse(null);
        return ResponseEntity.ok().body(order);
    }

}
