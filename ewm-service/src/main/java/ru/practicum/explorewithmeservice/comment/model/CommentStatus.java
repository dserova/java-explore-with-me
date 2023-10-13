package ru.practicum.explorewithmeservice.comment.model;

import java.util.Optional;

public enum CommentStatus {

    DRAFT,

    MODERATION,

    PUBLISHED;

    public static Optional<CommentStatus> from(String stringState) {
        for (CommentStatus state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
