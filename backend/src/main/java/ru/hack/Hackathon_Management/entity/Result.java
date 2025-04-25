package ru.hack.Hackathon_Management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_url")
    private String projectUrl;

    @Column(name = "pres_url")
    private String presUrl;

    @Column(name = "team_id", unique = true)
    private Integer teamId;

    @OneToOne(mappedBy = "result")
    private Team team;
}