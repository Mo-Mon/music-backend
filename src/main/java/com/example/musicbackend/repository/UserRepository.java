package com.example.musicbackend.repository;

import com.example.musicbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u  where u.email = :email and u.deleteFlag = false ")
    Optional<User> findByEmail(String email);

    @Query("select u from User u  where u.id = :id and u.deleteFlag = false ")
    Optional<User> findById(Long id);

    @Query("select u from User u  where u.deleteFlag = false ")
    List<User> findAll();

}
