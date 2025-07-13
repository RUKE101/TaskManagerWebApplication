package su.taskmanager.data.workspace.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceCreateDto {
    private Long authorId;
    private String nameOfWorkspace;
    private String description;
}
