package com.example.springtest.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {
    private UUID id;

    private String firstName;

    private String lastName;
}

