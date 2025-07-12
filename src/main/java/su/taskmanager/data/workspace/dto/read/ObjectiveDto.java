package su.taskmanager.data.workspace.dto.read;

import lombok.Getter;
import lombok.Setter;
import su.taskmanager.data.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class ObjectiveDto {
    private Long id;
    private String name;
    private String description;
    private String hangsOn;
    private Boolean is_done;
    private LocalDateTime expiryDate;
    private Long workspaceId;
}