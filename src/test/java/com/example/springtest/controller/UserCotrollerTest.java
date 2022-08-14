package com.example.springtest.controller;
import com.example.springtest.entities.UserEntity;
import com.example.springtest.exceptions.UserNotFoundException;
import com.example.springtest.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return correct user name from get user method")
    void getUser() throws Exception {
        // query parameter
        RequestBuilder requestBuilder = get("/user?name=Tomas");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals("This is Tomas", mvcResult.getResponse().getContentAsString());

//        var userController = new UserController();
//        assertEquals("This is Tomas", userController.getUser("Tomas"));
    }


    @Test
    @DisplayName("Get user by id")
    void getUser_by_id() throws Exception {
        doReturn(Optional.of(new UserEntity(UUID.fromString("79bf947e-a797-4ef4-923d-15cb8cd85cd7"), "Tomas", "Svojanovsky")))
                .when(userService).getUser(UUID.fromString("79bf947e-a797-4ef4-923d-15cb8cd85cd7"));

        mockMvc.perform(get("/user/{id}", "79bf947e-a797-4ef4-923d-15cb8cd85cd7"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Tomas")))
                .andExpect(jsonPath("$.lastName", is("Svojanovsky")));
    }
    @Test
    @DisplayName ("Get a user by id not found exception")
    void getUser_exception() throws Exception {
        when(userService.getUser(UUID.fromString("79bf947e-a797-4ef4-923d-15cb8cd85cd6")))
                .thenThrow(new UserNotFoundException("User not found"));
        mockMvc.perform(get("/user/{id}", "79bf947e-a797-4ef4-923d-15cb8cd85cd7"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()instanceof UserNotFoundException))
                .andExpect(result -> assertEquals( "User not found", result.getResolvedException().getMessage() ))
                ;
    }

}