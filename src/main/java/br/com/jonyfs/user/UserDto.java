package br.com.jonyfs.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

    @NotEmpty
    String firstName;

    @NotEmpty
    String lastName;

    @Email
    String email;

    @NotEmpty
    String password;
}
