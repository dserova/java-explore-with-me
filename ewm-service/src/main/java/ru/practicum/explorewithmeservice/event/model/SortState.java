package ru.practicum.explorewithmeservice.event.model;

import java.util.Optional;

public enum SortState {
    EVENT_DATE,

    VIEWS;

    public static Optional<SortState> from(String stringState) {
        for (SortState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
