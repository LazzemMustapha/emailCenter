package com.example.demo.auth;



import com.example.demo.Config.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final LogoutService logoutService;

    private final AuthenticationService service;
    @CrossOrigin(origins = "http://localhost:8088")

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @CrossOrigin(origins = "http://localhost:8088")

    @PostMapping("/registerclient")
    public ResponseEntity<AuthenticationResponse> registerclient(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register1(request));
    }
    @CrossOrigin(origins = "http://localhost:8088")

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @CrossOrigin(origins = "http://localhost:8088")

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
    @CrossOrigin(origins = "http://localhost:8088")

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invoke the logout method from the LogoutService instance
        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Return a response indicating successful logout
        return ResponseEntity.ok("Logout successful");
    }
}