package com.ndmitrenko.dinospringbootapp.dto.request.user;

import lombok.Data;

@Data
public class UserCreateRequestDto {
    private String firstName;
    private String lastName;
    private Long userId;
}
