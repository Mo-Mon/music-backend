package com.example.musicbackend.payload.request;

import com.example.musicbackend.validate.annotation.EmailValidate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotNull
    @NotEmpty
    @EmailValidate
    private String email;

    @NotNull
    @NotEmpty
    private String password;
}
