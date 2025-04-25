package ru.hack.Hackathon_Management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hack.Hackathon_Management.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
