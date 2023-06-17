package com.example.musicbackend.payload.request;

import com.example.musicbackend.validate.annotation.EmailValidate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @EmailValidate
    private String email;

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String firstName;

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String lastName;

    @NotNull(message = "không được phép null")
    @NotEmpty(message = "không được phép empty")
    @Length(max = 255, message = "độ dài tôi đa là 255 kí tự")
    private String password;

    @NotNull(message = "không được phép null")
    private List<String> roles;
}
