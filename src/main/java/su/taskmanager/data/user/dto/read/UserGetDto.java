package su.taskmanager.data.user.dto.read;

import lombok.Getter;
import lombok.Setter;
import su.taskmanager.data.workspace.dto.read.WorkspaceDto;


import java.util.List;
@Getter
@Setter
public class UserGetDto {
    Long userId;
    String username;
    List<WorkspaceDto> workspaces;

}
