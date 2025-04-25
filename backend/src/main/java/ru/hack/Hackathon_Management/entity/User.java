package ru.hack.Hackathon_Management.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.hack.Hackathon_Management.enums.CommandRole;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String skills;

    @Column(unique = true)
    private String email;

    private String password;
    private String role;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "user")
    private List<TeamRequest> teamRequests;

    @Enumerated(EnumType.STRING)
    CommandRole commandRole;

    public boolean isLeader() {
        return this.commandRole == CommandRole.LEADER;
    }
}