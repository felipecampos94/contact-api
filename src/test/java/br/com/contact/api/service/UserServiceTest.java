package br.com.contact.api.service;

import br.com.contact.api.entity.*;
import br.com.contact.api.entity.model.request.UserRequest;
import br.com.contact.api.entity.model.response.*;
import br.com.contact.api.repository.UserRepository;
import br.com.contact.api.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

    // Exception
    private static final String OBJECT_NOT_FOUND = "Object Not Found!";

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
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private UserRequest userRequest;
    private User user;
    private Optional<User> optionalUser;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.startUser();
        this.startUserRequest();
    }

    @Test
    @DisplayName("When the list of users is returned successfully")
    void findAllUsers() {
        when(this.userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> response = this.userService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(UserResponse.class, response.get(0).getClass());
        assertEquals(USER_ID, response.get(0).getId());
    }

    @Test
    @DisplayName("When the search for id is returned successfully")
    void findByIdUserSuccess() {
        when(this.userRepository.findById(anyLong())).thenReturn(optionalUser);

        UserResponse response = this.userService.findById(1L);

        assertNotNull(response);
        assertNotNull(response.getAddresses());
        assertNotNull(response.getContactInfo());
        assertNotNull(response.getPermissions());
        assertEquals(UserResponse.class, response.getClass());
        assertEquals(USER_ID, response.getId());
        assertEquals(FULL_NAME, response.getFullname());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(ACCOUNT_NOT_EXPIRED, response.getAccountNonExpired());
        assertEquals(ACCOUNT_NON_LOCKED, response.getAccountNonLocked());
        assertEquals(CREDENTIALS_NON_EXPIRED, response.getCredentialsNonExpired());
        assertEquals(ENABLED, response.getEnabled());
    }

    @Test
    @DisplayName("When the search for id returns ObjectNotFoundException")
    void findByIdReturnObjectNotFoundException() {
        when(userRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            userService.findById(USER_ID);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJECT_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    @DisplayName("When the search for username is returned successfully")
    void findByUsernameReturnSuccess() {
        when(this.userRepository.findByUsername(anyString())).thenReturn(optionalUser);

        User response = this.userService.findByUsername(USERNAME);

        assertNotNull(response);
        assertNotNull(response.getAddresses());
        assertNotNull(response.getContactInfo());
        assertNotNull(response.getPermissions());
        assertEquals(User.class, response.getClass());
        assertEquals(USER_ID, response.getId());
        assertEquals(FULL_NAME, response.getFullname());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(ACCOUNT_NOT_EXPIRED, response.getAccountNonExpired());
        assertEquals(ACCOUNT_NON_LOCKED, response.getAccountNonLocked());
        assertEquals(CREDENTIALS_NON_EXPIRED, response.getCredentialsNonExpired());
        assertEquals(ENABLED, response.getEnabled());
    }

    @Test
    @DisplayName("When the search for username returns ObjectNotFoundException")
    void findByUsernameReturnObjectNotFoundException() {
        when(this.userRepository.findByUsername(anyString())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            this.userService.findByUsername(USERNAME);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJECT_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    @DisplayName("When the search for username is returned successfully")
    void loadUserByUsernameReturnSuccess() {
        when(this.userRepository.findByUsername(anyString())).thenReturn(optionalUser);

        UserDetails response = this.userService.loadUserByUsername(USERNAME);

        assertNotNull(response);
        assertNotNull(response.getAuthorities());
        assertEquals(User.class, response.getClass());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    @DisplayName("When the search for username returns Object Not Found Exception")
    void loadUserByUsernameReturnObjectNotFoundException() {
        when(this.userRepository.findByUsername(anyString())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            this.userService.loadUserByUsername(USERNAME);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJECT_NOT_FOUND, e.getMessage());
        }
    }

    @Test
    @DisplayName("When the user is created successfully")
    void createUserSuccess() {
        when(this.userRepository.save(any())).thenReturn(user);

        UserResponse response = this.userService.create(userRequest);

        assertNotNull(response);
        assertNotNull(response.getAddresses());
        assertNotNull(response.getContactInfo());
        assertNotNull(response.getPermissions());
        assertEquals(UserResponse.class, response.getClass());
        assertEquals(USER_ID, response.getId());
        assertEquals(FULL_NAME, response.getFullname());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(ACCOUNT_NOT_EXPIRED, response.getAccountNonExpired());
        assertEquals(ACCOUNT_NON_LOCKED, response.getAccountNonLocked());
        assertEquals(CREDENTIALS_NON_EXPIRED, response.getCredentialsNonExpired());
        assertEquals(ENABLED, response.getEnabled());
    }

    @Test
    @DisplayName("When the user is updated successfully")
    void updateUserSuccess() {
        when(this.userRepository.findById(anyLong())).thenReturn(optionalUser);

        when(this.userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = this.userService.update(USER_ID, userRequest);

        assertNotNull(response);
        assertNotNull(response.getAddresses());
        assertNotNull(response.getContactInfo());
        assertNotNull(response.getPermissions());
        assertEquals(UserResponse.class, response.getClass());
        assertEquals(USER_ID, response.getId());
        assertEquals(FULL_NAME, response.getFullname());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(ACCOUNT_NOT_EXPIRED, response.getAccountNonExpired());
        assertEquals(ACCOUNT_NON_LOCKED, response.getAccountNonLocked());
        assertEquals(CREDENTIALS_NON_EXPIRED, response.getCredentialsNonExpired());
        assertEquals(ENABLED, response.getEnabled());
    }



    @Test
    @DisplayName("When the user is deleted successfully")
    void deleteUserSuccess() {
        when(this.userRepository.findById(anyLong())).thenReturn(optionalUser);
        doNothing().when(this.userRepository).deleteById(anyLong());
        this.userService.delete(USER_ID);
        verify(this.userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("When the user is not deleted returns ObjectNotFoundException")
    void deleteUserWithObjectNotFoundException() {
        when(this.userRepository.findById(anyLong())).thenThrow(new ObjectNotFoundException(OBJECT_NOT_FOUND));

        try {
            this.userService.delete(USER_ID);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals(OBJECT_NOT_FOUND, e.getMessage());
        }
    }

    private void startUser() {
        // Address instantiation
        Address address = new Address(STREET, CITY, STATE, ZIP);
        // Phone instantiation
        Phone phone = new Phone(PHONE_NUMBER_PHONE, PHONE_TYPE_PHONE);
        // ContactInfo instantiation
        ContactInfo contactInfo = new ContactInfo(address, phone);
        // Permission instantiation
        Permission permission = new Permission(PERMISSION_ID, DESCRIPTION);

        // User instantiation
        user = new User();
        user.setId(USER_ID);
        user.setFullname(FULL_NAME);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setAccountNonExpired(ACCOUNT_NOT_EXPIRED);
        user.setAccountNonLocked(ACCOUNT_NON_LOCKED);
        user.setCredentialsNonExpired(CREDENTIALS_NON_EXPIRED);
        user.setEnabled(ENABLED);
        user.getAddresses().add(address);
        user.setContactInfo(contactInfo);
        user.setPermissions(Set.of(permission));

        optionalUser = Optional.of(user);
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