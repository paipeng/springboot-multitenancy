package com.paipeng.saas.repository;

import com.paipeng.saas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    @Query("SELECT u FROM User u WHERE u.token is not null")
    List<User> findUsersWithToken();

    User findByToken(String token);
}
