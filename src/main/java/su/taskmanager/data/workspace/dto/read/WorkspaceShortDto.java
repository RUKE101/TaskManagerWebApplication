package su.taskmanager.data.workspace.dto.read;

import lombok.Getter;
import lombok.Setter;
import su.taskmanager.data.user.dto.read.UserWorkspaceDto;

import java.util.List;

@Getter
@Setter
public class WorkspaceShortDto {
    private Long id;
    private String name;
    private String description;
    private List<UserWorkspaceDto> users;
    private List<ObjectiveDto> objectives;
}
