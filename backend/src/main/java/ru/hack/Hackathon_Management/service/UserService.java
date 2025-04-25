package ru.hack.Hackathon_Management.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hack.Hackathon_Management.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


}
