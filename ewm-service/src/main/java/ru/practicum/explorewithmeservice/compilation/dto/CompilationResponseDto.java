package ru.practicum.explorewithmeservice.compilation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithmeservice.event.model.Event;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationResponseDto {
    private List<Event> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
