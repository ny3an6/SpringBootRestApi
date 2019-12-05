package com.ndmitrenko.dinospringbootapp.service;

import com.ndmitrenko.dinospringbootapp.dto.request.contact.UserContactRequestDto;
import com.ndmitrenko.dinospringbootapp.dto.response.contact.UserContactResponseDto;
import com.ndmitrenko.dinospringbootapp.dto.response.exception.DefaultExceptionResponse;
import com.ndmitrenko.dinospringbootapp.exception.ApiResult;
import com.ndmitrenko.dinospringbootapp.exception.DefaultException;
import com.ndmitrenko.dinospringbootapp.model.User;
import com.ndmitrenko.dinospringbootapp.model.UserContact;
import com.ndmitrenko.dinospringbootapp.repository.UserContactRepository;
import com.ndmitrenko.dinospringbootapp.repository.UserRepository;
import com.ndmitrenko.dinospringbootapp.util.ToUpperCaseString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ndmitrenko.dinospringbootapp.dto.response.user.UserDto.toDto;
import static com.ndmitrenko.dinospringbootapp.dto.response.contact.UserContactResponseDto.toDto;

@Service
public class UserContactService {

    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private UserRepository userRepository;

    public UserContactResponseDto getContactById(Long id){
        return toDto(userContactRepository.findByContactId(id).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "Contact not found")));
    }

    public UserContactResponseDto createNewContact(UserContactRequestDto userContactRequestDto){
        if (userContactRequestDto.getFirstName().trim().isEmpty()){ // fields must be not null, checking for empty fields
            throw new DefaultException(HttpStatus.BAD_REQUEST,"Enter first name");
        }
        if(userContactRequestDto.getLastName().trim().isEmpty()){
            throw new DefaultException(HttpStatus.BAD_REQUEST, "Enter last name");
        }

        if(userContactRepository.findByContactId(userContactRequestDto.getContactId()).isPresent()){
            throw new DefaultException(HttpStatus.UNPROCESSABLE_ENTITY, "Contact_id is unique variable");
        }

        String contactFirstName = ToUpperCaseString.toUpperCase(userContactRequestDto.getFirstName().toLowerCase().trim()); // validate user firstName and secondName
        String contactLastName = ToUpperCaseString.toUpperCase(userContactRequestDto.getLastName().toLowerCase().trim());

        if(userContactRequestDto.getUser() == null){
            throw new DefaultException(HttpStatus.BAD_REQUEST, "Enter user of this contacts");
        }

        User user = userRepository.findUserByUserId(userContactRequestDto.getUser().getUserId()).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "User not found"));

        userContactRepository.findByContactNumber(userContactRequestDto.getNumber()).ifPresent(ex -> { // checking for existing user
            throw new DefaultException(HttpStatus.UNPROCESSABLE_ENTITY, "Contact is already exist"); });

        userContactRequestDto.setFirstName(contactFirstName); // save edited fields
        userContactRequestDto.setLastName(contactLastName);

        UserContact userContact = UserContact.builder()
                .contactFirstName(userContactRequestDto.getFirstName())
                .contactLastName(userContactRequestDto.getLastName())
                .user(user)
                .contactNumber(userContactRequestDto.getNumber())
                .contactId(userContactRequestDto.getContactId())
                .build();

        userContactRepository.save(userContact);

        return toDto(userContact);
    }

    public UserContactResponseDto editUserContactInformation(Long number, UserContactRequestDto requestDto){
        UserContact userContact = userContactRepository.findByContactNumber(number).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "Contact not found")); // checking for user existing
        userContact.setContactFirstName(ToUpperCaseString.toUpperCase(requestDto.getFirstName().toLowerCase().trim())); // save and validate firstName and secondName
        userContact.setContactLastName(ToUpperCaseString.toUpperCase(requestDto.getLastName().toLowerCase().trim()));
        userContact.setContactNumber(requestDto.getNumber());
        userContactRepository.save(userContact);
        return toDto(userContact);
    }

    public DefaultExceptionResponse deleteContact(Long number){
        UserContact userContact = userContactRepository.findByContactNumber(number).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "Contact not found"));
        userContactRepository.deleteContact(userContact.getId());
        return new DefaultExceptionResponse(HttpStatus.ACCEPTED, "Contact has been successfully deleted", ApiResult.SUCCESS);
    }

    public UserContactResponseDto getContactByNumber(Long number){
        return toDto(userContactRepository.findByContactNumber(number).orElseThrow(()->
                new DefaultException(HttpStatus.NOT_FOUND, "Contact not found")));
    }
}
