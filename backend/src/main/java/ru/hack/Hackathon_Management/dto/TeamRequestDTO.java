package ru.hack.Hackathon_Management.dto;

import lombok.Getter;
import lombok.Setter;
import ru.hack.Hackathon_Management.enums.RequestStatus;

@Getter
@Setter
public class TeamRequestDTO {
    Long userId;
    Long teamId;
    RequestStatus status;
}
