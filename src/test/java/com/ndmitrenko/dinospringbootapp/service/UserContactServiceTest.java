package com.ndmitrenko.dinospringbootapp.service;


import com.ndmitrenko.dinospringbootapp.dto.request.contact.UserContactRequestDto;
import com.ndmitrenko.dinospringbootapp.dto.request.user.UserCreateContactDto;
import com.ndmitrenko.dinospringbootapp.dto.response.contact.UserContactResponseDto;
import com.ndmitrenko.dinospringbootapp.dto.response.exception.DefaultExceptionResponse;
import com.ndmitrenko.dinospringbootapp.exception.ApiResult;
import com.ndmitrenko.dinospringbootapp.exception.DefaultException;
import com.ndmitrenko.dinospringbootapp.model.User;
import com.ndmitrenko.dinospringbootapp.model.UserContact;
import com.ndmitrenko.dinospringbootapp.repository.UserContactRepository;
import com.ndmitrenko.dinospringbootapp.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserContactServiceTest {

    @Autowired
    private UserContactService userContactService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserContactRepository userContactRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getContactById() {
        UserContactResponseDto userContact = new UserContactResponseDto("Alla", "Pugacheva", 8981838333L, 55L);
        given(userContactRepository.findByContactId(55L)).willReturn(Optional.of(new UserContact(
                "Alla", "Pugacheva", 8981838333L, 55L)));
        Assert.assertEquals(userContactService.getContactById(55L), userContact);
        verify(userContactRepository, times(1)).findByContactId(anyLong());
    }

    @Test
    public void createNewContact() {
        UserContactRequestDto userContactRequestDto = new UserContactRequestDto("Alla", "Pugacheva", 89824313924L,
                55L, new UserCreateContactDto(123L));
        User user = new User("Nikita", "Dmitrenko", 123L);
        given(userContactRepository.findByContactNumber(userContactRequestDto.getNumber())).willReturn(Optional.empty());
        given(userRepository.findUserByUserId(userContactRequestDto.getUser().getUserId())).willReturn(Optional.of(new User("Nikita", "Dmitrenko", 123L)));

        userContactService.createNewContact(userContactRequestDto);

        UserContact userContact = UserContact.builder()
                .contactFirstName(userContactRequestDto.getFirstName())
                .contactLastName(userContactRequestDto.getLastName())
                .user(user)
                .contactNumber(userContactRequestDto.getNumber())
                .contactId(userContactRequestDto.getContactId())
                .build();

        verify(userContactRepository, times(1)).save(userContact);
    }



    @Test
    public void createNewContactTestingException() {
        thrown.expect(DefaultException.class);
        thrown.expectMessage("Contact is already exist");
        thrown.expect(Matchers.hasProperty("code"));
        thrown.expect(Matchers.hasProperty("code", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY)));
        thrown.expect(Matchers.hasProperty("apiResult"));
        thrown.expect(Matchers.hasProperty("apiResult", Matchers.is(ApiResult.FAIL)));

        UserContactRequestDto userContact = new UserContactRequestDto("Alla", "Pugacheva", 89824313924L,
                55L, new UserCreateContactDto(123L));
        given(userContactRepository.findByContactNumber(userContact.getNumber())).willReturn(Optional.of(new UserContact()));
        given(userRepository.findUserByUserId(userContact.getUser().getUserId())).willReturn(Optional.of(new User()));
        userContactService.createNewContact(userContact);
    }

    @Test
    public void editUserContactInformation() {
        Long number = 89811123322L;
        UserContact userContact = new UserContact("Alla", "Pugacheva", 89824313924L, 55L);

        given(userContactRepository.findByContactNumber(number)).willReturn(Optional.of(userContact));

        UserContactRequestDto userContactRequestDto = new UserContactRequestDto("Masha", "Lepnitskaya", 89824313924L,
                55L, new UserCreateContactDto(123L));

        userContactService.editUserContactInformation(number, userContactRequestDto);

        verify(userContactRepository, times(1)).save(any());

        Assert.assertEquals(userContact.getContactFirstName(), userContactRequestDto.getFirstName());
        Assert.assertEquals(userContact.getContactLastName(), userContactRequestDto.getLastName());
        Assert.assertEquals(userContact.getContactId(), userContactRequestDto.getContactId());
        Assert.assertEquals(userContact.getContactNumber(), userContactRequestDto.getNumber());
    }

    @Test
    public void deleteContact() {
        Long number = 89238928392L;

        UserContact userContact = new UserContact("Nikita", "Dmitrenko", 89182931922L,123L);

        when(userContactRepository.findByContactNumber(number)).thenReturn(Optional.of(userContact));

        userContactService.deleteContact(number);

        verify(userContactRepository, times(1)).findByContactNumber(anyLong());
        verify(userContactRepository, times(1)).deleteContact(anyObject());
        Assert.assertEquals(userContactService.deleteContact(number), new DefaultExceptionResponse(HttpStatus.ACCEPTED, "Contact has been successfully deleted", ApiResult.SUCCESS));
    }

    @Test
    public void getContactByNumber() {
        Long number = 89182312332L;
        UserContactResponseDto userContact = new UserContactResponseDto("Alla", "Pugacheva", 8981838333L, 55L);
        given(userContactRepository.findByContactNumber(number)).willReturn(Optional.of(new UserContact(
                "Alla", "Pugacheva", number, 55L)));
        Assert.assertEquals(userContactService.getContactByNumber(number).getClass(), userContact.getClass());
        verify(userContactRepository, times(1)).findByContactNumber(anyLong());
    }
}
