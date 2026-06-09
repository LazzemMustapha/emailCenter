package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entitys.PasswordResetToken;
import com.example.demo.entitys.Role;
import com.example.demo.entitys.User;

public interface UserService {
	//User findByUsername(String username);


    void createPasswordResetTokenForUser(final User user, final String token);
    Optional<User> findByEmail (String email);
    PasswordResetToken getPasswordResetToken(final String token);
    User save(User user);
    boolean existsByEmail(String email);
    User findByResetToken(String resetToken);

    void saveUser(User user);
    List<User> findAll() ;






    Optional<User> findById(Integer id);

    void removeOne(Integer id);
    public List<User> searchByPropertyName(String username);
    public void updateUserRole(Integer userId, Role role);
}
