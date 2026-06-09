package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.PasswordResetToken;
import com.example.demo.entitys.Role;
import com.example.demo.entitys.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserImpl  implements UserService {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {

        return (List<User>) userRepository.findAll();
    }


    @Override
    public User findByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(Integer id) {

        return userRepository.findById(id);
    }

    @Override
    public void removeOne(Integer id) {
        userRepository.deleteById(id);

    }
    @Override
    public Optional<User> findByEmail (String email) {
        return(userRepository.findByEmail(email));

    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
    @Override
    public List<User> searchByPropertyName(String username) {
        return userRepository.searchByPropertyName(username);
    }
    @Override
    public void updateUserRole(Integer userId, Role role) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setRole(role);
        userRepository.save(user);
    }
}
