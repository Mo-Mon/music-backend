package com.example.musicbackend.repository;

import com.example.musicbackend.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.deleteFlag = false and u.id = :id and t.isRefreshToken = false and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidAccessTokenByUser(Long id);

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.deleteFlag = false and u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Long id);

    @Query("select t from Token t  where t.token = :token and t.deleteFlag = false ")
    Optional<Token> findByToken(String token);

    @Query("select t from Token t  where t.id = :id and t.deleteFlag = false ")
    Optional<Token> findById(Long id);

    @Query("select t from Token t  where t.deleteFlag = false ")
    List<Token> findAll();
}
