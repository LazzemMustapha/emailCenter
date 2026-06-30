package com.example.demo.auth;

import com.example.demo.Config.JwtService;
import com.example.demo.entitys.Role;
import com.example.demo.entitys.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf("ADMIN"))
                .build();
        repository.save(user);
        return AuthenticationResponse.builder().build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        // ← Ajouter l'email dans les claims du token
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", user.getEmail());

        var jwtToken = jwtService.generateToken(extraClaims, user); // ← avec email
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userr(user)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
        javax.servlet.http.HttpServletRequest request,
        javax.servlet.http.HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractEmail(refreshToken); // ← extractEmail

        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                // ← email dans les claims du nouveau token aussi
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("email", user.getEmail());

                var accessToken = jwtService.generateToken(extraClaims, user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public AuthenticationResponse register1(RegisterRequest request) {
        boolean existingUser = userRepository.existsByEmail(request.getEmail());
        if (!existingUser) {
            var user = User.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.valueOf("USER"))
                    .build();
            userRepository.save(user);
            return AuthenticationResponse.builder().build();
        } else {
            return AuthenticationResponse.builder()
                    .error(true)
                    .message("User with the given email already exists.")
                    .build();
        }
    }
}