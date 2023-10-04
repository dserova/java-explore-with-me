package ru.practicum.explorewithmeservice.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Setter
@Getter
public class ErrorItem {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("status")
    private String status;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private Calendar timestamp;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
