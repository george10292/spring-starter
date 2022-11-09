package com.dmdev.spring.integration.http.controller;

import com.dmdev.spring.dto.UserCreateEditDto;
import com.dmdev.spring.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.hamcrest.collection.IsCollectionWithSize;

import static com.dmdev.spring.dto.UserCreateEditDto.Fields.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform( get( "/users"))
                .andExpect( status().is2xxSuccessful() )
                .andExpect( view().name( "user/users" ) )
                .andExpect( model().attributeExists( "users" ) )
                .andExpect( model().attribute( "users", IsCollectionWithSize.hasSize(5) ) );
    }

    @Test
    void create() throws Exception {
        mockMvc.perform( post( "/users" )
                                 .param( username, "test@gmail.com" )
                                 .param( firstname, "Test" )
                                 .param( lastname, "Test" )
                                 .param( role, "ADMIN" )
                                 .param( companyId, "1" )
                                 .param( birthDate, "2000-01-01" ))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern( "/users/{\\d+}" )
                );
    }
}