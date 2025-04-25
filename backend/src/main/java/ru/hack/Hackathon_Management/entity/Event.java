package ru.hack.Hackathon_Management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;

    @Column(name = "start_event")
    private LocalDateTime startEvent;

    @Column(name = "end_event")
    private LocalDateTime endEvent;

    @OneToMany(mappedBy = "event")
    List<Stage> stages = new ArrayList<>();
}