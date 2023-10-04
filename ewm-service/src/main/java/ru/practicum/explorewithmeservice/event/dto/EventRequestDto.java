package ru.practicum.explorewithmeservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explorewithmeservice.event.model.EventState;
import ru.practicum.explorewithmeservice.location.model.Location;
import ru.practicum.explorewithmeservice.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventRequestDto {
    @NotBlank
    @Length(min = 20, max = 2000, message = "Annotation is not valid")
    private String annotation;
    private Long category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar createdOn;
    @NotBlank
    @Length(min = 20, max = 7000, message = "Description is not valid")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private Calendar eventDate;
    private Long id;
    private User initiator;
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private String publishedOn;
    private Boolean requestModeration = true;
    private EventState state;
    @Length(min = 3, max = 120, message = "Title is not valid")
    private String title;
    private Long views;
    private String stateAction;
}
