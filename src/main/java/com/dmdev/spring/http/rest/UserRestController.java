package com.dmdev.spring.http.rest;

import com.dmdev.spring.database.entity.Role;
import com.dmdev.spring.dto.PageResponse;
import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.dto.UserFilter;
import com.dmdev.spring.dto.UserReadDto;
import com.dmdev.spring.service.CompanyService;
import com.dmdev.spring.service.UserService;
import com.dmdev.spring.validation.group.CreateAction;
import com.dmdev.spring.validation.group.UpdateAction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.groups.Default;

import static org.springframework.http.ResponseEntity.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {
    private final UserService userService;
    private final CompanyService companyService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponse<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        var page = userService.findAll(filter, pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Long id) {
        return userService.findById( id )
                          .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

//    @GetMapping(value = "/{id}/avatar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public byte[] findavater(@PathVariable("id") Long id) {
//        return userService.findAvatar( id )
//                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND ));
//    }

    @GetMapping(value = "/{id}/avatar")
    public ResponseEntity<byte []> findavater(@PathVariable("id") Long id) {
        return userService.findAvatar( id )
                          .map(content -> ok()
                                  .header( HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE )
                                  .contentLength( content.length )
                                  .body(content))
                .orElseGet( notFound()::build );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserReadDto create(@Validated({Default.class, CreateAction.class}) @RequestBody UserCreateEditDto user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public UserReadDto update(@PathVariable("id") Long id, @Validated({Default.class, UpdateAction.class}) @RequestBody UserCreateEditDto user) {
        return userService.update( id, user )
                          .orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return userService.delete( id )
                ? noContent().build()
                : notFound().build();
    }
}
