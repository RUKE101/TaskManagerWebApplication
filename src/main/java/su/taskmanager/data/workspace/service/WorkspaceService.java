package su.taskmanager.data.workspace.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.taskmanager.data.user.dto.read.UserDto;
import su.taskmanager.data.user.entity.User;
import su.taskmanager.data.user.service.UserService;
import su.taskmanager.data.workspace.dto.read.WorkspaceDto;
import su.taskmanager.data.workspace.entity.Invite;
import su.taskmanager.data.workspace.entity.Workspace;
import su.taskmanager.data.workspace.repository.WorkspaceRepository;
import su.taskmanager.mappers.UserMapper;
import su.taskmanager.mappers.WorkspaceMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;

    @CacheEvict(value = "usersDto", key="#workspace.author.id")
    @Transactional
    public WorkspaceDto createWorkspace(Workspace workspace) {

        Long authorId = workspace.getAuthor().getId();
        User author = userService.getUserById(authorId);
        workspace.addUser(author);
        workspace.setAuthor(author);
        author.addWorkspace(workspace);
        workspaceRepository.save(workspace);
        userService.save(author);
        return WorkspaceMapper.toDto(workspace);
    }


    @Transactional
    @CacheEvict(value = "workspacesDto", key = "#workspace")
    public WorkspaceDto removeUserFromWorkspace(Workspace workspace, User user, UserDto userToRemove) {
        User removeUser = userService.getUserByUsername(userToRemove.getUsername());
        if (isAuthor(workspace, removeUser)) {
            throw new AccessDeniedException("Author of workspace can't remove himself from workspace");
        }
        removeUser.removeWorkspace(workspace);
        workspace.removeUser(removeUser);
        workspaceRepository.save(workspace);
        return WorkspaceMapper.toDto(workspace);
    }

    @CacheEvict(value = "users", key = "#user.id")
    @CachePut(value = "workspacesDto", key = "#workspace.id")
    @Transactional
    public UserDto addUserToWorkspace(Workspace workspace, User user) {
        workspace.addUser(user);
        workspaceRepository.save(workspace);
        return UserMapper.toDto(user);
    }

    @CacheEvict(value = "workspacesDto", key="#workspace.id")
    @Transactional
    public void deleteWorkspace(Workspace workspace) {
        for (User users : workspace.getUsers()) {
            users.getWorkspaces().remove(workspace);
        }
        workspace.getInvites().clear();
        workspace.getUsers().clear();
        workspaceRepository.delete(workspace);
    }

    @CachePut(value = "workspacesDto", key="#workspace.id")
    @Transactional
    public WorkspaceDto updateWorkspaceAuthor(Workspace workspace, User user) {
        workspace.setAuthor(user);
        return WorkspaceMapper.toDto(workspaceRepository.save(workspace));
    }

    @CachePut(value = "workspacesDto", key="#workspace.id")
    @Transactional
    public WorkspaceDto updateWorkspace(Workspace workspace, WorkspaceDto dto) {
        workspace.setNameOfWorkspace(dto.getName());
        workspace.setDescription(dto.getDescription());
        return WorkspaceMapper.toDto(workspaceRepository.save(workspace));
    }

    @CachePut(value = "workspacesDto", key="#workspace.id")
    @Transactional
    WorkspaceDto addInvite(Workspace workspace, Invite invite) {
        workspace.addInvite(invite);
        return WorkspaceMapper.toDto(workspaceRepository.save(workspace));
    }

    @Transactional
    Workspace save(Workspace workspace) {
        return workspaceRepository.save(workspace);
    }

    public Workspace getWorkspace(Long workspaceId) {
        return findById(workspaceId)
                .orElseThrow(() -> new EntityNotFoundException("Workspace not found with id: " + workspaceId));
    }

    @Cacheable(cacheNames = "workspacesDto", key = "#id")
    public WorkspaceDto getWorkspaceDtoById(Long id) {
        return workspaceRepository.findById(id)
                .map(WorkspaceMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Not found user with id: " + id));
    }

    public List<Workspace> findAuthorById(Long id) {
        return workspaceRepository.findByAuthorId(id);
    }

    public Optional<Workspace> findById(Long id) {
        return workspaceRepository.findById(id);
    }

    public boolean isUserInWorkspace(Workspace workspace, User user) {
        return workspace.getUsers().stream()
                .anyMatch(users -> users.getUsername().equals(user.getUsername()));
    }

    public boolean isAuthor(Workspace workspace, User user) {
        return workspace.getAuthor().getUsername().equals(user.getUsername());
    }
}



