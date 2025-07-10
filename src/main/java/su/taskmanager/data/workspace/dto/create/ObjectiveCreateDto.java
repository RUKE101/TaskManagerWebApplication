package su.taskmanager.data.workspace.dto.create;

import lombok.Getter;
import lombok.Setter;
import su.taskmanager.data.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class ObjectiveCreateDto {
    private Long workspaceId;
    private LocalDateTime expiryDate;
    private User hangsOn;
    private String description;
    private String name;
}
