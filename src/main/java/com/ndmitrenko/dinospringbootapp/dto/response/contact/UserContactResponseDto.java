package com.ndmitrenko.dinospringbootapp.dto.response.contact;

import com.ndmitrenko.dinospringbootapp.model.UserContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
public class UserContactResponseDto {
    private String firstName;
    private String lastName;
    private Long number;
    private Long contactId;

    public static UserContactResponseDto toDto(UserContact userContact){
        return UserContactResponseDto.builder()
                .firstName(userContact.getContactFirstName())
                .lastName(userContact.getContactLastName())
                .number(userContact.getContactNumber())
                .contactId(userContact.getContactId())
                .build();
    }

    public static List<UserContactResponseDto> toDto(List<UserContact> userContacts){
        return userContacts.stream().map(UserContactResponseDto::toDto).collect(Collectors.toList());
    }
}
