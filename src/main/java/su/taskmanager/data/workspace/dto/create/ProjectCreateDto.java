package su.taskmanager.data.workspace.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectCreateDto {
    private Long workspaceId;
    private String nameOfProject;
    private String description;

}
