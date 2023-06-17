package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r  where r.name = :name and r.deleteFlag = false ")
    Optional<Role> findByName(String name);

    @Query("select r from Role r  where r.id = :id and r.deleteFlag = false ")
    Optional<Role> findById(Long id);

    @Query("select r from Role r  where r.deleteFlag = false ")
    List<Role> findAll();
}
