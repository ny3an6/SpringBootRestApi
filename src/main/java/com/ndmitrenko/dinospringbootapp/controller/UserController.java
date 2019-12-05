package com.ndmitrenko.dinospringbootapp.controller;

import com.ndmitrenko.dinospringbootapp.dto.request.user.UserCreateRequestDto;
import com.ndmitrenko.dinospringbootapp.dto.response.contact.UserContactResponseDto;
import com.ndmitrenko.dinospringbootapp.dto.response.exception.DefaultExceptionResponse;
import com.ndmitrenko.dinospringbootapp.dto.response.user.UserDto;
import com.ndmitrenko.dinospringbootapp.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Show All users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @GetMapping() // Show all existing user
    public List<UserDto> getAllUsers(){
       return userService.getAllUsers();
    }

    @ApiOperation(value = "Show user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @GetMapping("/showUser/{id}")
    public UserDto getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @ApiOperation(value = "Create user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @PostMapping("/create") // Create new user
    public UserDto addUser(@RequestBody UserCreateRequestDto user){
        return userService.createUser(user);
    }


    //TODO: cannot change id variable while edit user information
    @ApiOperation(value = "Edit user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "editing was successful"),
    })
    @PutMapping("/edit/{lastName}") //Edit user Information
    public UserDto editUser(@PathVariable String lastName, @Valid @RequestBody UserCreateRequestDto user){
        return userService.editUserInformation(lastName, user);
    }

    @ApiOperation(value = "Delete user by second name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "deleting was successful"),
    })
    @DeleteMapping("/delete/{secondName}") // delete user by secondName
    public DefaultExceptionResponse deleteUser(@PathVariable String secondName){
        return userService.deleteUser(secondName);
    }

    @GetMapping("/contactsUserList/{lastName}")
    public List<UserContactResponseDto> getListOfUserContacts(@PathVariable String lastName){
        return userService.getContactsList(lastName);
    }

    @GetMapping("/showByPartOfName/{lastName}")
    public List<UserDto> getUsersByPartOfTheirName(@PathVariable String lastName){
        return userService.getUserByPartOfTheirName(lastName);
    }
}
