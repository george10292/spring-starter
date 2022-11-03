package com.dmdev.spring.integration.database.repository;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.database.entity.User;
import com.dmdev.spring.database.repository.UserRepository;
import com.dmdev.spring.integration.annotation.IT;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Test
    void checkPageable() {
        var pageable = PageRequest.of(1,2,Sort.by( "id" ));
        var slice = userRepository.findAllBy( pageable );
        slice.forEach( user -> System.out.println(user.getId()) );
        while(slice.hasNext()) {
            slice = userRepository.findAllBy( slice.nextPageable() );
            slice.forEach( user -> System.out.println(user.getId()) );
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