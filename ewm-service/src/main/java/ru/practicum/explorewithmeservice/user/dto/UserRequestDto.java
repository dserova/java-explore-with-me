package ru.practicum.explorewithmeservice.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserRequestDto {
    private Long id;
    @NotBlank
    @Length(min = 2, max = 250, message = "Name is not valid")
    private String name;
    @NotBlank
    @Email(message = "Email is not valid")
    @Length(min = 6, max = 254, message = "Email is not valid")
    private String email;
}
