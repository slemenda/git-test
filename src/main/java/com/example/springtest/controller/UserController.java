package com.example.springtest.controller;

import com.example.springtest.exceptions.UserNotFoundException;
import com.example.springtest.response.ErrorResponse;
import com.example.springtest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.testng.annotations.Test;

import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/user")
    public String getUser (@RequestParam("name") String name ){
        return String.format("This is %s", name);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getByUserId(@PathVariable("id") UUID id) throws Exception{
        var user = userService.getUser(id);
        return ResponseEntity.ok().body(user);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle (UserNotFoundException exception) {
        var errorResponse = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
