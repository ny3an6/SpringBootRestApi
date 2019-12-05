package com.ndmitrenko.dinospringbootapp.dto.request.contact;

import com.ndmitrenko.dinospringbootapp.dto.request.user.UserCreateContactDto;
import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class UserContactRequestDto {
    private String firstName;
    private String lastName;
    private Long number;
    private Long contactId;
    private UserCreateContactDto user;
}
