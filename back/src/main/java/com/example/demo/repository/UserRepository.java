package com.example.demo.repository;




import com.example.demo.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
   // User findByUsername(String username) ;
   //Optional <User> findByEmail(String email) ;
    Optional <User> findById(Integer id);
    User save(Optional<User>user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
   // PasswordResetToken getPasswordResetToken(final String token);
    User findByResetToken(String resetToken);
    @Query("SELECT m FROM User m WHERE m.username LIKE %:username%")
    public List<User> searchByPropertyName(@Param("username") String username);
}
