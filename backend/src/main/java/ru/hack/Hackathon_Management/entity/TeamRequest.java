package ru.hack.Hackathon_Management.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.hack.Hackathon_Management.enums.RequestStatus;

@Entity
@Table(name = "team_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}