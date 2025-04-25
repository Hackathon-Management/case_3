package ru.hack.Hackathon_Management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hack.Hackathon_Management.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByName(String name);
}
