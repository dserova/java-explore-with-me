package ru.practicum.explorewithmestatsserver.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorItem {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    @JsonProperty("error")
    private String message;

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
