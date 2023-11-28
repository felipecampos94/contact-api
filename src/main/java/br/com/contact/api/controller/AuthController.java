package br.com.contact.api.controller;

import br.com.contact.api.entity.model.security.AccountCredentialsRequest;
import br.com.contact.api.entity.model.security.TokenResponse;
import br.com.contact.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AccountCredentialsRequest request) {
        return ResponseEntity.ok().body(this.authService.login(request));
    }

    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity<TokenResponse> refreshToken(@PathVariable("username") String username,
                                       @RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok().body(this.authService.refreshToken(username, refreshToken));
    }
}
