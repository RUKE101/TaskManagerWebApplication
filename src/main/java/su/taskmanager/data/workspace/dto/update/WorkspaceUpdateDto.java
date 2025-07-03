package su.taskmanager.data.workspace.dto.update;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceUpdateDto {
    private Long userId;
    private String workspaceName;
    private String nameOfWorkspace;
}
