package com.ndmitrenko.dinospringbootapp.controller;

import com.ndmitrenko.dinospringbootapp.dto.request.contact.UserContactRequestDto;
import com.ndmitrenko.dinospringbootapp.dto.response.contact.UserContactResponseDto;
import com.ndmitrenko.dinospringbootapp.dto.response.exception.DefaultExceptionResponse;
import com.ndmitrenko.dinospringbootapp.service.UserContactService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/user_contacts")
public class UserContactController {

    @Autowired
    private UserContactService userContactService;

    @ApiOperation(value = "Get contact by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @GetMapping("/getContact/{id}")
    public UserContactResponseDto getContactById(@PathVariable Long id){
        return userContactService.getContactById(id);
    }

    @GetMapping("/getContactByNumber/{number}")
    public UserContactResponseDto getContactByNumber(@PathVariable Long number){
        return userContactService.getContactByNumber(number);
    }

    @ApiOperation(value = "Create new contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @PostMapping("/createContact")
    public UserContactResponseDto createContact(@RequestBody UserContactRequestDto userContact){
        return userContactService.createNewContact(userContact);
    }

    @DeleteMapping("/delete/{number}")
    public DefaultExceptionResponse removeContact(@PathVariable Long number){
        return userContactService.deleteContact(number);
    }

    @ApiOperation(value = "Edit contact by his number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @PutMapping("/edit/{number}")
    public UserContactResponseDto editContactInformation(@PathVariable Long number, @RequestBody UserContactRequestDto requestDto){
        return userContactService.editUserContactInformation(number, requestDto);
    }
}
