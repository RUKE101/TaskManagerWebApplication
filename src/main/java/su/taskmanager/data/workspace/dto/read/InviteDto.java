package su.taskmanager.data.workspace.dto.read;

import lombok.Getter;
import lombok.Setter;
import su.taskmanager.data.workspace.InvitationStatus;

import java.util.UUID;
@Getter
@Setter
public class InviteDto {
    private Long id;
    private Long senderId;
    private UUID uuid;
    private InvitationStatus status;

}
