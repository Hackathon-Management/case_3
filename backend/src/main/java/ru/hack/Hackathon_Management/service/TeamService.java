package ru.hack.Hackathon_Management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hack.Hackathon_Management.dto.TeamDTO;
import ru.hack.Hackathon_Management.dto.TeamRequestDTO;
import ru.hack.Hackathon_Management.entity.*;
import ru.hack.Hackathon_Management.enums.CommandRole;
import ru.hack.Hackathon_Management.enums.RequestStatus;
import ru.hack.Hackathon_Management.exception.*;
import ru.hack.Hackathon_Management.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamRequestRepository teamRequestRepository;
    private final CaseRepository caseRepository;

    // Создание новой команды (автоматически делает создателя капитаном)
    @Transactional
    public Team createTeam(TeamDTO teamDTO, Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (creator.getTeam() != null) {
            throw new RuntimeException("User already in a team");
        }

        Case hackathonCase = caseRepository.findById(teamDTO.getCaseId()).orElse(null);

        Team team = new Team();
        team.setName(teamDTO.getName());
        team.setCaseObj(hackathonCase);
        team.setUsers(new ArrayList<>());
        team.setTeamRequests(new ArrayList<>());

        creator.setCommandRole(CommandRole.LEADER);
        creator.setTeam(team);
        team.getUsers().add(creator);

        return teamRepository.save(team);
    }

    // Подача заявки в команду
    @Transactional
    public TeamRequest applyToTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTeam() != null) {
            throw new RuntimeException("User already in a team");
        }

        if (teamRequestRepository.existsByTeamAndUserAndStatus(
                team, user, RequestStatus.PENDING)) {
            throw new RuntimeException("Request already exists");
        }

        TeamRequest request = new TeamRequest();
        request.setUser(user);
        request.setTeam(team);
        request.setStatus(RequestStatus.PENDING);

        return teamRequestRepository.save(request);
    }

    // Принятие заявки в команду (только для капитана)
    @Transactional
    public Team acceptRequest(Long requestId, Long captainId) {
        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!captain.isLeader() || !captain.getTeam().equals(request.getTeam())) {
            throw new RuntimeException("Only team captain can accept requests");
        }

        User user = request.getUser();
        Team team = request.getTeam();

        if (team.getUsers().size() == 6) { // Пример ограничения на размер команды
            throw new RuntimeException("Team is full");
        }

        user.setTeam(team);
        user.setCommandRole(CommandRole.DEFAULT);
        team.getUsers().add(user);

        request.setStatus(RequestStatus.ACCEPTED);
        teamRequestRepository.save(request);

        return teamRepository.save(team);
    }

    // Отклонение заявки
    @Transactional
    public void rejectRequest(Long requestId, Long captainId) {
        TeamRequest request = teamRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!captain.isLeader() || !captain.getTeam().equals(request.getTeam())) {
            throw new RuntimeException("Only team captain can reject requests");
        }

        request.setStatus(RequestStatus.REJECTED);
        teamRequestRepository.save(request);
    }

    // Назначение нового капитана
    @Transactional
    public Team changeCaptain(Long teamId, Long newCaptainId, Long currentCaptainId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User currentCaptain = userRepository.findById(currentCaptainId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User newCaptain = userRepository.findById(newCaptainId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!currentCaptain.isLeader() || !currentCaptain.getTeam().equals(team)) {
            throw new RuntimeException("Only current captain can transfer captaincy");
        }

        if (!team.getUsers().contains(newCaptain)) {
            throw new RuntimeException("New captain must be a team member");
        }

        currentCaptain.setCommandRole(CommandRole.DEFAULT);
        newCaptain.setCommandRole(CommandRole.LEADER);

        userRepository.save(currentCaptain);
        userRepository.save(newCaptain);

        return team;
    }

    // Удаление участника из команды (для капитана)
    @Transactional
    public Team removeMember(Long teamId, Long memberId, Long captainId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!captain.isLeader() || !captain.getTeam().equals(team)) {
            throw new RuntimeException("Only captain can remove members");
        }

        if (member.isLeader()) {
            throw new RuntimeException("Cannot remove captain this way");
        }

        team.getUsers().remove(member);
        member.setTeam(null);
        member.setCommandRole(null);

        userRepository.save(member);
        return teamRepository.save(team);
    }

    // Получение команды по ID
    public TeamDTO getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return toTeamDto(team);
    }



    // Получение всех команд
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream().map(this::toTeamDto).collect(Collectors.toList());
    }

    // Поиск команд по имени
    public List<TeamDTO> searchTeamsByName(String name) {
        return teamRepository.findByName(name).stream().map(this::toTeamDto).collect(Collectors.toList());
    }

    // Получение всех заявок в команду
    public List<TeamRequestDTO> getTeamRequests(Long teamId, Long captainId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        User captain = userRepository.findById(captainId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!captain.isLeader() || !captain.getTeam().equals(team)) {
            throw new RuntimeException("Only captain can view requests");
        }

        return teamRequestRepository.findByTeamAndStatus(team, RequestStatus.PENDING).stream().map(this::toTeamRequestDto).collect(Collectors.toList());
    }

    private TeamRequestDTO toTeamRequestDto(TeamRequest teamRequest) {
        TeamRequestDTO dto = new TeamRequestDTO();
        dto.setUserId(teamRequest.getUser().getId());
        dto.setTeamId(teamRequest.getTeam().getId());
        return dto;
    }

    private TeamDTO toTeamDto(Team team) {
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setName(team.getName());
        teamDTO.setCaseId(team.getCaseObj().getId());
        return teamDTO;
    }
}