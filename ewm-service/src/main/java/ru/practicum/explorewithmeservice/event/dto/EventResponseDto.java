package ru.practicum.explorewithmeservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithmeservice.category.model.Category;
import ru.practicum.explorewithmeservice.comment.dto.CommentResponseShortDto;
import ru.practicum.explorewithmeservice.event.model.EventState;
import ru.practicum.explorewithmeservice.location.model.Location;
import ru.practicum.explorewithmeservice.user.model.User;

import java.util.Calendar;
import java.util.List;

@Data
@NoArgsConstructor
public class EventResponseDto {
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar eventDate;
    private Long id;
    private User initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
    private List<CommentResponseShortDto> comments;
}
