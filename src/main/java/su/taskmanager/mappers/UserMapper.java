package su.taskmanager.mappers;

import su.taskmanager.data.user.dto.read.UserGetDto;
import su.taskmanager.data.user.dto.read.UserWorkspaceDto;
import su.taskmanager.data.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {


    public static List<UserWorkspaceDto> toWorkspaceDtos(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(su.taskmanager.mappers.UserMapper::toWorkspaceDto)
                .collect(Collectors.toList());
    }

    public static UserWorkspaceDto toWorkspaceDto(User user) {
        UserWorkspaceDto dto = new UserWorkspaceDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }

    public static UserGetDto toDto(User user) {
        UserGetDto dto = new UserGetDto();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        if (user.getWorkspaces() != null) {
            dto.setWorkspaces(WorkspaceMapper.toDto(user.getWorkspaces()));
        }
        return dto;
    }
}
