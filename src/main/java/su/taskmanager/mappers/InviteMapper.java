package su.taskmanager.mappers;

import su.taskmanager.data.workspace.dto.read.InviteDto;
import su.taskmanager.data.workspace.entity.Invite;

import java.util.List;
import java.util.stream.Collectors;

public class InviteMapper {

    public static List<InviteDto> toDto(List<Invite> invites) {
        if (invites == null) {
            return null;
        }
        return invites.stream()
                .map(InviteMapper::toDto)
                .collect(Collectors.toList());

    }

    public static InviteDto toDto(Invite invite) {
        InviteDto dto = new InviteDto();
        dto.setId(invite.getId());
        dto.setSenderId(invite.getSender().getId());
        dto.setUuid(invite.getUuid());
        dto.setStatus(invite.getStatus());
        return dto;
    }
}
