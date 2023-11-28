package br.com.contact.api.service;

import br.com.contact.api.config.security.jwt.JwtTokenProvider;
import br.com.contact.api.entity.Permission;
import br.com.contact.api.entity.User;
import br.com.contact.api.entity.model.security.AccountCredentialsRequest;
import br.com.contact.api.entity.model.security.TokenResponse;
import br.com.contact.api.service.exceptions.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    private static final String AUTHORIZATION_EXCEPTION = "Invalid username/password supplied!";
    private static final int jwtExpiration = 86400000;
    private final Date now = new Date();
    private final Date validity = new Date(now.getTime() + jwtExpiration);
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
    // Token
    private static final Boolean AUTHENTICATED = true;
    private static final Date CREATED = new Date();
    private final Date EXPIRATION = validity;
    private static final String ACCESS_TOKEN = "fakeAccessToken";
    private static final String REFRESH_TOKEN = "fakeRefreshAccessToken";


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.startUser();
    }

    @Test
    @DisplayName("When the token is generated successfully")
    void loginValidCredentialsThenReturnTokenResponse() {
        AccountCredentialsRequest request = new AccountCredentialsRequest(USERNAME, PASSWORD);

        when(this.authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));

        when(this.userService.findByUsername(USERNAME)).thenReturn(user);

        when(this.tokenProvider.createAccessToken(USERNAME, user.getRoles()))
                .thenReturn(new TokenResponse(USERNAME, AUTHENTICATED, CREATED, EXPIRATION, ACCESS_TOKEN, REFRESH_TOKEN));

        TokenResponse response = this.authService.login(request);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals(REFRESH_TOKEN, response.getRefreshToken());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(AUTHENTICATED, response.getAuthenticated());
    }

    @Test
    @DisplayName("When the generate token return AuthorizationException")
    void login_InvalidCredentials_ShouldThrowAuthorizationException() {

        AccountCredentialsRequest request = new AccountCredentialsRequest(USERNAME, PASSWORD);

        when(this.authenticationManager.authenticate(any()))
                .thenThrow(new AuthorizationException(AUTHORIZATION_EXCEPTION));

        try {
            this.authService.login(request);
        } catch (Exception e) {
            assertEquals(AuthorizationException.class, e.getClass());
            assertEquals(AUTHORIZATION_EXCEPTION, e.getMessage());
        }
    }

    @Test
    @DisplayName("When the refreshToken is generated successfully")
    void refreshTokenValidThenReturnTokenResponse() {
        when(this.userService.findByUsername(USERNAME)).thenReturn(user);

        when(this.tokenProvider.refreshToken(REFRESH_TOKEN))
                .thenReturn(new TokenResponse(USERNAME, AUTHENTICATED, CREATED, EXPIRATION, ACCESS_TOKEN, REFRESH_TOKEN));

        TokenResponse response = this.authService.refreshToken(USERNAME, REFRESH_TOKEN);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals(REFRESH_TOKEN, response.getRefreshToken());
        assertEquals(USERNAME, response.getUsername());
        assertEquals(AUTHENTICATED, response.getAuthenticated());
    }

    private void startUser() {
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
        user.setPermissions(Set.of(permission));
    }
}