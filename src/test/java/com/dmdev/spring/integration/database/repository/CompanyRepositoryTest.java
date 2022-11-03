package com.dmdev.spring.integration.database.repository;

import com.dmdev.spring.database.entity.Company;
import com.dmdev.spring.database.repository.CompanyRepository;
import com.dmdev.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class CompanyRepositoryTest {

    private static final Integer APPLE_ID = 4;
    private final EntityManager entityManager;
    private final CompanyRepository companyRepository;

    @Test
    void checkFindByQueries() {
        companyRepository.findByName("google");
        companyRepository.findAllByNameContainingIgnoreCase( "a" );
    }

    @Test
    void delete() {
        var maybeCompany = companyRepository.findById( APPLE_ID );
        assertTrue(maybeCompany.isPresent());
        maybeCompany.ifPresent( companyRepository ::delete);
        entityManager.flush();
        assertTrue(companyRepository.findById( APPLE_ID ).isEmpty());
    }

    @Test
    void findById() {
            var company = entityManager.find( Company.class, 1 );
            assertNotNull( company );
            assertThat(company.getLocales()).hasSize( 2 );
    }

    @Test
    void save() {
        var company = Company.builder()
                .name("Apple")
                .locales( Map.of(
                        "ru", "Apple description",
                        "en", "O vy iz anglii"
                ))
                .build();
        entityManager.persist( company );
        assertNotNull( company.getId() );
    }
}