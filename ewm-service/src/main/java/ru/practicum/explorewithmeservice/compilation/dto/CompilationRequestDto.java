package ru.practicum.explorewithmeservice.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class CompilationRequestDto {
    private List<Long> events;
    private Long id;
    private Boolean pinned = false;
    @NotBlank
    @Length(max = 50, message = "Title is not valid")
    private String title;
}
