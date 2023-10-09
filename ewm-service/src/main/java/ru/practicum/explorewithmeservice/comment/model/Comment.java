package ru.practicum.explorewithmeservice.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.user.model.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"comment_id"})})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;
    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User author;
    @Column(name = "created")
    private Calendar created = Calendar.getInstance();
    @Column(name = "edited")
    private Calendar updated;
    @Column(name = "status")
    private CommentStatus status;
    @JoinColumn(name = "comment_id")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Comment> replies;

    @PreUpdate
    protected void onUpdate() {
        this.updated = Calendar.getInstance();
    }
}