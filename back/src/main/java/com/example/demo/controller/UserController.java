package com.example.demo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.auth.passwordRequest;
import com.example.demo.entitys.ResetPasswordRequest;
import com.example.demo.entitys.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import com.example.demo.utility.MailConstructor;
import com.example.demo.utility.SecurityUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.demo.entitys.Role;

@RestController

@RequestMapping("/api")
@RequiredArgsConstructor
public class  UserController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    private UserRepository userRepository; // Assurez-vous que UserRepository est correctement configuré
    
    @Autowired
    

@CrossOrigin(origins = "${app.cors.origin}")

    @GetMapping(value="/ListUser")

    public List<User> users() {
        List<User> users = userService.findAll();
        return users;

    }
@CrossOrigin(origins = "${app.cors.origin}")

    @GetMapping("/UserInfo")
    public User getUserInfo(@RequestParam("id") Integer id) {
        User user1 = new User();
        java.util.Optional<User> user = userService.findById(id);
  user1.setId(user.get().getId());
  
  user1.setEmail(user.get().getEmail());
  
  user1.setUsername(user.get().getUsername());
  user1.setRole(user.get().getRole());
  user1.setPassword(user.get().getPassword());
  user1.setAccounts(user.get().getAccounts());
        return user1;
    }
    @CrossOrigin(origins = "${app.cors.origin}")

    @GetMapping("/email/{e}")
    public Optional<User> findByEmail(@PathVariable("e") String email) {

        return userService.findByEmail(email);
    }
    @CrossOrigin(origins = "${app.cors.origin}")

    @PostMapping("/AddUser")
    public User addUser(@RequestBody User user) {
        // Encrypt the password
        String encryptedPassword = passwordEncoder.encode(user.getPassword());

        // Create a new user object and set its properties
        User nouvelleUser = new User();
        nouvelleUser.setPassword(encryptedPassword);
        nouvelleUser.setEmail(user.getEmail());
        nouvelleUser.setUsername(user.getUsername());
        nouvelleUser.setRole(user.getRole());

        // Save the new user in the database
        nouvelleUser = userService.save(nouvelleUser);

        return nouvelleUser;
    }
@CrossOrigin(origins = "${app.cors.origin}")
    @DeleteMapping("/removeUser/{id}")
    public ResponseEntity<String> removeOne(@PathVariable("id") Integer id) {
        userService.removeOne(id);
        return ResponseEntity.ok("User removed successfully");
    }
@CrossOrigin(origins = "${app.cors.origin}")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();

        // Check if the user exists with the given email
        Optional<User> optionalUser = userService.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body("User not found with the given email.");
        }

        // If the user is present, get the User object from the Optional
        User user = optionalUser.get();

        // Generate a password reset token and save it to the user's record in the database
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userService.saveUser(user);

        // Send an email to the user with the password reset link containing the resetToken
        // Here, you can use a service to send the email, like JavaMailSender, or use a third-party service.
        emailService.sendPasswordResetEmail(email, resetToken);

        return ResponseEntity.ok("Password reset link sent to your email.");
    }
@CrossOrigin(origins = "${app.cors.origin}")
    @RequestMapping("/forget_password")
    public String forgetPassword(HttpServletRequest request, @RequestBody String email, Model model) {

        model.addAttribute("classActiveForgetPassword", true);

        Optional<User> user1 = userService.findByEmail(email);
        if (user1 .isPresent()) {
        User user = user1.get();
        if (user == null) {
            model.addAttribute("emailNotExist", true);
            return "forget_password";
        }

        String password = SecurityUtility.randomPassword();

        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
                password);

        mailSender.send(newEmail);

        model.addAttribute("forgetPasswordEmailSent", "true");

        return "forget_password";
    }return "forget_password";
    }





   @CrossOrigin(origins = "${app.cors.origin}")


    @GetMapping("/api/check-email-uniqueness")

    public ResponseEntity<Boolean> checkEmailUnique(@RequestParam String email) {
        boolean isUnique = userRepository.findByEmail(email) == null;
        return ResponseEntity.ok(isUnique);
    }



@CrossOrigin(origins = "${app.cors.origin}")


    @PutMapping("/updatepassword/{id}")
    public User updatePassword(@PathVariable("id") Integer id, @RequestBody passwordRequest maisonDetails) {
        Optional<User> optionalMaison = userService.findById(id);

        if (optionalMaison.isPresent()) {
            User maison = optionalMaison.get();

            // Mettre à jour les champs de la maison avec les détails reçus
            maison.setPassword(passwordEncoder.encode(maisonDetails.getPassword()));


            // Enregistrer la maison mise à jour dans la base de données
            maison = userService.save(maison);

            return maison;
        } else {
            // Ou renvoyer une réponse appropriée en cas de maison non trouvée
            return null;
        }
    }
    @CrossOrigin(origins = "${app.cors.origin}")
    @GetMapping("/user/searchh")
    public ResponseEntity<List<User>> searchByPropertyName(@RequestParam String username) {
        List<User> maisons = userService.searchByPropertyName(username);
        return ResponseEntity.ok(maisons);
    }
    @CrossOrigin(origins = "${app.cors.origin}")
    
    @PutMapping("/modifieruser/{id}")
    public User updateRole(@PathVariable("id") Integer id, @RequestParam("role") Role role) {
        Optional<User> optionalUser = userService.findById(id);
    
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
    
            // Mettre à jour uniquement le rôle de l'utilisateur
            user.setRole(role);
    
            // Enregistrer l'utilisateur mis à jour dans la base de données
            user = userService.save(user);
    
            return user;
        } else {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            return null;
        }
    }
    
}
