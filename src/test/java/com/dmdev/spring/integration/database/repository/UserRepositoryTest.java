package com.dmdev.spring.integration.database.repository;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.spring.dto.PersonalInfo;
import com.dmdev.spring.dto.UserFilter;
import com.dmdev.spring.integration.IntegrationTestBase;
import com.dmdev.spring.integration.annotation.IT;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void checkBatch() {
        var users = userRepository.findAll();
        userRepository.updateCompanyAndRole( users );
        System.out.println();
    }

    @Test
    void checkJdbcTemplate() {
        var users = userRepository.findAllByCompanyIdAndRole( 1, Role.USER );
         assertThat( users ).hasSize( 1 );
    }

    @Test
    void checkAuditing() {
        var ivan = userRepository.findById( 1L ).get();
        ivan.setBirthDate( ivan.getBirthDate().plusYears( 1L ) );
        userRepository.flush();
        System.out.println();
    }

    @Test
    void checkCustomImplementation() {
        UserFilter filter = new UserFilter(
                null,
                "ov",
                LocalDate.now()
        );
        var users = userRepository.findAllByFilter( filter );
        assertThat(users).hasSize( 4 );
    }

    @Test
    void checkProjections() {
        var users = userRepository.findAllByCompanyId( 1 );
        assertThat(users).hasSize( 2 );
        System.out.println();
    }

    @Test
    void checkPageable() {
        var pageable = PageRequest.of(1,2,Sort.by( "id" ));
        var slice = userRepository.findAllBy( pageable );
        slice.forEach( user -> System.out.println(user.getCompany().getName()) );
        while(slice.hasNext()) {
            slice = userRepository.findAllBy( slice.nextPageable() );
            slice.forEach( user -> System.out.println(user.getCompany().getName()) );
        }
    }

    @Test
    void checkSort() {
        var sort = Sort.sort( User.class );
        sort.by(User::getFirstname)
              .and(sort.by(User::getLastname));
        var sortById = Sort.by( "firstname").and( Sort.by("lastname") );
        var allUsers = userRepository.findTop3ByBirthDateBefore( LocalDate.now(), sort );
        assertThat(allUsers).hasSize(3);
    }

    @Test
    void checkFirstTop() {

        var topUser = userRepository.findTopByOrderByIdDesc();
        assertTrue( topUser.isPresent() );
        topUser.ifPresent( user -> assertEquals( 5L, user.getId() ));
    }

    @Test
    void checkUpdate() {
        var ivan = userRepository.getById( 1L );
        assertSame(Role.ADMIN, ivan.getRole());
        ivan.setBirthDate( LocalDate.now() );

        var resultCount = userRepository.updateRole( Role.USER, 1L, 5L );
        assertEquals( 2, resultCount);

        var theSameIvan = userRepository.getById( 1L );
        assertSame(Role.USER, theSameIvan.getRole());
    }


    @Test
    void checkQueries() {
        var users = userRepository.findAllBy( "a", "ov" );
        assertThat(users).hasSize( 3 );
        System.out.println(users);
    }
}