package ru.practicum.explorewithmeservice.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {
    private Long id;
    @NotBlank
    @Length(max = 50, message = "Name is not valid")
    private String name;
}
