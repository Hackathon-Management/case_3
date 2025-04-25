package ru.hack.Hackathon_Management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hack.Hackathon_Management.dto.TeamDTO;
import ru.hack.Hackathon_Management.dto.TeamRequestDTO;
import ru.hack.Hackathon_Management.entity.Team;
import ru.hack.Hackathon_Management.entity.TeamRequest;
import ru.hack.Hackathon_Management.service.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    // Создание новой команды (userId в теле запроса)
    @PostMapping("{id}")
    public ResponseEntity<?> createTeam(@RequestBody TeamDTO teamDTO, @PathVariable("id") Long creatorId) {
        teamService.createTeam(teamDTO, creatorId);
        return ResponseEntity.ok().build();
    }

    // Подача заявки в команду (userId в параметрах)
    @PostMapping("/{teamId}/apply")
    public ResponseEntity<?> applyToTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId) {
        teamService.applyToTeam(teamId, userId);
        return ResponseEntity.ok().build();
    }

    // Принятие заявки в команду (captainId в параметрах)
    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<Team> acceptRequest(
            @PathVariable Long requestId,
            @RequestParam Long captainId) {
        teamService.acceptRequest(requestId, captainId);
        return ResponseEntity.ok().build();
    }

    // Отклонение заявки (captainId в параметрах)
    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable Long requestId,
            @RequestParam Long captainId) {
        teamService.rejectRequest(requestId, captainId);
        return ResponseEntity.noContent().build();
    }

    // Назначение нового капитана (currentCaptainId в параметрах)
    @PatchMapping("/{teamId}/captain")
    public ResponseEntity<?> changeCaptain(
            @PathVariable Long teamId,
            @RequestParam Long newCaptainId,
            @RequestParam Long currentCaptainId) {
        teamService.changeCaptain(teamId, newCaptainId, currentCaptainId);
        return ResponseEntity.ok().build();
    }

    // Удаление участника из команды (captainId в параметрах)
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<?> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId,
            @RequestParam Long captainId) {
        teamService.removeMember(teamId, memberId, captainId);
        return ResponseEntity.ok().build();
    }

    // Получение информации о команде
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    // Получение всех команд
    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // Поиск команд по имени
    @GetMapping("/search")
    public ResponseEntity<List<TeamDTO>> searchTeams(
            @RequestParam String name) {
        List<TeamDTO> teamsDto = teamService.searchTeamsByName(name);
        return ResponseEntity.ok(teamsDto);
    }

    // Получение заявок в команду (captainId в параметрах)
    @GetMapping("/{teamId}/requests")
    public ResponseEntity<List<TeamRequestDTO>> getTeamRequests(
            @PathVariable Long teamId,
            @RequestParam Long captainId) {
        List<TeamRequestDTO> requestsDTO = teamService.getTeamRequests(teamId, captainId);
        return ResponseEntity.ok(requestsDTO);
    }
}