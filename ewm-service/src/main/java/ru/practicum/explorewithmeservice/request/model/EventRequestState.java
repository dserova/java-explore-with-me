package ru.practicum.explorewithmeservice.request.model;

import java.util.Optional;

public enum EventRequestState {
    PENDING,//

    CONFIRMED,

    CANCELED,//

    REJECTED;

    public static Optional<EventRequestState> from(String stringState) {
        for (EventRequestState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
