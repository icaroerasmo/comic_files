package com.comic.files.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DUser {

    private Long id;

    @NotNull(message = "The name cannot be null")
    @Size(max = 255, message = "The maximum length of the name is 255 characters")
    private String name;

    @NotNull(message = "The e-mail cannot be null")
    @Email(message = "The e-mail is invalid")
    @Size(max = 100, message = "The maximum length of the e-mail is 100 characters")
    private String email;

    @NotNull(message = "The username cannot be null")
    @Size(max = 50, message = "The maximum length of the username is 50 characters")
    private String username;

    @NotNull(message = "The password cannot be null")
    @Size(max = 50, message = "The maximum length of the password is 50 characters")
    private String password;

    @NotNull(message = "The birthdate cannot be null")
    private LocalDate birthDate;
}
