package su.taskmanager.mappers;

import su.taskmanager.data.workspace.dto.read.ObjectiveDto;
import su.taskmanager.data.workspace.entity.Objective;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectiveMapper {

    public static ObjectiveDto toDto(Objective objective) {
        ObjectiveDto dto = new ObjectiveDto();
        dto.setDescription(objective.getDescription());
        dto.setName(objective.getName());



        dto.setExpiryDate((objective.getExpiryDate()));

        if (objective.getTaskHangsOn() != null) {
            dto.setHangsOn(objective.getTaskHangsOn().getUsername());
        }
        dto.setWorkspaceId(objective.getWorkspace().getId());
        dto.setIs_done(objective.getIs_done());
        return dto;
    }

    public static List<ObjectiveDto> toDtos(List<Objective> objectives) {
        if (objectives == null || objectives.isEmpty()) {
            return Collections.emptyList();
        }
        return objectives.stream()
                .map(ObjectiveMapper::toDto)
                .collect(Collectors.toList());
    }
}
