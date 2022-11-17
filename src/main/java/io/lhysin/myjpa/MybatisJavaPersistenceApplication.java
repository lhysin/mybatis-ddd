package io.lhysin.myjpa;

import io.lhysin.myjpa.entity.Customer;
import io.lhysin.myjpa.repository.CustomerCRUDRepository;
import io.lhysin.myjpa.repository.TestCRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MybatisJavaPersistenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisJavaPersistenceApplication.class, args);
    }

    @Autowired
    CustomerCRUDRepository customerCRUDRepository;



    @GetMapping("/v1/customers")
    public ResponseEntity customers() {
        Customer customer = customerCRUDRepository.findById("20220101").orElse(null);
        return ResponseEntity.ok().body(customer);
    }

}
