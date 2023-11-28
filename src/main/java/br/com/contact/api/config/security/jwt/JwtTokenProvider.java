package br.com.contact.api.config.security.jwt;

import br.com.contact.api.entity.model.security.TokenResponse;
import br.com.contact.api.service.exceptions.AuthorizationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${security.jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${security.jwt.jwtExpiration}")
    private long jwtExpiration;

    private final UserDetailsService userDetailsService;

    private static final String BEARER = "Bearer ";
    private static final String ROLES = "roles";

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
    }

    public TokenResponse createAccessToken(String username, List<String> roles) {
        var now = new Date();
        var validity = new Date(now.getTime() + jwtExpiration);
        var accessToken = getAccessToken(username, roles, now, validity);
        var refreshToken = getRefreshToken(username, roles, now);

        return new TokenResponse(username, true, now, validity, accessToken, refreshToken);
    }


    public TokenResponse refreshToken(String refreshToken) {
        if (refreshToken.contains(BEARER)) refreshToken =
                refreshToken.substring(BEARER.length());

        var verifier = JWT.require(algorithm).build();
        var decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim(ROLES).asList(String.class);
        return createAccessToken(username, roles);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim(ROLES, roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        var validityRefreshToken = new Date(now.getTime() + (jwtExpiration * 3));
        return JWT.create()
                .withClaim(ROLES, roles)
                .withIssuedAt(now)
                .withExpiresAt(validityRefreshToken)
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token) {
        var decodedJWT = decodedToken(token);
        var userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        var alg = Algorithm.HMAC256(jwtSecret.getBytes());
        var verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        var decodedJWT = decodedToken(token);
        try {
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new AuthorizationException("Expired or invalid JWT token!");
        }
    }
}
