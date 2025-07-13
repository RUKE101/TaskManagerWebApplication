package su.taskmanager.mappers;

import su.taskmanager.data.workspace.dto.read.WorkspaceDto;
import su.taskmanager.data.workspace.entity.Workspace;

import java.util.List;
import java.util.stream.Collectors;

public class WorkspaceMapper {
    public static List<WorkspaceDto> toDto(List<Workspace> workspaces) {
        if (workspaces == null) {
            return null;
        }
        return workspaces.stream()
                .map(WorkspaceMapper::toDto)
                .collect(Collectors.toList());

    }
    public static WorkspaceDto toDto(Workspace workspace) {
        WorkspaceDto dto = new WorkspaceDto();
        dto.setId(workspace.getId());
        dto.setName(workspace.getNameOfWorkspace());
        dto.setObjectives(workspace.getObjectives());
        dto.setDescription(workspace.getDescription());
        dto.setUsers(UserMapper.toWorkspaceDtos(workspace.getUsers()));

        dto.setInvites(InviteMapper.toDto(workspace.getInvites()));
        dto.setAuthorId(workspace.getAuthor().getId());
        return dto;
    }

}
