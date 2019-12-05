package com.ndmitrenko.dinospringbootapp.dto.request.user;

import lombok.Data;

// TODO: create new dto for creating contacts
@Data
public class UserCreateRequestDto {
    private String firstName;
    private String lastName;
    private Long userId;
}
