package su.taskmanager.data.workspace.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteCreateDto {
    private Long workspace_id;
    private Long sender_id;
}
