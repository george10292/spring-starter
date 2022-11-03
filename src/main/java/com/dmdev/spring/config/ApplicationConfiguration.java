package com.dmdev.spring.config;

import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

//@Import(WebConfiguration.class)
@Configuration

public class ApplicationConfiguration {

    @Bean
    public ConnectionPool pool2 (@Value("${db.username}") String username) {
        return new ConnectionPool(username,20);
    }

    @Bean
    public ConnectionPool pool3 () {
        return new ConnectionPool("test-pool",20);
    }

}
