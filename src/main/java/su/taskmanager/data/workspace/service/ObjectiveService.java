package su.taskmanager.data.workspace.service;

import lombok.Builder;
import org.springframework.stereotype.Service;
import su.taskmanager.data.user.service.UserService;
import su.taskmanager.data.workspace.dto.create.ObjectiveCreateDto;
import su.taskmanager.data.workspace.dto.read.ObjectiveDto;
import su.taskmanager.data.workspace.entity.Objective;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.ObjectiveRepository;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;
import su.taskmanager.mappers.ObjectiveMapper;

import java.util.Optional;

@Builder
@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;

    public ObjectiveDto createObjective(ObjectiveCreateDto dto) {
        Objective objective = new Objective();
        Long workspaceId = dto.getWorkspaceId();
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Not found workspace with this id: " + workspaceId));

        objective.setName(dto.getName());
        objective.setWorkspace(workspace);
        objective.setDescription(dto.getDescription());
        objective.setTaskHangsOn(dto.getHangsOn());
        objective.setExpiryDate(dto.getExpiryDate());
        objective.setIs_done(false);
        Objective saved = objectiveRepository.save(objective);
        return ObjectiveMapper.toDto(saved);
    }



    public Optional<Objective> findObjectiveById(Long id) {
        return objectiveRepository.findById(id);
    }

    public Objective updateObjective(ObjectiveDto dto, Objective objective) {
        objective.setName(dto.getName());
        objective.setExpiryDate(dto.getExpiryDate());
        objective.setDescription(dto.getDescription());
        objective.setIs_done(dto.getIs_done());
        return objectiveRepository.save(objective);
    }

    public void deleteObjectiveById(Long id) {
        objectiveRepository.deleteById(id);
    }


}
