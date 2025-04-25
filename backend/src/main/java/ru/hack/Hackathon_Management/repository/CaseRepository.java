package ru.hack.Hackathon_Management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hack.Hackathon_Management.entity.Case;

public interface CaseRepository extends JpaRepository<Case, Long> {
}
