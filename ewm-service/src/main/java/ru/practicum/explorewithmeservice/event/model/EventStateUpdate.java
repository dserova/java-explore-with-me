package ru.practicum.explorewithmeservice.event.model;

import java.util.Optional;

public enum EventStateUpdate {
    PUBLISH_EVENT,

    REJECT_EVENT,

    PENDING_EVENT,//

    CANCEL_REVIEW;//

//    "SEND_TO_REVIEW"
//    "CANCEL_REVIEW"

    public static Optional<EventStateUpdate> from(String stringState) {
        for (EventStateUpdate state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
