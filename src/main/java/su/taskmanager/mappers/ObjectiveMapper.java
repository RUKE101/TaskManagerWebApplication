package su.taskmanager.mappers;

import su.taskmanager.data.workspace.dto.read.ObjectiveDto;
import su.taskmanager.data.workspace.entity.Objective;

public class ObjectiveMapper {
    public static ObjectiveDto toDto(Objective objective) {
        ObjectiveDto dto = new ObjectiveDto();
        dto.setDescription(objective.getDescription());
        dto.setName(objective.getName());
        dto.setExpiryDate(objective.getExpiryDate());
        if (objective.getTaskHangsOn() != null) {
            dto.setHangsOn(objective.getTaskHangsOn().getUsername());
        }
        dto.setWorkspaceId(objective.getWorkspace().getId());
        dto.setIs_done(objective.getIs_done());
        return dto;
    }
}
