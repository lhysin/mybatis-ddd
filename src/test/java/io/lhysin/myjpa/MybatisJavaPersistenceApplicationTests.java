package io.lhysin.myjpa;

import io.lhysin.myjpa.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MybatisJavaPersistenceApplicationTests {

    @Autowired
    private CustomerMapper customerRepository;

    @Test
    void contextLoads() {



    }

}
