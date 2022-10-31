package com.dmdev.spring.config;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.pool.ConnectionPool;
import com.dmdev.spring.database.repository.CrudRepository;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.web.config.WebConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

@Import(WebConfiguration.class)
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.dmdev.spring",
useDefaultFilters = false,
includeFilters = {
        @Filter(type= FilterType.ANNOTATION, value = Component.class),
        @Filter(type= FilterType.ASSIGNABLE_TYPE, value = CrudRepository.class),
        @Filter(type= FilterType.REGEX, pattern = "com\\..+Repository")
})
public class ApplicationConfiguration {

    @Bean
    public ConnectionPool pool2 (@Value("${db.username}") String username) {
        return new ConnectionPool(username,20);
    }

    @Bean
    public ConnectionPool pool3 () {
        return new ConnectionPool("test-pool",20);
    }

    @Bean
    @Profile("prod")
    public UserRepository userRepository2(ConnectionPool pool2) {
        return new UserRepository(pool2);
    }

    @Bean
    public UserRepository userRepository3(ConnectionPool pool2) {
        var connectionPool1 = pool3();
        var connectionPool2 = pool3();
        var connectionPool3 = pool3();
        return new UserRepository(pool3());
    }

}
