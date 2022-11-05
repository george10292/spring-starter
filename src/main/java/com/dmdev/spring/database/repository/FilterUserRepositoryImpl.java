package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.querydsl.QPredicates;
import com.dmdev.spring.dto.UserFilter;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static com.dmdev.spring.database.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilter userFilter) {
        var predicate = QPredicates.builder()
                .add( userFilter.firstname(), user.firstname::containsIgnoreCase)
                .add( userFilter.lastname(), user.lastname::containsIgnoreCase)
                .add( userFilter.birthDate(), user.birthDate::before)
        .build();
        return new JPAQuery<User>(entityManager)
                .select( user )
                .from(user)
                .where(predicate)
                .fetch();
    }
}
