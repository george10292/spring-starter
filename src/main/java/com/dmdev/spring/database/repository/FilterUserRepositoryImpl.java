package com.dmdev.spring.database.repository;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.querydsl.QPredicates;
import com.dmdev.spring.dto.PersonalInfo;
import com.dmdev.spring.dto.UserFilter;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dmdev.spring.database.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private static final String FIND_BY_COMPANY_AND_ROLE = """
            SELECT 
            firstname,
            lastname,
            birth_date
            FROM users
            WHERE company_id = ?
            AND role = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE = """
            UPDATE users
            SET company_id = ?,
                role = ?
            WHERE id = ?
            """;
    private static final String UPDATE_COMPANY_AND_ROLE_NAMED = """
            UPDATE users
            SET company_id = :company_id,
                role = :role
            WHERE id = :id
            """;

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    @Override
    public List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role) {
        return jdbcTemplate.query( FIND_BY_COMPANY_AND_ROLE, (rs, rowNum) -> new PersonalInfo(
                rs.getString( "firstname" ),
                rs.getString( "lastname" ),
                rs.getDate( "birth_date" ).toLocalDate()
        ), companyId, role.name() );
    }

    @Override
    public void updateCompanyAndRole(List<User> users) {
        var args = users.stream()
                                      .map( user -> new Object[]{user.getCompany().getId(), user.getRole().name(), user.getId()} )
                                      .toList();
        jdbcTemplate.batchUpdate( UPDATE_COMPANY_AND_ROLE, args );
    }

    @Override
    public void updateCompanyAndRoleNamed(List<User> users) {
        var args = users.stream()
                                                              .map( user -> Map.of( "companyId", user.getCompany().getId(),
                                                                                    "role", user.getRole().name(),
                                                                                    "id", user.getId() ) )
                                                              .map( MapSqlParameterSource::new )
                                                              .toArray( MapSqlParameterSource[]::new );
        namedParameterJdbcTemplate.batchUpdate( UPDATE_COMPANY_AND_ROLE_NAMED, args);
    }
}
