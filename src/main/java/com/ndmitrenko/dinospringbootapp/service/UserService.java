package com.ndmitrenko.dinospringbootapp.service;

import com.ndmitrenko.dinospringbootapp.dto.request.user.UserCreateRequestDto;
import com.ndmitrenko.dinospringbootapp.dto.response.contact.UserContactResponseDto;
import com.ndmitrenko.dinospringbootapp.dto.response.exception.DefaultExceptionResponse;
import com.ndmitrenko.dinospringbootapp.dto.response.user.UserDto;
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

import java.util.List;

import static com.ndmitrenko.dinospringbootapp.dto.response.user.UserDto.toDto;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserContactRepository userContactRepository;

    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(!users.isEmpty()){
            return toDto(users);
        }else throw new DefaultException(HttpStatus.NOT_FOUND, "There is no user");
    }

    public UserDto getUser(Long id){
        return toDto(userRepository.findUserByUserId(id).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public UserDto createUser(UserCreateRequestDto requestUser){

        if (requestUser.getFirstName().trim().isEmpty()){ // fields must be not null, checking for empty fields
            throw new DefaultException(HttpStatus.BAD_REQUEST,"Enter first name");
        }
        if(requestUser.getLastName().trim().isEmpty()){
            throw new DefaultException(HttpStatus.BAD_REQUEST, "Enter second name");
        }

        String firstName = ToUpperCaseString.toUpperCase(requestUser.getFirstName().toLowerCase().trim()); // validate user firstName and secondName
        String secondName = ToUpperCaseString.toUpperCase(requestUser.getLastName().toLowerCase().trim());

        userRepository.findUserByLastName(secondName).ifPresent(ex -> { // checking for existing user
            throw new DefaultException(HttpStatus.UNPROCESSABLE_ENTITY, "User is already exist"); });
        requestUser.setFirstName(firstName); // save edited fields
        requestUser.setLastName(secondName);

        User user = User.builder()
                .userId(requestUser.getUserId())
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .build();
        userRepository.save(user);

        return toDto(user);
    }

    public UserDto editUserInformation(String lastName, UserCreateRequestDto user){
        String editLastName = ToUpperCaseString.toUpperCase(lastName.toLowerCase().trim());
        User userCandidate = userRepository.findUserByLastName(editLastName).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "User not found")); // checking for user existing
        userCandidate.setFirstName(ToUpperCaseString.toUpperCase(user.getFirstName().trim())); // save and validate firstName and secondName
        userCandidate.setLastName(ToUpperCaseString.toUpperCase(user.getLastName().trim()));
        userRepository.save(userCandidate);
        return toDto(userCandidate);
    }


    public DefaultExceptionResponse deleteUser(String lastName){
        String editLastName = ToUpperCaseString.toUpperCase(lastName.toLowerCase().trim());
        User userCandidate = userRepository.findUserByLastName(editLastName).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(userCandidate);
        return new DefaultExceptionResponse(HttpStatus.ACCEPTED, "User has been successfully deleted", ApiResult.SUCCESS);
    }

    public List<UserContactResponseDto> getContactsList(String lastName) {
        String editLastName = ToUpperCaseString.toUpperCase(lastName.toLowerCase().trim());
        User user = userRepository.findUserByLastName(editLastName).orElseThrow(() ->
                new DefaultException(HttpStatus.NOT_FOUND, "User not found"));
        List<UserContact> contacts = userContactRepository.findAllByUser(user);

        return UserContactResponseDto.toDto(contacts);
    }

    public List<UserDto> getUserByPartOfTheirName(String partOfLastName){
        String editPartOfLastName = ToUpperCaseString.toUpperCase(partOfLastName.toLowerCase().trim());
        return toDto(userRepository.findUserByLastNameContaining(editPartOfLastName).orElseThrow(()->
                new DefaultException(HttpStatus.NOT_FOUND, "User not found")));
    }
}


