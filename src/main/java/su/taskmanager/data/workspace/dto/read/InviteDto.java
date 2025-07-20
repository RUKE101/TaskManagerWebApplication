package su.taskmanager.data.workspace.dto.read;

import lombok.Getter;
import lombok.Setter;
import su.taskmanager.data.workspace.InvitationStatus;

import java.io.Serializable;
import java.util.UUID;
@Getter
@Setter

public class InviteDto implements Serializable {
    private Long id;
    private Long senderId;
    private UUID uuid;
    private InvitationStatus status;

}
