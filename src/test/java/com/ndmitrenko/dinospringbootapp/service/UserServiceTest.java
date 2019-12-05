package com.ndmitrenko.dinospringbootapp.service;

import com.ndmitrenko.dinospringbootapp.dto.request.user.UserCreateRequestDto;
import com.ndmitrenko.dinospringbootapp.dto.response.exception.DefaultExceptionResponse;
import com.ndmitrenko.dinospringbootapp.dto.response.user.UserDto;
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

import java.util.*;

import static com.ndmitrenko.dinospringbootapp.dto.response.contact.UserContactResponseDto.toDto;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserContactRepository userContactRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getAllUsers() {
        given(this.userRepository.findAll()).willReturn(Collections.singletonList(new User(
                "Nikita", "Dmitrenko", 123L)));
        List<UserDto> userList = new ArrayList<>();
        UserDto user = new UserDto("Nikita", "Dmitrenko", 123L);
        userList.add(user);
        Assert.assertEquals(userService.getAllUsers(), userList);
    }

    @Test
    public void getAllUsersTestException(){
        thrown.expect(DefaultException.class);
        thrown.expectMessage("There is no user");
        thrown.expect(Matchers.hasProperty("code"));
        thrown.expect(Matchers.hasProperty("code", Matchers.is(HttpStatus.NOT_FOUND)));
        thrown.expect(Matchers.hasProperty("apiResult"));
        thrown.expect(Matchers.hasProperty("apiResult", Matchers.is(ApiResult.FAIL)));

        given(this.userRepository.findAll()).willReturn(Collections.EMPTY_LIST);
        userService.getAllUsers();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUser() {
        given(this.userRepository.findUserByUserId(anyLong())).willReturn(Optional.of(new User(
                "Alex", "Smith", 124L)));
        Assert.assertEquals(userService.getUser(124L), new UserDto("Alex", "Smith", 124L));
        verify(userRepository, times(1)).findUserByUserId(anyLong());
    }

    @Test
    public void getUserTestException(){
        Long INCORRECT_ID = 12222L;
        thrown.expect(DefaultException.class);
        thrown.expectMessage("User not found");
        thrown.expect(Matchers.hasProperty("code"));
        thrown.expect(Matchers.hasProperty("code", Matchers.is(HttpStatus.NOT_FOUND)));
        thrown.expect(Matchers.hasProperty("apiResult"));
        thrown.expect(Matchers.hasProperty("apiResult", Matchers.is(ApiResult.FAIL)));

        given(this.userRepository.findUserByUserId(INCORRECT_ID)).willReturn(Optional.empty());
        userService.getUser(INCORRECT_ID);
        verify(userRepository, times(1)).findUserByUserId(anyLong());
    }

    @Test
    public void createUser() {
        UserCreateRequestDto user = new UserCreateRequestDto();
        user.setLastName("Smith");
        user.setFirstName("Alex");
        user.setUserId(124L);

        given(this.userRepository.findUserByUserId(anyLong())).willReturn(Optional.of(new User(
                "Alex", "Smith", 124L)));
        userService.createUser(user);
        Assert.assertEquals("Smith", userService.getUser(user.getUserId()).getLastName());
        Assert.assertEquals("Alex", userService.getUser(user.getUserId()).getFirstName());
        Assert.assertEquals(Optional.of(124L), Optional.of(userService.getUser(user.getUserId()).getUser_id()));
        verify(userRepository, times(1)).findUserByLastName(anyString());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void editUserInformation() {
        String lastName = "Dmitrenko";
        User user = new User("Nikita", "Dmitrenko", 123L);

        given(userRepository.findUserByLastName(lastName)).willReturn(Optional.of(user));

        UserCreateRequestDto editUser = new UserCreateRequestDto();
        editUser.setUserId(123L);
        editUser.setFirstName("Alex");
        editUser.setLastName("Smith");

        userService.editUserInformation(lastName, editUser);

        verify(userRepository, times(1)).save(any());

        Assert.assertEquals(user.getFirstName(), editUser.getFirstName());
        Assert.assertEquals(user.getLastName(), editUser.getLastName());
        Assert.assertEquals(user.getUserId(), editUser.getUserId());
    }

    @Test
    public void deleteUser() {
        String lastName = "Dmitrenko";
        User user = new User("Nikita", "Dmitrenko", 123L);

        when(userRepository.findUserByLastName("Dmitrenko")).thenReturn(Optional.of(user));

        userService.deleteUser(lastName);

        verify(userRepository, times(1)).findUserByLastName(anyString());
        verify(userRepository, times(1)).delete(anyObject());
        Assert.assertEquals(userService.deleteUser(lastName), new DefaultExceptionResponse(HttpStatus.ACCEPTED, "User has been successfully deleted", ApiResult.SUCCESS));
    }

    @Test
    public void getContactsList() {
        String lastName = "Dmitrenko";
        User user = new User("Nikita", "Dmitrenko", 123L);

        List<UserContact> userContacts = Arrays.asList(new UserContact(
                "Alla", "Pugacheva", 8981838333L, 55L),
                new UserContact("Anna", "Hilkevich", 8922332123L, 66L));

        given(userRepository.findUserByLastName(lastName)).willReturn(Optional.of(user));
        given(userContactRepository.findAllByUser(user)).willReturn(userContacts);

        userService.getContactsList(lastName);

        verify(userRepository, times(1)).findUserByLastName(lastName);
        verify(userContactRepository, times(1)).findAllByUser(user);

        Assert.assertNotNull(userService.getContactsList(lastName));
        Assert.assertArrayEquals(new List[]{toDto(userContacts)}, new List[]{userService.getContactsList(lastName)});
    }
}
