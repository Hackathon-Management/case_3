package ru.hack.Hackathon_Management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Case caseObj;

    @OneToOne
    @JoinColumn(name = "result_id", unique = true)
    private Result result;

    @OneToMany(mappedBy = "team")
    private List<User> users;

    @OneToMany(mappedBy = "team")
    private List<TeamRequest> teamRequests;
}