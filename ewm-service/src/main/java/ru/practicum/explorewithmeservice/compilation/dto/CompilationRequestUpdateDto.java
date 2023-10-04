package ru.practicum.explorewithmeservice.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationRequestUpdateDto {
    private List<Long> events;
    private Long id;
    private Boolean pinned = false;
    @Length(max = 50, message = "Title is not valid")
    private String title;
}
