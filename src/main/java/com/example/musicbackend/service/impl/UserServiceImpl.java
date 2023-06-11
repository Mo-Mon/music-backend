package com.example.musicbackend.service.impl;

import com.example.musicbackend.entity.User;
import com.example.musicbackend.exception.custom.NotFoundItemException;
import com.example.musicbackend.repository.UserRepository;
import com.example.musicbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundItemException("không tìm thấy current user có email là:" + email));
    }

}
