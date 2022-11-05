package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.dto.UserFilter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilter userFilter) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteria = criteriaBuilder.createQuery(User.class);
        var user = criteria.from(User.class);
        criteria.select( user ).where(  );

        List<Predicate> predicates = new ArrayList<>();
        if (userFilter.firstname() != null) {
            predicates.add( criteriaBuilder.like( user.get("firstname") , userFilter.firstname()  ));
        }
        if (userFilter.lastname() != null) {
            predicates.add( criteriaBuilder.like( user.get("lastname") , userFilter.lastname()  ));
        }
        if (userFilter.birthDate() != null) {
            predicates.add( criteriaBuilder.lessThan( user.get("birthDate") , userFilter.birthDate()  ));
        }

        criteria.where( predicates.toArray(Predicate[]::new) );
        return entityManager.createQuery( criteria ).getResultList();
    }
}
