package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.service.UserService;
import su.taskmanager.data.workspace.dto.create.ObjectiveCreateDto;
import su.taskmanager.data.workspace.dto.read.ObjectiveDto;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.ObjectiveRepository;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;
import su.taskmanager.mappers.ObjectiveMapper;

import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Service
public class ObjectiveService {


    private final ObjectiveRepository objectiveRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;

    @Transactional
    @CachePut(value = "users", key = "#user.id")
    public ObjectiveDto createObjective(ObjectiveCreateDto dto, User user) {
        Objective objective = new Objective();
        Long workspaceId = dto.getWorkspaceId();
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Not found workspace with this id: " + workspaceId));
        if (!workspace.getAuthor().getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("Only workspace author allowed to create objectives");
        }
        objective.setName(dto.getName());
        objective.setWorkspace(workspace);
        objective.setDescription(dto.getDescription());
        objective.setTaskHangsOn(dto.getHangsOn());
        objective.setExpiryDate(dto.getExpiryDate());
        objective.setIs_done(false);
        Objective saved = objectiveRepository.save(objective);
        return ObjectiveMapper.toDto(saved);
    }


    @Transactional
    public ObjectiveDto updateObjective(ObjectiveDto dto, Objective objective) {
        if (!dto.getHangsOn().isBlank()) {
            User hangsOn = userService.getUserByUsername(dto.getHangsOn());
            objective.setTaskHangsOn(hangsOn);
        }
        objective.setName(dto.getName());
        objective.setExpiryDate(LocalDateTime.parse(dto.getExpiryDate()));
        objective.setDescription(dto.getDescription());
        objective.setIs_done(dto.getIs_done());

        return ObjectiveMapper.toDto(objectiveRepository.save(objective));
    }

    @Transactional
    public void deleteObjectiveById(Long id) {
        objectiveRepository.deleteById(id);
    }

    @Transactional
    public void delete(Objective objective) {
        objectiveRepository.delete(objective);
    }


    public ObjectiveDto getObjectiveDtoById(Long id) {
        Objective objective = getObjectiveById(id);
        return ObjectiveMapper.toDto(objective);
    }
    public Objective getObjectiveById(Long id) {
        Optional<Objective> objectiveOptional = findObjectiveById(id);
        if (objectiveOptional.isEmpty()) {
            throw new EntityNotFoundException("Not found objective with id: " + id);
        }
        return objectiveOptional.get();
    }

    public Optional<Objective> findObjectiveById(Long id) {
        return objectiveRepository.findById(id);
    }

    public boolean isUserAuthorized(User user, Objective objective) {
        if (user == null || objective == null || objective.getWorkspace() == null || objective.getWorkspace().getAuthor() == null) {
            return false;
        }
        boolean isAuthor = user.getUsername().equals(objective.getWorkspace().getAuthor().getUsername());
        boolean isTaskHangsOn = false;
        if (objective.getTaskHangsOn() != null) {
            isTaskHangsOn = user.getUsername().equals(objective.getTaskHangsOn().getUsername());
        }

        return isAuthor || isTaskHangsOn;
    }

}
