package ru.hack.Hackathon_Management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hack.Hackathon_Management.entity.Team;
import ru.hack.Hackathon_Management.entity.TeamRequest;
import ru.hack.Hackathon_Management.entity.User;
import ru.hack.Hackathon_Management.enums.RequestStatus;

import java.util.List;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {

    boolean existsByTeamAndUserAndStatus(Team team, User user, RequestStatus status);

    List<TeamRequest> findByTeamAndStatus(Team team, RequestStatus status);
}
