package br.com.contact.api.controller;

import br.com.contact.api.entity.model.request.UserRequest;
import br.com.contact.api.entity.model.response.*;
import br.com.contact.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserControllerTest {
    // Address
    private static final String STREET = "123 Main St";
    private static final String CITY = "São Paulo";
    private static final String STATE = "SP";
    private static final int ZIP = 12345;

    // Phone
    private static final String PHONE_NUMBER = "123 - 456 - 7890";
    private static final String PHONE_TYPE = "Casa";
    private static final String PHONE_NUMBER_PHONE = "555-123-4567";
    private static final String PHONE_TYPE_PHONE = "Mobile";

    // User
    private static final Long USER_ID = 1L;
    private static final String FULL_NAME = "User";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "123456";
    private static final Boolean ACCOUNT_NOT_EXPIRED = true;
    private static final Boolean ACCOUNT_NON_LOCKED = true;
    private static final Boolean CREDENTIALS_NON_EXPIRED = true;
    private static final Boolean ENABLED = true;

    // Permission
    private static final Long PERMISSION_ID = 1L;
    private static final String DESCRIPTION = "ADMIN";

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;

    private UserRequest userRequest;
    private UserResponse userResponse;

    UserControllerTest() {
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.startUserRequest();
        this.startUserResponse();
    }

    @Test
    @DisplayName("When the list of users is returned successfully")
    void findAllUsersThenReturnSuccess() {
        when(this.userService.findAll()).thenReturn(List.of(userResponse));
        //when(modelMapperConfig.parseObject(any(), any())).thenReturn(userResponse);
        when(modelMapper.map(any(), any())).thenReturn(this.userResponse);
        ResponseEntity<List<UserResponse>> response = this.userController.findAll();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserResponse.class, response.getBody().get(0).getClass());

        assertEquals(USER_ID, response.getBody().get(0).getId());
    }

    @Test
    @DisplayName("When the search for id is returned successfully")
    void findByIdUserThenReturnSuccess() {
        when(this.userService.findById(anyLong())).thenReturn(userResponse);
        when(modelMapper.map(any(), any())).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = this.userController.findById(USER_ID);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserResponse.class, response.getBody().getClass());
        assertEquals(USER_ID, response.getBody().getId());
    }

    @Test
    @DisplayName("When the user is created successfully")
    void createUserThenReturnSuccess() {
        when(this.userService.create(any())).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.create(userRequest);

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("When the user is updated successfully")
    void updateUserThenReturnSuccess() {
        when(this.userService.update(USER_ID, userRequest)).thenReturn(userResponse);
        when(modelMapper.map(any(), any())).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.update(USER_ID, userRequest);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(UserResponse.class, response.getBody().getClass());

        assertEquals(USER_ID, response.getBody().getId());
    }

    @Test
    void deleteThenReturnSuccess() {
        doNothing().when(this.userService).delete(anyLong());

        ResponseEntity<Void> response = userController.delete(USER_ID);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        verify(userService, times(1)).delete(anyLong());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private void startUserResponse() {
        // Address instantiation
        AddressResponse address = new AddressResponse(STREET, CITY, STATE, ZIP);
        // Phone instantiation
        PhoneResponse phone = new PhoneResponse(PHONE_NUMBER_PHONE, PHONE_TYPE_PHONE);
        // ContactInfo instantiation
        ContactInfoResponse contactInfo = new ContactInfoResponse(address, phone);
        // Permission instantiation
        PermissionResponse permission = new PermissionResponse(PERMISSION_ID, DESCRIPTION);

        // User instantiation
        userResponse = new UserResponse();
        userResponse.setId(USER_ID);
        userResponse.setFullname(FULL_NAME);
        userResponse.setUsername(USERNAME);
        userResponse.setAccountNonExpired(ACCOUNT_NOT_EXPIRED);
        userResponse.setAccountNonLocked(ACCOUNT_NON_LOCKED);
        userResponse.setCredentialsNonExpired(CREDENTIALS_NON_EXPIRED);
        userResponse.setEnabled(ENABLED);
        userResponse.getAddresses().add(address);
        userResponse.setContactInfo(contactInfo);
        userResponse.setPermissions(Set.of(permission));
    }

    private void startUserRequest() {
        // AddressResponse instantiation
        AddressResponse address = new AddressResponse(STREET, CITY, STATE, ZIP);
        // PhoneResponse instantiation
        PhoneResponse phone = new PhoneResponse(PHONE_NUMBER_PHONE, PHONE_TYPE_PHONE);
        // ContactInfoResponse instantiation
        ContactInfoResponse contactInfo = new ContactInfoResponse(address, phone);
        // PermissionResponse instantiation
        PermissionResponse permission = new PermissionResponse(PERMISSION_ID, DESCRIPTION);

        // UserRequest instantiation
        userRequest = new UserRequest();
        userRequest.setFullname(FULL_NAME);
        userRequest.setUsername(USERNAME);
        userRequest.setPassword(PASSWORD);
        userRequest.setAccountNonExpired(ACCOUNT_NOT_EXPIRED);
        userRequest.setAccountNonLocked(ACCOUNT_NON_LOCKED);
        userRequest.setCredentialsNonExpired(CREDENTIALS_NON_EXPIRED);
        userRequest.setEnabled(ENABLED);
        userRequest.getAddresses().add(address);
        userRequest.setContactInfo(contactInfo);
        userRequest.setPermissions(Set.of(permission));
    }
}