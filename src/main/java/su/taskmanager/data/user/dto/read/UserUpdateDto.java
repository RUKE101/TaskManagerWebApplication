package su.taskmanager.data.user.dto.read;

import lombok.Getter;
import lombok.Setter;
import su.taskmanager.data.workspace.dto.read.WorkspaceDto;


import java.io.Serializable;
import java.util.List;
@Getter
@Setter
public class UserUpdateDto implements Serializable {
    Long userId;
    String username;
    String password;
    List<WorkspaceDto> workspaces;

}
