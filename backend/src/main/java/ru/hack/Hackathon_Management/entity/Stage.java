package ru.hack.Hackathon_Management.entity;

import jakarta.persistence.*;
import ru.hack.Hackathon_Management.entity.Event;

import java.time.LocalDateTime;

@Entity
@Table(name = "stage")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private LocalDateTime startStage;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
