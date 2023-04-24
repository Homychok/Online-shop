package com.example.diplomproject.repository;

import com.example.diplomproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//    Optional<User> findByUsernameIgnoreCase(String username);
//    @Modifying
//    @Query(value = "UPDATE users u SET password = ?1 WHERE u.username = ?2", nativeQuery = true)
//    void updatePassword(String newPassword, String username);

//    // Получить информацию об авторизованном пользователе
    User findByUsernameIgnoreCase(String username);

    // Проверка наличия логина
    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
