package com.example.demo.auth;



import com.example.demo.Config.LogoutService;
import com.example.demo.entitys.Account;
import com.example.demo.entitys.User;
import com.example.demo.service.AccountService;
import com.example.demo.service.ConnectedUserService;
import com.example.demo.service.EmailService1;
import com.example.demo.service.UserService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserService userService;
       @Autowired
    private AccountService accountService;
    @Autowired
    private EmailService1 emailService;
   @Autowired
    private ConnectedUserService connectedUserService;
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
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        // ✅ Ton code existant
        AuthenticationResponse response = service.authenticate(request);

        // ✅ Après login réussi : marquer connecté + fetch immédiat
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            Integer userId = userOpt.get().getId();

            // Marquer le user comme connecté
            connectedUserService.userConnected(userId);

            // Fetch immédiat des emails de ses comptes
            List<Account> comptes = accountService.getAccountsByUserId(userId);
            for (Account compte : comptes) {
                try {
                    emailService.fetchAndSaveEmails(compte.getId());
                } catch (Exception e) {
                    System.out.println("❌ Fetch échoué compte " + compte.getId() + " : " + e.getMessage());
                }
            }
        }

        return ResponseEntity.ok(response);
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

        // ✅ Récupérer le user connecté avant de le déconnecter
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            connectedUserService.userDisconnected(userOpt.get().getId());
        }

        logoutService.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("Logout successful");
    }
}