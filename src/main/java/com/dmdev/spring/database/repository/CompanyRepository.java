package com.dmdev.spring.database.repository;
import com.dmdev.spring.database.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

//    @Query(name =  "Company.findByName")
    @Query("select c from Company c " +
            "join fetch c.locales cl " +
            "where c.name = :name")
    Optional<Company> findByName(String name);
    List<Company> findAllByNameContainingIgnoreCase(String fragment);
}
