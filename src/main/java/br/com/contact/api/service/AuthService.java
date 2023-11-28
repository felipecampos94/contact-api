package br.com.contact.api.service;

import br.com.contact.api.config.security.jwt.JwtTokenProvider;
import br.com.contact.api.entity.model.security.AccountCredentialsRequest;
import br.com.contact.api.entity.model.security.TokenResponse;
import br.com.contact.api.service.exceptions.AuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public TokenResponse login(AccountCredentialsRequest request) {
        try {
            var username = request.getUsername();
            var password = request.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = this.userService.findByUsername(username);
            return tokenProvider.createAccessToken(username, user.getRoles());
        } catch (Exception e) {
            throw new AuthorizationException("Invalid username/password supplied!");
        }
    }

    public TokenResponse refreshToken(String username, String refreshToken) {
        this.userService.findByUsername(username);

        return tokenProvider.refreshToken(refreshToken);
    }
}
