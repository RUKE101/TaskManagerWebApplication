package su.taskmanager.data.workspace.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectiveCreateDto {
    private Long ProjectId;
    private String description;
    private String nameOfProject;
}
