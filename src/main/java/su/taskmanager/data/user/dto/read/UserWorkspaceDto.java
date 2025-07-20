package su.taskmanager.data.user.dto.read;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserWorkspaceDto implements Serializable {
    private Long id;
    private String username;
}
